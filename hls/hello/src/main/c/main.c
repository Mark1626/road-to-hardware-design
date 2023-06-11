#include "lib/bambu_io.h"

inline void bambu_print(char *str, int len) {
    for (int i = 0; i < len; i++) {
        bambu_putchar(str[i]);
    }
}

int main(void) {
    char str[] = "Hello World";
    int len = sizeof(str) / sizeof(char);
    bambu_print(str, len);
    return 0;
}
