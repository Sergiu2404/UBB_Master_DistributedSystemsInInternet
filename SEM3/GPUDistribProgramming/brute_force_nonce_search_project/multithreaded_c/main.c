#include <pthread.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "sha1.h"

#define NONCE_SIZE 8
#define MAX_INPUT 256
#define MAX_THREADS 64

#define BITS_PER_HEX 4
#define MAX_SUFFIX_DIGITS 8

#define SHA1_BLOCK_BYTES 64
#define SHA1_LENGTH_FIELD_POS 56

typedef struct {
    int threadIndex;
    int threadCount;

    unsigned long long triesCount;

    int hasFound;
    unsigned long long foundNonce;
    unsigned int foundState[5];
} WorkerThread;

const char* inputText = "a multithreaded task";
const char* targetSuffix = "00000000";
int inputLength;

unsigned char basePaddedBlock[SHA1_BLOCK_BYTES];
unsigned int expectedTailBits;
unsigned int tailCompareMask;

volatile int isFound = 0;

void hashMessage(const unsigned char* data, int len, unsigned char digest[20])
{
    SHA1_CTX ctx;

    SHA1Init(&ctx);
    SHA1Update(&ctx, data, len);
    SHA1Final(digest, &ctx);
}

static double nowSeconds(void)
{
    struct timespec ts;
    timespec_get(&ts, TIME_UTC);
    return (double)ts.tv_sec + (double)ts.tv_nsec / 1e9;
}

void toHex(const unsigned char digest[20], char hex[41])
{
    const char digits[] = "0123456789abcdef";

    for (int i = 0; i < 20; i++) {
        hex[2 * i] = digits[digest[i] >> 4];
        hex[2 * i + 1] = digits[digest[i] & 0x0F];
    }

    hex[40] = '\0';
}

void* searchNonce(void* parameter)
{
    WorkerThread* worker = (WorkerThread*)parameter;

    unsigned char block[SHA1_BLOCK_BYTES];
    memcpy(block, basePaddedBlock, SHA1_BLOCK_BYTES);

    unsigned long long nonce = (unsigned long long)worker->threadIndex;
    const unsigned long long stride = (unsigned long long)worker->threadCount;
    unsigned long long localTries = 0;
    unsigned int hashState[5];

    while (!isFound) {
        for (int i = 0; i < NONCE_SIZE; i++) {
            block[inputLength + i] = (unsigned char)(nonce >> (8 * i));
        }

        hashState[0] = 0x67452301;
        hashState[1] = 0xEFCDAB89;
        hashState[2] = 0x98BADCFE;
        hashState[3] = 0x10325476;
        hashState[4] = 0xC3D2E1F0;

        SHA1Transform(hashState, block);
        localTries++;

        if ((hashState[4] & tailCompareMask) == expectedTailBits) {
            worker->hasFound = 1;
            worker->foundNonce = nonce;

            memcpy(worker->foundState, hashState, sizeof(hashState));
            isFound = 1;

            break;
        }

        nonce += stride;
    }

    worker->triesCount = localTries;

    return NULL;
}

int checkSuffix(const char* hexSuffix, unsigned int* expectedTailBits, unsigned int* tailCompareMask)
{
    int suffixDigitCount = (int)strlen(hexSuffix);
    if (suffixDigitCount < 1 || suffixDigitCount > MAX_SUFFIX_DIGITS) {
        return -1;
    }

    int bitsToCompare = suffixDigitCount * BITS_PER_HEX;

    *expectedTailBits = (unsigned int)strtoul(hexSuffix, NULL, 16);
    *tailCompareMask = (bitsToCompare == 32)
        ? 0xFFFFFFFFu
        : ((1u << bitsToCompare) - 1u);

    return 0;
}

int main(int argc, char* argv[])
{
    int threadCount = 12;

    inputLength = (int)strlen(inputText);
    if (checkSuffix(targetSuffix, &expectedTailBits, &tailCompareMask) != 0) {
        printf("suffix mist be 1..%d hex digits\n", MAX_SUFFIX_DIGITS);
        return 1;
    }

    int messageLen = inputLength + NONCE_SIZE;
    if (messageLen > SHA1_LENGTH_FIELD_POS - 1) {
        printf("input too long for a single sha1 block (max %d bytes)\n",
            SHA1_LENGTH_FIELD_POS - 1 - NONCE_SIZE);
        return 1;
    }

    memset(basePaddedBlock, 0, SHA1_BLOCK_BYTES);
    memcpy(basePaddedBlock, inputText, inputLength);
    basePaddedBlock[messageLen] = 0x80;
    unsigned long long messageBitLen = (unsigned long long)messageLen * 8;
    for (int i = 0; i < 8; i++) {
        basePaddedBlock[SHA1_BLOCK_BYTES - 1 - i] = (unsigned char)(messageBitLen >> (8 * i));
    }

    pthread_t threads[MAX_THREADS];
    WorkerThread workers[MAX_THREADS];

    printf("search: %d threads...\n", threadCount);

    double start = nowSeconds();

    for (int i = 0; i < threadCount; i++) {
        workers[i].threadIndex = i;
        workers[i].threadCount = threadCount;
        workers[i].triesCount = 0;
        workers[i].hasFound = 0;

        pthread_create(&threads[i], NULL, searchNonce, &workers[i]);
    }

    for (int i = 0; i < threadCount; i++) {
        pthread_join(threads[i], NULL);
    }

    double seconds = nowSeconds() - start;

    unsigned long long totalTries = 0;
    WorkerThread* winner = NULL;

    for (int i = 0; i < threadCount; i++) {
        totalTries += workers[i].triesCount;
        if (workers[i].hasFound && (winner == NULL || workers[i].foundNonce < winner->foundNonce)) {
            winner = &workers[i];
        }
    }

    if (winner == NULL) {
        printf("no nonce found\n");
        return 1;
    }

    unsigned char digest[20];
    char hex[41];

    for (int wordIndex = 0; wordIndex < 5; wordIndex++) {
        digest[4 * wordIndex + 0] = (unsigned char)(winner->foundState[wordIndex] >> 24);
        digest[4 * wordIndex + 1] = (unsigned char)(winner->foundState[wordIndex] >> 16);
        digest[4 * wordIndex + 2] = (unsigned char)(winner->foundState[wordIndex] >> 8);
        digest[4 * wordIndex + 3] = (unsigned char)(winner->foundState[wordIndex]);
    }
    toHex(digest, hex);

    printf("nonce: %llu\n", winner->foundNonce);
    printf("nonce bytes: ");
    for (int i = 0; i < NONCE_SIZE; i++) {
        printf("%02x", (unsigned char)(winner->foundNonce >> (8 * i)));
    }
    printf("\n");

    printf("sha1: %s\n", hex);
    printf("threads: %d\n", threadCount);
    printf("tries: %llu\n", totalTries);
    printf("time: %.3f s\n", seconds);
    printf("throughput: %.2f tries/sec\n", (double)totalTries / seconds);

    return 0;
}