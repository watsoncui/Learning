/*
Example 1.7: print user ID and group ID
*/
#include "apue.h"

int main(void) {
	printf("uid = %d, gid = %d\n", getuid(), getgid());
	exit(0);
}
