#include <stdio.h>
#include <string.h>
#include <time.h>
#include <cuda_runtime.h>

#define NONCE_SIZE 8
#define MAX_INPUT 256
#define MAX_MESSAGE (MAX_INPUT + NONCE_SIZE)

#define BLOCKS 16384
#define THREADS_PER_BLOCK 256
#define NONCES_PER_THREAD 256


/*
SHA-1 in C
By Steve Reid <steve@edmweb.com>
100% Public Domain

Test Vectors (from FIPS PUB 180-1)
"abc"
  A9993E36 4706816A BA3E2571 7850C26C 9CD0D89D
"abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"
  84983E44 1C3BD26E BAAE4AA1 F95129E5 E54670F1
A million repetitions of "a"
  34AA973C D4C4DAA4 F61EEB2B DBAD2731 6534016F
*/

/* #define LITTLE_ENDIAN * This should be #define'd already, if true. */
/* #define SHA1HANDSOFF * Copies data before messing with it. */

#define SHA1HANDSOFF

#include <string.h>

#include <stdint.h>
#include "cuda_runtime.h"

#include "sha1.h"

__host__ __device__ void sha1Copy(void* destination, const void* source, uint32_t size)
{
    unsigned char* d = (unsigned char*)destination;
    unsigned char* s = (unsigned char*)source;

    for (uint32_t i = 0; i < size; i++) {
        d[i] = s[i];
    }
}

__host__ __device__ void sha1Set(void* destination, int value, uint32_t size)
{
    unsigned char* d = (unsigned char*)destination;

    for (uint32_t i = 0; i < size; i++) {
        d[i] = (unsigned char)value;
    }
}


#define rol(value, bits) (((value) << (bits)) | ((value) >> (32 - (bits))))

/* blk0() and blk() perform the initial expand. */
/* I got the idea of expanding during the round function from SSLeay */
#define blk0_le(i) (block->l[i] = (rol(block->l[i],24)&0xFF00FF00) \
    |(rol(block->l[i],8)&0x00FF00FF))
#define blk0_be(i) block->l[i]
#if BYTE_ORDER == LITTLE_ENDIAN
#define blk0(i) blk0_le(i)
#elif BYTE_ORDER == BIG_ENDIAN
#define blk0(i) blk0_be(i)
#else
/* Fall back to a runtime endian check */
const union {
    long l;
    char c;
} sha1_endian = { 1 };
#define blk0(i) (sha1_endian.c == 0 ? blk0_be(i) : blk0_le(i))
#endif

#define blk(i) (block->l[i&15] = rol(block->l[(i+13)&15]^block->l[(i+8)&15] \
    ^block->l[(i+2)&15]^block->l[i&15],1))

/* (R0+R1), R2, R3, R4 are the different operations used in SHA1 */
#define R0(v,w,x,y,z,i) z+=((w&(x^y))^y)+blk0(i)+0x5A827999+rol(v,5);w=rol(w,30);
#define R1(v,w,x,y,z,i) z+=((w&(x^y))^y)+blk(i)+0x5A827999+rol(v,5);w=rol(w,30);
#define R2(v,w,x,y,z,i) z+=(w^x^y)+blk(i)+0x6ED9EBA1+rol(v,5);w=rol(w,30);
#define R3(v,w,x,y,z,i) z+=(((w|x)&y)|(w&x))+blk(i)+0x8F1BBCDC+rol(v,5);w=rol(w,30);
#define R4(v,w,x,y,z,i) z+=(w^x^y)+blk(i)+0xCA62C1D6+rol(v,5);w=rol(w,30);


/* Hash a single 512-bit block. This is the core of the algorithm. */

