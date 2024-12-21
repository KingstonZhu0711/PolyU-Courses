################################################################################
# File: p1_22101071.s                                                         #
# READ ME!!                                                                   #
# This program is to ask an input from the user and it is assumed to be an    #
# integer. Then the code will capture the input and store in a register, with #
# using my own algorithm, the value will be transfered as three forms, Binary #
# Quaternary and Octal. The code will then ask if the user wants to continue. #
# If yes then return back to start.If no is accepted, then the program will   #
# terminate.                                                                  #
###############################################################################

.data
prompt:      .asciiz "Enter a number: "
ans1:        .asciiz "\nInput Number is "
ans2:        .asciiz "\nBinary: "
ans3:        .asciiz "\nQuaternary: "
ans4:        .asciiz "\nOctal: "
bye_message: .asciiz "\nBye!\n"
binary:      .space 33
quaternary:  .space 17
octal:       .space 12

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
    move $t3, $t0
    move $t4, $t0

    # Print "Input Number is "
    la $a0, ans1
    li $v0, 4
    syscall

    # Print the input number
    move $a0, $t0
    li $v0, 1
    syscall

    # Print "Binary: "
    la $a0, ans2
    li $v0, 4
    syscall

    # Convert the input number to binary and store it in binary
    la $a1, binary
    li $t1, 32
convert_binary:
    andi $t2, $t0, 1
    addiu $t2, $t2, '0'
    sb $t2, 0($a1)
    srl $t0, $t0, 1
    addiu $a1, $a1, 1
    addiu $t1, $t1, -1
    bnez $t1, convert_binary

    # Print leading zeros
    li $t1, 32
print_zeros:
    addiu $a1, $a1, -1
    li $v0, 11
    lb $a0, 0($a1)
    syscall
    addiu $t1, $t1, -1
    bnez $t1, print_zeros

    # Print "Quaternary: "
    la $a0, ans3
    li $v0, 4
    syscall

    # Convert the input number to quaternary and store it in buffer
    la $a1, quaternary
    move $t0, $t3
    li $t1, 32
    
convert_Quaternary:
    andi $t2, $t0, 3
    addiu $t2, $t2, '0'
    sb $t2, 0($a1)
    srl $t0, $t0, 2
    addiu $a1, $a1, 1
    addiu $t1, $t1, -1
    bnez $t1, convert_Quaternary

    la $a1, quaternary+16    # Load the address of the last character in the buffer
    li $t1, 17              # Set the counter $t1 to the total number of characters

print_quaternary_zeros:
    addiu $a1, $a1, -1   # Move back one position in the buffer
    li $v0, 11           # Set the system call code for printing a character
    lb $a0, 0($a1)       # Load the character from the buffer
    syscall              # Print the character
    addiu $t1, $t1, -1   # Decrease the counter $t1 by 1
    bnez $t1, print_quaternary_zeros  # If $t1 is not zero, continue the loop


    # Print "Octal: "
    la $a0, ans4
    li $v0, 4
    syscall

    # Convert the input number to octal and store it in buffer
    la $a1, octal
    move $t0, $t4
    li $t1, 32
convert_Octal:
    andi $t2, $t0, 7
    addiu $t2, $t2, '0'
    sb $t2, 0($a1)
    srl $t0, $t0, 3
    addiu $a1, $a1, 1
    addiu $t1, $t1, -1
    bnez $t1, convert_Octal

    # Print octal number
    la $a0, octal
    li $v0, 4
    syscall

    # Print "Bye!"
    la $a0, bye_message
    li $v0, 4
    syscall

    # Exit the program
    li $v0, 10
    syscall
    
##################
# End of Program #
##################
