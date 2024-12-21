##################################################################################
# File: p1_22101071.s                                                            #
# READ ME!!                                                                      #
# This program is to ask an input from the user and it is assumed to be an       #
# integer. Then the code will capture the input and store in a register, with    #
# using my own algorithm, the value will be transfered as three forms, Binary    #
# Quaternary and Octal. My own algorithm is I first take the input from the user #    
# and store into three register for three differenct converting process, then I  #
# convert decimal number into Binary/Quaternary/Octal by using loop to extract   #
# the least significant 1/2/3 bit each time and converting it to an ASCII        #
# character because it will represent (0,1)/(0,1,2,3)/(0,1,2,3,4,5,6,7) for      #
# each of the converted result. Then I will continue in the loop to shift 1/2/3  #
# postions to continue execute until all numbers are processed. Then I will      #
# print out the results in reverse order and add extra number of zeros to fit    #
# 32/16/11 number of bits format. Finally, The code will then ask if the user    #
# wants to continue.If yes then return back to start.If no is accepted,          #
# then the program will terminate.                                               #
##################################################################################

####################
# The data segment #
####################
	
    .data

# Create some null terminated strings which are to be used in the program

prompt:             .asciiz "Enter a number: "
ans1:               .asciiz "Input Number is "
ans2:               .asciiz "\nBinary: "
ans3:               .asciiz "\nQuaternary: "
ans4:               .asciiz "\nOctal: "
continue_message:   .asciiz"\nContinue? (1=Yes, 0=No): "
bye_message:        .asciiz "Bye!"
binary:             .space 33
quaternary:         .space 17
octal:              .space 12

.text
.globl main
main:
    # STEP 1 -- get the first operand
    # Print a prompt asking user for input

    la $a0, prompt              # "load address" of the string
    li $v0, 4                   # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    # Now read in the first/second/third operand for later usage

    li $v0, 5                   # syscall number 5 reads an int
    syscall                     # actually read the int
    move $t0, $v0               # save result in $t0 for later
    move $t3, $t0               # save result in $t3 for later
    move $t4, $t0               # save result in $t4 for later

    # STEP 2 -- print the input
    # Print "Input Number is "

    la $a0, ans1                # "load address" of the string
    li $v0, 4                   # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    # Then print out the input number by user

    move $a0, $t0               # Move the input number to register $a0
    li $v0, 1                   # syscall number 1 prints int
    syscall                     # actually print the int

    # STEP 3 -- print the Binary form of the input
    # Print "Binary: "

    la $a0, ans2                # "load address" of the string
    li $v0, 4                   # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    # Then, convert the input number to binary and store it in binary

    la $a1, binary              # Load the address of the binary buffer
    li $t1, 32                  # Set the counter $t1 to 32 bits

    # Create a loop to convert into binary form

convert_binary:

    andi $t2, $t0, 1            # Extract the least significant bit of $t0, where 1=2^1-1 (0, or 1)      
    addiu $t2, $t2, 48          # Convert the bit to ASCII character
    sb $t2, 0($a1)              # Store the ASCII character in the binary buffer
    srl $t0, $t0, 1             # Shift $t0 right by 1 bit
    addiu $a1, $a1, 1           # Move to the next position in the binary buffer
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1
    bnez $t1, convert_binary    # If $t1 is not zero, continue the loop

    la $a1, binary+32           # Load the updated address of the last character in the binary buffer
    li $t1, 32                  # Set the counter $t1 to the total number of characters that will be printed

print_zeros:

    addiu $a1, $a1, -1          # Move back one position in the binary buffer
    li $v0, 11                  # Set the system call code for printing a character
    lb $a0, 0($a1)              # Load the character from the binary buffer
    syscall                     # Print the character
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1 to check next position
    bnez $t1, print_zeros       # If $t1 is not zero, continue the loop



    # STEP 4 -- print the Quaternary form of the input
    # Print "Quaternary: "
    
    la $a0, ans3                # "load address" of the string
    li $v0, 4                   # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    
    la $a1, quaternary          # Load the address of the quaternary buffer
    move $t0, $t3               # save result in $t0 for later converting
    li $t1, 17                  # Set the counter $t1 to 17 bits

