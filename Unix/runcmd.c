#include "apue.h"
#include <sys/wait.h>

static void sig_int(int);

int main(void) {
	char buf[MAXLINE];
	pid_t pid;
	int status;

	if(signal(SIGINT, sig_int) == SIG_ERR) {
		err_sys("signal error");
	}
	printf("%% ");
	while(fgets(buf, MAXLINE, stdin) != NULL) {
		if(buf[strlen(buf) - 1] == '\n')
			buf[strlen(buf) - 1] = 0; 
	}
}
