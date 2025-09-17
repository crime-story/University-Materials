.data

    x:.float 20.6
    y:.float 11.5
    
.text
main:

    l.s $f0, x
    l.s $f2, y
    
    s.s $f0, y
    s.s $f2, x

    li $v0, 2
    mov.s $f12, $f2
    syscall

    li $v0, 11
    li $a0, ' '
    syscall

    li $v0, 2
    mov.s $f12, $f0
    syscall

    li $v0, 10
    syscall