convert_Quat:

    andi $t2, $t0, 3            # Extract the least significant 2 bits of $t0, where 3=2^2-1 (0, 1, 2, 3, or 4)
    addiu $t2, $t2, 48          # Convert the bit to ASCII character
    sb $t2, 0($a1)              # Store the ASCII character in the quaternary buffer
    srl $t0, $t0, 2             # Shift $t0 right by 2 bit
    addiu $a1, $a1, 1           # Move to the next position in the quaternary buffer
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1
    bnez $t1, convert_Quat      # If $t1 is not zero, continue the loop

    la $a1, quaternary+16       # Load the updated address of the last character in the quaternary buffer
    li $t1, 17                  # Set the counter $t1 to the total number of characters that will be printed

print_qua_zeros:

    addiu $a1, $a1, -1          # Move back one position in the quaternary buffer
    li $v0, 11                  # Set the system call code for printing a character
    lb $a0, 0($a1)              # Load the character from the quaternary buffer
    syscall                     # Print the character
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1 to check next position
    bnez $t1, print_qua_zeros   # If $t1 is not zero, continue the loop

    # STEP 5 -- print the Qctal form of the input
    # Print "Octal: "

    la $a0, ans4                # "load address" of the string
    li $v0, 4                   # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    la $a1, octal               # Load the address of the quaternary buffer
    move $t0, $t4               # save result in $t0 for later converting
    li $t1, 12                  # Set the counter $t1 to 12 bits


convert_Octal:

    andi $t2, $t0, 7            # Extract the least significant 3 bits of $t0, where 7=2^3-1 (0, 1, 2, 3, 4, 5, 6, or 7)
    addiu $t2, $t2, 48          # Convert the bit to ASCII character
    sb $t2, 0($a1)              # Store the ASCII character in the octal buffer
    srl $t0, $t0, 3             # Shift $t0 right by 3 bit
    addiu $a1, $a1, 1           # Move to the next position in the octal buffer
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1
    bnez $t1, convert_Octal     # If $t1 is not zero, continue the loop


    la $a1, octal+10            # Load the updated address of the last character in the octal buffer
    li $t1, 11                  # Set the counter $t1 to the total number of characters that will be printed

print_oct_zeros:

    lb $a0, 0($a1)              # Load the byte at the address specified by $a1 into register $a0
    beqz $a0, skip_zero         # If the byte is zero (end of the octal representation), skip to skip_zero
    li $v0, 11                  # Set the system call code for printing a character
    syscall                     # Print the character

skip_zero:

    addiu $a1, $a1, -1          # Move back one position in the octal buffer
    addiu $t1, $t1, -1          # Decrease the counter $t1 by 1 to check next position
    bnez $t1, print_oct_zeros   # If $t1 is not zero, continue the loop

    # STEP 6 -- Ask to Continue

    li $v0, 4                   # "load address" of the string
    la $a0, continue_message    # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    li $v0, 5                   # syscall number 5 reads an int
    syscall                     # actually read the int              
    move $t3, $v0               # save result in $t3 for later comparing exit or continue

    beqz $t3, exit_program      # Check if the value in register $t3 is zero. If the value is zero, the program will jump to exit_program to exit
                                # If other values are entered, the code will also immediately terminate
    beq $t3, 1, main            # If value entered equals to 1, return back to main for another round of transfering

    # STEP 7-- EXIT
    # Create an exit_program to exit and print the "Bye!" message

exit_program:

    li $v0, 4                   # "load address" of the string
    la $a0, bye_message         # syscall number 4 prints string whose address is in $a0
    syscall                     # actually print the string

    
    li $v0, 10                  # Syscall number 10 is to terminate the program
    syscall                     # exit now
    
##################
# End of Program #
##################
