#include <stdio.h>

#include "arr_passing.h"

int main(int argc, char **argv) {
    int n = 16;
    int sz = n * n;
    int arr[sz];
    for (int i=0; i < sz; i++) {
        arr[i] = i+1;
    }

    int out[n];
    int exp_out[n];
    int sum_const = (n * (n+1) / 2);
    for (int i = 0; i < n; i++) {
        exp_out[i] = (i * (n*n)) + sum_const;
    }

    ArrayPass(arr, out,n);

    int err = 0;
    for (int i = 0; i < n; i++) {
        int check = exp_out[i] != out[i];
        if (check) {
            err = err | check;
            printf("Expected: %d Actual: %d\n", exp_out[i], out[i]);
        }
    }

    return err;
}
