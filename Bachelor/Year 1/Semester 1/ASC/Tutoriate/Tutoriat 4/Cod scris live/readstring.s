.data

    sir:.space 100

.text

main:

    la $a0, sir
    li $a1, 99
    li $v0, 8
    syscall

    li $v0, 4
    la $a0, sir
    syscall

    li $v0, 10
    syscall