.data
.text
main:

    li $t0, 0         # -5
    li $t1, 0         # -4

    li $a0, 100       # -3
    li $v0, 1         # -2

    beq $t0, $t1, et  # -1
    et: 
    syscall           # 0

    li $v0, 10        # 1
    syscall           # 2
