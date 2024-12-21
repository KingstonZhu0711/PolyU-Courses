.data
prompt: .asciiz "Enter a number: "
ans1: .asciiz "\nInput Number is "
ans2: .asciiz "\nOctal: "
buffer: .space 12

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

    # Print "Octal: "
    la $a0, ans2
    li $v0, 4
    syscall

    # Convert the input number to octal and store it in buffer
    la $a1, buffer
    li $t1, 12
convert:
    andi $t2, $t0, 7
    addiu $t2, $t2, '0'
    sb $t2, 0($a1)
    srl $t0, $t0, 3
    addiu $a1, $a1, 1
    addiu $t1, $t1, -1
    bnez $t1, convert
    
 # Print the octal number stored in buffer in reverse order
    la $a0, buffer
    addiu $a0, $a0, 11  # Start from the last character in the buffer
print_reverse:
    lb $a1, 0($a0)       # Load the character from the buffer
    beqz $a1, exit       # If the character is null, exit the loop
    li $v0, 11           # Set the system call code for printing a character
    move $a0, $a1       # Move the character to $a0 for printing
    syscall              # Print the character
    addiu $a0, $a0, -1   # Move to the previous character in the buffer
    j print_reverse



    # Print the octal number
    li $v0, 4
    la $a0, buffer  # Start from the 11th digit (buffer+22) to print only 11 digits
    syscall

    # Exit the program
    li $v0, 10
    syscall