__host__ __device__ void SHA1Transform(
    uint32_t state[5],
    const unsigned char buffer[64]
)
{
    uint32_t a, b, c, d, e;

    typedef union
    {
        unsigned char c[64];
        uint32_t l[16];
    } CHAR64LONG16;

#ifdef SHA1HANDSOFF
    CHAR64LONG16 block[1];      /* use array to appear as a pointer */

    sha1Copy(block, buffer, 64);
#else
    /* The following had better never be used because it causes the
     * pointer-to-const buffer to be cast into a pointer to non-const.
     * And the result is written through.  I threw a "const" in, hoping
     * this will cause a diagnostic.
     */
    CHAR64LONG16* block = (const CHAR64LONG16*)buffer;
#endif
    /* Copy context->state[] to working vars */
    a = state[0];
    b = state[1];
    c = state[2];
    d = state[3];
    e = state[4];
    /* 4 rounds of 20 operations each. Loop unrolled. */
    R0(a, b, c, d, e, 0);
    R0(e, a, b, c, d, 1);
    R0(d, e, a, b, c, 2);
    R0(c, d, e, a, b, 3);
    R0(b, c, d, e, a, 4);
    R0(a, b, c, d, e, 5);
    R0(e, a, b, c, d, 6);
    R0(d, e, a, b, c, 7);
    R0(c, d, e, a, b, 8);
    R0(b, c, d, e, a, 9);
    R0(a, b, c, d, e, 10);
    R0(e, a, b, c, d, 11);
    R0(d, e, a, b, c, 12);
    R0(c, d, e, a, b, 13);
    R0(b, c, d, e, a, 14);
    R0(a, b, c, d, e, 15);
    R1(e, a, b, c, d, 16);
    R1(d, e, a, b, c, 17);
    R1(c, d, e, a, b, 18);
    R1(b, c, d, e, a, 19);
    R2(a, b, c, d, e, 20);
    R2(e, a, b, c, d, 21);
    R2(d, e, a, b, c, 22);
    R2(c, d, e, a, b, 23);
    R2(b, c, d, e, a, 24);
    R2(a, b, c, d, e, 25);
    R2(e, a, b, c, d, 26);
    R2(d, e, a, b, c, 27);
    R2(c, d, e, a, b, 28);
    R2(b, c, d, e, a, 29);
    R2(a, b, c, d, e, 30);
    R2(e, a, b, c, d, 31);
    R2(d, e, a, b, c, 32);
    R2(c, d, e, a, b, 33);
    R2(b, c, d, e, a, 34);
    R2(a, b, c, d, e, 35);
    R2(e, a, b, c, d, 36);
    R2(d, e, a, b, c, 37);
    R2(c, d, e, a, b, 38);
    R2(b, c, d, e, a, 39);
    R3(a, b, c, d, e, 40);
    R3(e, a, b, c, d, 41);
    R3(d, e, a, b, c, 42);
    R3(c, d, e, a, b, 43);
    R3(b, c, d, e, a, 44);
    R3(a, b, c, d, e, 45);
    R3(e, a, b, c, d, 46);
    R3(d, e, a, b, c, 47);
    R3(c, d, e, a, b, 48);
    R3(b, c, d, e, a, 49);
    R3(a, b, c, d, e, 50);
    R3(e, a, b, c, d, 51);
    R3(d, e, a, b, c, 52);
    R3(c, d, e, a, b, 53);
    R3(b, c, d, e, a, 54);
    R3(a, b, c, d, e, 55);
    R3(e, a, b, c, d, 56);
    R3(d, e, a, b, c, 57);
    R3(c, d, e, a, b, 58);
    R3(b, c, d, e, a, 59);
    R4(a, b, c, d, e, 60);
    R4(e, a, b, c, d, 61);
    R4(d, e, a, b, c, 62);
    R4(c, d, e, a, b, 63);
    R4(b, c, d, e, a, 64);
    R4(a, b, c, d, e, 65);
    R4(e, a, b, c, d, 66);
    R4(d, e, a, b, c, 67);
    R4(c, d, e, a, b, 68);
    R4(b, c, d, e, a, 69);
    R4(a, b, c, d, e, 70);
    R4(e, a, b, c, d, 71);
    R4(d, e, a, b, c, 72);
    R4(c, d, e, a, b, 73);
    R4(b, c, d, e, a, 74);
    R4(a, b, c, d, e, 75);
    R4(e, a, b, c, d, 76);
    R4(d, e, a, b, c, 77);
    R4(c, d, e, a, b, 78);
    R4(b, c, d, e, a, 79);
    /* Add the working vars back into context.state[] */
    state[0] += a;
    state[1] += b;
    state[2] += c;
    state[3] += d;
    state[4] += e;
    /* Wipe variables */
    a = b = c = d = e = 0;
#ifdef SHA1HANDSOFF
    sha1Set(block, '\0', sizeof(block));
#endif
}


/* SHA1Init - Initialize new context */

__host__ __device__ void SHA1Init(
    SHA1_CTX* context
)
{
    /* SHA1 initialization constants */
    context->state[0] = 0x67452301;
    context->state[1] = 0xEFCDAB89;
    context->state[2] = 0x98BADCFE;
    context->state[3] = 0x10325476;
    context->state[4] = 0xC3D2E1F0;
    context->count[0] = context->count[1] = 0;
}


