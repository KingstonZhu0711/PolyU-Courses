.data
prompt: .asciiz "Enter a number (32 to 126): "
ans1: .asciiz "\n Number: "
ans2: .asciiz "\n Binary: "
letter: .word 0
buffer: .space 33   # 32 bits + null terminator
.text
.globl main

# Function to convert a number to binary
# Input: $a0 - Number to convert
#        $a1 - Buffer to store binary representation
binary_conversion:
    addiu $sp, $sp, -4       # Save return address
    sw $ra, 0($sp)

    addiu $t0, $zero, 32     # Number of bits to convert
    addiu $t1, $zero, 0      # Index for buffer
    addiu $t2, $zero, 0      # Remainder

binary_conversion_loop:
    div $a0, $zero, 2        # Divide by 2
    mfhi $t2                 # Get remainder

    addiu $t1, $t1, -1       # Decrement index
    addiu $t2, $t2, 48       # Convert remainder to ASCII

    sb $t2, ($a1)            # Store remainder in buffer

    beqz $a0, binary_conversion_end   # Exit loop if number is 0

    move $a0, $a0            # Move quotient to $a0
    move $a1, $a1            # Move buffer address to $a1

    j binary_conversion_loop

binary_conversion_end:
    addiu $t1, $t1, -1       # Decrement index
    addiu $t2, $t2, 48       # Convert remainder to ASCII

    sb $t2, ($a1)            # Store final remainder in buffer
    sb $zero, 1($a1)         # Null terminate the buffer

    lw $ra, 0($sp)           # Restore return address
    addiu $sp, $sp, 4

    jr $ra                   # Return from the function

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
    sw $t0, 0($t1)  # Corrected line

    la $a0, letter
    li $v0, 1
    syscall

    la $a0, buffer          # Set buffer address
    move $a1, $t1           # Move number to convert to $a0
    jal binary_conversion   # Call binary_conversion function

    la $a0, buffer          # Set buffer address
    li $v0, 4               # Set system call code for printing string
    syscall

    li $v0, 10              # Exit program
    syscall