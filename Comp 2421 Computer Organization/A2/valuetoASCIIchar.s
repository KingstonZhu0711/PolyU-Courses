   .data
prompt: .asciiz "Enter a number (32 to 126): "
ans1:   .asciiz "\n Number: "
ans2:   .asciiz "\n ASCII : "
letter: .space 1

    .text
    .globl main
main:
    la $a0, prompt
    li $v0, 4
    syscall

    li $v0, 5
    syscall
    move $t0, $v0

    la $a0, ans1
    li $v0, 4
    syscall

    move $a0, $t0
    li $v0, 1
    syscall

    la $a0, ans2
    li $v0, 4
    syscall

    la $t1, letter
    sb $t0, 0($t1)

    la $a0, letter
    li $v0, 4
    syscall