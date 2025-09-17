.data

    a:.byte 'p'

.text
main:

    li $v0, 11
    lb $a0, a
    syscall

    li $v0, 10
    syscall