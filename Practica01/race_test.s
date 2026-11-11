	.file	"race_test.c"
	.text
	.globl	global_counter
	.data
	.align 4
global_counter:
	.long	20
	.globl	mutex
	.align 8
mutex:
	.quad	-1
	.section .rdata,"dr"
.LC0:
	.ascii "starting thread...\0"
.LC1:
	.ascii "Thread1 - Contador: %d\12\0"
	.align 8
.LC2:
	.ascii "C:/Users/diego/OneDrive/Documentos/GitHub/Work/Concurrencia_y_Paralelismo/Practica01/readme.txt\0"
	.text
	.globl	thread_routine
	.def	thread_routine;	.scl	2;	.type	32;	.endef
	.seh_proc	thread_routine
thread_routine:
	pushq	%rbp
	.seh_pushreg	%rbp
	movq	%rsp, %rbp
	.seh_setframe	%rbp, 0
	subq	$144, %rsp
	.seh_stackalloc	144
	.seh_endprologue
	movq	%rcx, 16(%rbp)
	movq	16(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, -8(%rbp)
	leaq	.LC0(%rip), %rax
	movq	%rax, %rcx
	call	puts
	movl	$0, -4(%rbp)
	jmp	.L2
.L3:
	leaq	mutex(%rip), %rax
	movq	%rax, %rcx
	call	pthread_mutex_lock
	movl	global_counter(%rip), %eax
	addl	$1, %eax
	movl	%eax, global_counter(%rip)
	movl	global_counter(%rip), %edx
	leaq	.LC1(%rip), %rcx
	leaq	-112(%rbp), %rax
	movl	%edx, %r9d
	movq	%rcx, %r8
	movl	$100, %edx
	movq	%rax, %rcx
	call	__mingw_snprintf
	leaq	.LC2(%rip), %rax
	movl	$420, %r8d
	movl	$265, %edx
	movq	%rax, %rcx
	call	open
	movl	%eax, -12(%rbp)
	leaq	-112(%rbp), %rax
	movq	%rax, %rcx
	call	strlen
	movl	%eax, %ecx
	leaq	-112(%rbp), %rdx
	movl	-12(%rbp), %eax
	movl	%ecx, %r8d
	movl	%eax, %ecx
	call	write
	movl	-12(%rbp), %eax
	movl	%eax, %ecx
	call	close
	leaq	mutex(%rip), %rax
	movq	%rax, %rcx
	call	pthread_mutex_unlock
	addl	$1, -4(%rbp)
.L2:
	movl	-4(%rbp), %eax
	cmpl	-8(%rbp), %eax
	jl	.L3
	movl	$0, %eax
	addq	$144, %rsp
	popq	%rbp
	ret
	.seh_endproc
	.section .rdata,"dr"
.LC3:
	.ascii "starting thread two...\0"
.LC4:
	.ascii "Thread 2 - Contador: %d\12\0"
	.text
	.globl	thread_routine_two
	.def	thread_routine_two;	.scl	2;	.type	32;	.endef
	.seh_proc	thread_routine_two
thread_routine_two:
	pushq	%rbp
	.seh_pushreg	%rbp
	movq	%rsp, %rbp
	.seh_setframe	%rbp, 0
	subq	$144, %rsp
	.seh_stackalloc	144
	.seh_endprologue
	movq	%rcx, 16(%rbp)
	movq	16(%rbp), %rax
	movl	(%rax), %eax
	movl	%eax, -8(%rbp)
	leaq	.LC3(%rip), %rax
	movq	%rax, %rcx
	call	puts
	movl	$0, -4(%rbp)
	jmp	.L6
.L7:
	leaq	mutex(%rip), %rax
	movq	%rax, %rcx
	call	pthread_mutex_lock
	movl	global_counter(%rip), %eax
	subl	$1, %eax
	movl	%eax, global_counter(%rip)
	movl	global_counter(%rip), %edx
	leaq	.LC4(%rip), %rcx
	leaq	-112(%rbp), %rax
	movl	%edx, %r9d
	movq	%rcx, %r8
	movl	$100, %edx
	movq	%rax, %rcx
	call	__mingw_snprintf
	leaq	.LC2(%rip), %rax
	movl	$420, %r8d
	movl	$265, %edx
	movq	%rax, %rcx
	call	open
	movl	%eax, -12(%rbp)
	leaq	-112(%rbp), %rax
	movq	%rax, %rcx
	call	strlen
	movl	%eax, %ecx
	leaq	-112(%rbp), %rdx
	movl	-12(%rbp), %eax
	movl	%ecx, %r8d
	movl	%eax, %ecx
	call	write
	movl	-12(%rbp), %eax
	movl	%eax, %ecx
	call	close
	leaq	mutex(%rip), %rax
	movq	%rax, %rcx
	call	pthread_mutex_unlock
	addl	$1, -4(%rbp)
.L6:
	movl	-4(%rbp), %eax
	cmpl	-8(%rbp), %eax
	jl	.L7
	movl	$0, %eax
	addq	$144, %rsp
	popq	%rbp
	ret
	.seh_endproc
	.section .rdata,"dr"
.LC5:
	.ascii "Uso: %s <numero_de_lineas>\12\0"
	.text
	.globl	main
	.def	main;	.scl	2;	.type	32;	.endef
	.seh_proc	main
main:
	pushq	%rbp
	.seh_pushreg	%rbp
	movq	%rsp, %rbp
	.seh_setframe	%rbp, 0
	subq	$64, %rsp
	.seh_stackalloc	64
	.seh_endprologue
	movl	%ecx, 16(%rbp)
	movq	%rdx, 24(%rbp)
	call	__main
	cmpl	$1, 16(%rbp)
	jg	.L10
	movq	24(%rbp), %rax
	movq	(%rax), %rax
	leaq	.LC5(%rip), %rcx
	movq	%rax, %rdx
	call	__mingw_printf
	movl	$-1, %eax
	jmp	.L14
.L10:
	movl	$0, -4(%rbp)
	movq	24(%rbp), %rax
	addq	$8, %rax
	movq	(%rax), %rax
	movq	%rax, %rcx
	call	atoi
	movl	%eax, -4(%rbp)
	leaq	-4(%rbp), %rdx
	leaq	thread_routine(%rip), %rcx
	leaq	-16(%rbp), %rax
	movq	%rdx, %r9
	movq	%rcx, %r8
	movl	$0, %edx
	movq	%rax, %rcx
	call	pthread_create
	testl	%eax, %eax
	je	.L12
	movl	$-1, %eax
	jmp	.L14
.L12:
	leaq	-4(%rbp), %rdx
	leaq	thread_routine_two(%rip), %rcx
	leaq	-24(%rbp), %rax
	movq	%rdx, %r9
	movq	%rcx, %r8
	movl	$0, %edx
	movq	%rax, %rcx
	call	pthread_create
	testl	%eax, %eax
	je	.L13
	movl	$-1, %eax
	jmp	.L14
.L13:
	movq	-16(%rbp), %rax
	movl	$0, %edx
	movq	%rax, %rcx
	call	pthread_join
	movq	-24(%rbp), %rax
	movl	$0, %edx
	movq	%rax, %rcx
	call	pthread_join
	movl	$0, %eax
.L14:
	addq	$64, %rsp
	popq	%rbp
	ret
	.seh_endproc
	.def	__main;	.scl	2;	.type	32;	.endef
	.ident	"GCC: (MinGW-W64 x86_64-msvcrt-posix-seh, built by Brecht Sanders, r4) 16.1.0"
	.def	puts;	.scl	2;	.type	32;	.endef
	.def	pthread_mutex_lock;	.scl	2;	.type	32;	.endef
	.def	open;	.scl	2;	.type	32;	.endef
	.def	strlen;	.scl	2;	.type	32;	.endef
	.def	write;	.scl	2;	.type	32;	.endef
	.def	close;	.scl	2;	.type	32;	.endef
	.def	pthread_mutex_unlock;	.scl	2;	.type	32;	.endef
	.def	atoi;	.scl	2;	.type	32;	.endef
	.def	pthread_create;	.scl	2;	.type	32;	.endef
	.def	pthread_join;	.scl	2;	.type	32;	.endef
