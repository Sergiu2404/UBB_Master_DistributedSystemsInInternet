#include <stdio.h>
#include <stdlib.h>

__global__ void add(int* res, const int* a, const int* b, int n) {
    int i = blockIdx.x * blockDim.x + threadIdx.x;
    if (i < n) {
        res[i] = a[i] + b[i];
    }
}

int main()
{
    const int n = 10000000, blockSize = 256;
    const size_t bytes = n * sizeof(int);

    int* a = (int*)malloc(bytes);
    int* b = (int*)malloc(bytes);
    int* res = (int*)malloc(bytes);

    if (!a || !b || !res) {
        puts("Host allocation failed");
        return 1;
    }

    for (int i = 0; i < n; i++) {
        a[i] = i;
        b[i] = i + 24;
        res[i] = -1;
    }

    int* devA, * devB, * devRes;

    cudaMalloc(&devA, bytes);
    cudaMalloc(&devB, bytes);
    cudaMalloc(&devRes, bytes);

    cudaMemcpy(devA, a, bytes, cudaMemcpyHostToDevice);
    cudaMemcpy(devB, b, bytes, cudaMemcpyHostToDevice);

    add << <(n + blockSize - 1) / blockSize, blockSize >> > (devRes, devA, devB, n);

    cudaDeviceSynchronize();
    cudaMemcpy(res, devRes, bytes, cudaMemcpyDeviceToHost);

    int wrong = 0;
    for (int i = 0; i < n; i++) {
        if (res[i] != a[i] + b[i]) wrong++;
    }

    printf("Sample: %d + %d = %d\n", a[12345], b[12345], res[12345]);
    printf("Checked %d elements, wrong: %d\n", n, wrong);
    puts(wrong ? "FAIL" : "PASS");

    cudaFree(devA);
    cudaFree(devB);
    cudaFree(devRes);
    free(a);
    free(b);
    free(res);

    return wrong != 0;
}