/* Run your data through this. */

__host__ __device__ void SHA1Update(
    SHA1_CTX* context,
    const unsigned char* data,
    uint32_t len
)
{
    uint32_t i;

    uint32_t j;

    j = context->count[0];
    if ((context->count[0] += len << 3) < j)
        context->count[1]++;
    context->count[1] += (len >> 29);
    j = (j >> 3) & 63;
    if ((j + len) > 63)
    {
        sha1Copy(&context->buffer[j], data, (i = 64 - j));
        SHA1Transform(context->state, context->buffer);
        for (; i + 63 < len; i += 64)
        {
            SHA1Transform(context->state, &data[i]);
        }
        j = 0;
    }
    else
        i = 0;
    sha1Copy(&context->buffer[j], &data[i], len - i);
}


/* Add padding and return the message digest. */

__host__ __device__ void SHA1Final(
    unsigned char digest[20],
    SHA1_CTX* context
)
{
    unsigned i;

    unsigned char finalcount[8];

    unsigned char c;

#if 0    /* untested "improvement" by DHR */
    /* Convert context->count to a sequence of bytes
     * in finalcount.  Second element first, but
     * big-endian order within element.
     * But we do it all backwards.
     */
    unsigned char* fcp = &finalcount[8];

    for (i = 0; i < 2; i++)
    {
        uint32_t t = context->count[i];

        int j;

        for (j = 0; j < 4; t >>= 8, j++)
            *--fcp = (unsigned char)t
    }
#else
    for (i = 0; i < 8; i++)
    {
        finalcount[i] = (unsigned char)((context->count[(i >= 4 ? 0 : 1)] >> ((3 - (i & 3)) * 8)) & 255);      /* Endian independent */
    }
#endif
    c = 0200;
    SHA1Update(context, &c, 1);
    while ((context->count[0] & 504) != 448)
    {
        c = 0000;
        SHA1Update(context, &c, 1);
    }
    SHA1Update(context, finalcount, 8); /* Should cause a SHA1Transform() */
    for (i = 0; i < 20; i++)
    {
        digest[i] = (unsigned char)
            ((context->state[i >> 2] >> ((3 - (i & 3)) * 8)) & 255);
    }
    /* Wipe variables */
    sha1Set(context, '\0', sizeof(*context));
    sha1Set(&finalcount, '\0', sizeof(finalcount));
}

void SHA1(
    char* hash_out,
    const char* str,
    uint32_t len)
{
    SHA1_CTX ctx;
    unsigned int ii;

    SHA1Init(&ctx);
    for (ii = 0; ii < len; ii += 1)
        SHA1Update(&ctx, (const unsigned char*)str + ii, 1);
    SHA1Final((unsigned char*)hash_out, &ctx);
}




__host__ __device__ void hashMessage(const unsigned char* data, int len, unsigned char digest[20])
{
    SHA1_CTX ctx;

    SHA1Init(&ctx);
    SHA1Update(&ctx, data, len);
    SHA1Final(digest, &ctx);
}


__host__ __device__ char hexDigit(int value)
{
    return value < 10 
        ? (char)('0' + value) 
        : (char)('a' + value - 10);
}

__host__ __device__ void toHex(const unsigned char digest[20], char hex[41])
{
    for (int i = 0; i < 20; i++) {
        hex[2 * i] = hexDigit(digest[i] >> 4);
        hex[2 * i + 1] = hexDigit(digest[i] & 0x0F);
    }

    hex[40] = '\0';
}

__host__ __device__ void buildMessage(unsigned char* input, int inputLength, unsigned long long nonce, unsigned char* message)
{
    for (int i = 0; i < inputLength; i++) {
        message[i] = input[i];
    }
    for (int i = 0; i < NONCE_SIZE; i++) {
        message[inputLength + i] = (unsigned char)(nonce >> (8 * i));
    }
}

