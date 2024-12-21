.data
prompt: .asciiz "Enter a number: "
ans1: .asciiz "\nInput Number is "
ans2: .asciiz "\nQuaternary: "
buffer: .space 33

.text
.globl main
main:
    # Print the prompt
    la $a0, prompt
    li $v0, 4
    syscall

    # Read the input number
    li $v0, 5
    syscall
    move $t0, $v0

    # Print "Input Number is "
    la $a0, ans1
    li $v0, 4
    syscall

    # Print the input number
    move $a0, $t0
    li $v0, 1
    syscall

    # Print "Quaternary: "
    la $a0, ans2
    li $v0, 4
    syscall

    # Convert the input number to quaternary and store it in buffer
    la $a1, buffer
    li $t1, 32
convert:
    andi $t2, $t0, 3
    addiu $t2, $t2, '0'
    sb $t2, 0($a1)
    srl $t0, $t0, 2
    addiu $a1, $a1, 1
    addiu $t1, $t1, -1
    bnez $t1, convert

    # Print the quaternary number
    la $a0, buffer
    li $v0, 4
    syscall

    # Exit the program
    li $v0, 10
    syscall