#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include "sha1.h"

#define NONCE_SIZE 8
#define MAX_INPUT 256

#define BITS_PER_HEX 4
#define MAX_SUFFIX_DIGITS 8

#define SHA1_BLOCK_BYTES 64
#define SHA1_LENGTH_FIELD_POS 56

void hashMessage(const unsigned char* data, int len, unsigned char digest[20])
{
    SHA1_CTX ctx;

    SHA1Init(&ctx);
    SHA1Update(&ctx, data, len);
    SHA1Final(digest, &ctx);
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
    const char* input = "a sequential task";
    const char* suffix = "000000";
    int inputLen = (int)strlen(input);
    int suffixLen = (int)strlen(suffix);

    unsigned char digest[20];
    char hex[41];

    unsigned char message[MAX_INPUT + NONCE_SIZE];
    memcpy(message, input, inputLen);

    printf("search\n");

    unsigned long long nonce = 0;
    clock_t start = clock();

    unsigned int expectedTailBits;
    unsigned int tailCompareMask;

    if (checkSuffix(suffix, &expectedTailBits, &tailCompareMask) != 0) {
        printf("suffix mist be 1..%d hex digits\n", MAX_SUFFIX_DIGITS);
        return 1;
    }

    unsigned char paddedBlock[SHA1_BLOCK_BYTES] = { 0 };

    int messageLen = inputLen + NONCE_SIZE;
    if (messageLen > SHA1_LENGTH_FIELD_POS - 1) {
        printf("input too long for a single sha1 block (max %d bytes)\n", SHA1_LENGTH_FIELD_POS - 1 - NONCE_SIZE);
        return 1;
    }

    memcpy(paddedBlock, input, inputLen);
    paddedBlock[messageLen] = 0x80;

    unsigned long long messageBitLen = (unsigned long long)messageLen * 8;
    for (int i = 0; i < 8; i++) {
        paddedBlock[SHA1_BLOCK_BYTES - 1 - i] = (unsigned char)(messageBitLen >> (8 * i));
    }

    unsigned int hashState[5];

    while (1) {
        for (int i = 0; i < NONCE_SIZE; i++) {
            paddedBlock[inputLen + i] = (unsigned char)(nonce >> (8 * i));
        }

        hashState[0] = 0x67452301;
        hashState[1] = 0xEFCDAB89;
        hashState[2] = 0x98BADCFE;
        hashState[3] = 0x10325476;
        hashState[4] = 0xC3D2E1F0;

        SHA1Transform(hashState, paddedBlock);

        if ((hashState[4] & tailCompareMask) == expectedTailBits) {
            break;
        }

        nonce++;
    }

    for (int wordIndex = 0; wordIndex < 5; wordIndex++) {
        digest[4 * wordIndex + 0] = (unsigned char)(hashState[wordIndex] >> 24);
        digest[4 * wordIndex + 1] = (unsigned char)(hashState[wordIndex] >> 16);
        digest[4 * wordIndex + 2] = (unsigned char)(hashState[wordIndex] >> 8);
        digest[4 * wordIndex + 3] = (unsigned char)(hashState[wordIndex]);
    }

    toHex(digest, hex);

    double seconds = (double)(clock() - start) / CLOCKS_PER_SEC;

    unsigned long long triws = nonce + 1;

    printf("nonce: %llu\n", nonce);
    printf("nocne bytes: ");
    for (int i = 0; i < NONCE_SIZE; i++) {
        printf("%02x", message[inputLen + i]);
    }
    printf("\n");
    printf("sha1: %s\n", hex);
    printf("trials: %llu\n", triws);
    printf("time: %.3f s\n", seconds);

    return 0;
}