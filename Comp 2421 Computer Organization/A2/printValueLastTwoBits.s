    .data
prompt: .asciiz "\n Enter a number: "
ans:    .asciiz "\n Last bit is: "
    .text
    .globl main
 main:
    la $a0, prompt
    li $v0, 4
    syscall
    
    li $v0, 5
    syscall

    move $t0, $v0

    la $a0, ans
    li $v0, 4
    syscall

    li $t1, 3

    and $t2, $t0, $t1

    move $a0, $t2
    li $v0, 1
    syscall