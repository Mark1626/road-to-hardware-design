#include <stdio.h>
#include "repeater.h"

int main() {
    int arr[100];
    for (int i = 0; i < 100; i++) {
        arr[i] = i;
    }

    int exp[200];
    int act[200];
    for (int i = 0; i < 200; i++) {
        exp[i] = i / 2;
    }

    RepeaterTop(arr, act, 100);

    int err = 0;
    for (int i = 0; i < 200; i++) {
        int cond = exp[i] != act[i];
        if (cond) {
            err = err | cond;
            printf("Exp: %d Act: %d\n", exp[i], act[i]); 
        }
    }

    return err;
}
