.data
    x:.word 3
    y:.word 6
.text

suma:

    subu $sp, 4
    sw $fp, 0($sp)

    addi $fp, $sp, 4

    subu $sp, 4
    sw $s0, 0($sp)

    subu $sp, 4
    sw $s1, 0($sp)

    lw $s0, 0($fp)
    lw $s1, 4($fp)

    add $s0, $s0, $s1 # acum in loc sa salvez rezultatul in $v0
                      # il salvez tot in $s0
    
    sw $s0, 0($fp)    # pun valoarea returnata in capatul stivei
                      # deci stiva mea de acum se va transforma
                      # din (s1), (s0), (fp), (x), (y)
                      # in (s1), (s0), (fp), (rezultat), (y)
                      # iar dupa ce vom scoate (s1), (s0) si (fp)
                      # vor ramane doar (rezultat), (y)

    lw $s1, -12 ($fp)
    lw $s0, -8 ($fp)
    lw $fp, -4 ($fp)

    addu $sp, 12      
                      # acum stiva noastra este (rezultat) (y)
    
    jr $ra

main:

    lw $t0, y 
    subu $sp, 4
    sw $t0, 0($sp)

    lw $t0, x
    subu $sp, 4
    sw $t0, 0($sp)

    jal suma

    lw $t0, 0($sp)    # luam rezultatul din varful stivei
    addu $sp, 8       # dam pop si rezultatului, si lui y de pe stiva

    move $a0, $t0     # afisam pe ecran rezultatul
    li $v0, 1
    syscall

    li $v0, 10
    syscall