   .text
   .globl main
main:
    la $a0, ask
    li $v0, 4
    syscall
    
    la $a0, buffer
    la $a1, buffer

    li $v0, 8
    syscall

    la $a0, reply
    li $v0, 4
    syscall

    la $a0, buffer
    li $v0, 4
    syscall

    li $v0, 10
    syscall
    .data
ask:    .asciiz "\n Enter string: "
reply:  .asciiz "\n You wrote: "
buffer: .space 10