//__global__ void searchNonce(unsigned char* input, int inputLength, const char* suffix, int suffixLength, unsigned long long firstNonce, volatile int* found, unsigned long long* foundNonce)
//{
//    /*if (*found) {
//        return;
//    }*/
//
//    unsigned long long threadId = (unsigned long long) blockIdx.x * blockDim.x + threadIdx.x;
//    unsigned long long stride = gridDim.x * blockDim.x;
//
//    unsigned long long nonce = firstNonce + threadId;
//
//    unsigned char concatenatedMessage[MAX_MESSAGE];
//    unsigned char rawHash[20];
//    char hexHash[41];
//
//    for (int i = 0; i < NONCES_PER_THREAD; i++) {
//
//        if (*found) {
//            return;
//        }
//
//        buildMessage(input, inputLength, nonce, concatenatedMessage);
//        hashMessage(concatenatedMessage, inputLength + NONCE_SIZE, rawHash);
//        toHex(rawHash, hexHash);
//
//        bool match = true;
//        for (int j = 0; j < suffixLength; j++) {
//            if (hexHash[40 - suffixLength + j] != suffix[j]) {
//                match = false;
//                break;
//            }
//        }
//
//        if (match) {
//            *foundNonce = nonce;
//            *found = 1;
//            return;
//        }
//
//        nonce += stride;
//    }
//}


__global__ void searchNonce(unsigned char* input, int inputLength, const char* suffix, int suffixLength, unsigned long long firstNonce, int* found, unsigned long long* foundNonce)
{
    if (*found) {
        return;
    }

    unsigned long long threadId = (unsigned long long) blockIdx.x * blockDim.x + threadIdx.x;
    unsigned long long nonce = firstNonce + threadId;

    unsigned char concatenatedMessage[MAX_MESSAGE];
    unsigned char rawHash[20];
    char hexHash[41];

    buildMessage(input, inputLength, nonce, concatenatedMessage);
    hashMessage(concatenatedMessage, inputLength + NONCE_SIZE, rawHash);
    toHex(rawHash, hexHash);

    for (int i = 0; i < suffixLength; i++) {
        if (hexHash[40 - suffixLength + i] != suffix[i]) {
            return;
        }
    }

    *foundNonce = nonce;
    *found = 1;
}

int main()
{
    char* input = "a cuda task";
    char* suffix = "00000000";
    int inputLength = (int)strlen(input);
    int suffixLength = (int)strlen(suffix);

    unsigned char message[MAX_MESSAGE];
    unsigned char digest[20];
    char hex[41];

    unsigned char* devInput;
    char* devSuffix;
    int* devFound;
    unsigned long long* devNonce;

    cudaMalloc(&devInput, inputLength);
    cudaMalloc(&devSuffix, suffixLength);
    cudaMalloc(&devFound, sizeof(int));
    cudaMalloc(&devNonce, sizeof(unsigned long long));

    cudaMemcpy(devInput, input, inputLength, cudaMemcpyHostToDevice);
    cudaMemcpy(devSuffix, suffix, suffixLength, cudaMemcpyHostToDevice);
    cudaMemset(devFound, 0, sizeof(int));

    //unsigned long long noncesPerLaunch = (unsigned long long)BLOCKS * THREADS_PER_BLOCK * NONCES_PER_THREAD;
    unsigned long long noncesPerLaunch = (unsigned long long)BLOCKS * THREADS_PER_BLOCK;

    unsigned long long firstNonce = 0;
    int found = 0;

    printf("searching cuda...\n");
    clock_t start = clock();

    while (!found) {
        searchNonce <<<BLOCKS, THREADS_PER_BLOCK>>>(devInput, inputLength, devSuffix, suffixLength, firstNonce, devFound, devNonce);

        if (cudaDeviceSynchronize() != cudaSuccess) {
            printf("error\n");
            return 1;
        }

        cudaMemcpy(&found, devFound, sizeof(int), cudaMemcpyDeviceToHost);
        firstNonce += noncesPerLaunch;
    }

    double seconds = (double)(clock() - start) / CLOCKS_PER_SEC;

    unsigned long long nonce;
    cudaMemcpy(&nonce, devNonce, sizeof(nonce), cudaMemcpyDeviceToHost);

    buildMessage((unsigned char*)input, inputLength, nonce, message);
    hashMessage(message, inputLength + NONCE_SIZE, digest);
    toHex(digest, hex);

    if (strcmp(hex + 40 - suffixLength, suffix) != 0) {
        printf("incorrect\n");
        return 1;
    }

    printf("nonce bytes: ");
    for (int i = 0; i < NONCE_SIZE; i++) {
        printf("%02x", (unsigned char)(nonce >> (8 * i)));
    }
    printf("nonce: %llu\n", nonce);
    printf("sha1: %s\n", hex);
    printf("tries: %llu\n", firstNonce);
    printf("time: %.3f s\n", seconds);

    cudaFree(devInput);
    cudaFree(devSuffix);
    cudaFree(devFound);
    cudaFree(devNonce);

    return 0;
}