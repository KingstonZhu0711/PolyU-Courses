#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

void main() {
    int fd; 
    char buf[1024];
    fd = open("/dev/helloworld", O_RDONLY);
    if (fd < 0) {
        perror("Open /dev/helloworld failure.");
        exit(EXIT_FAILURE);
    }
    int num = read(fd, buf, sizeof(buf)-1);
    buf[num] = 0;
    printf("Got the message from /dev/helloworld: \"%s\".\n", buf);
    close(fd);
    exit(EXIT_SUCCESS);
}

