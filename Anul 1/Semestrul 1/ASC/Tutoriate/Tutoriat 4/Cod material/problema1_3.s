.data
.text
main:

    li $v0, 6        
    syscall

    cvt.w.s $f2, $f0 # convertim pe $f0 (citit) in word, si tinem resultatul in $f2
                     # noi am vrea sa-l tinem in $t0 pentru ca este word, dar compilerul nu ne lasa
                     # asa ca trebuie sa punem un registru pentru float-uri ca destinatie
    mfc1 $t0, $f2    # punem rezultatul din $f2 in $t0 (in $t0, este reprezentat intern ca un
                     # word, dar este in registru de float, deci il copiem intr-un registru
                     # pentru word-uri)

    li $v0, 1        # afisam pe ecran rezultatul
    move $a0, $t0
    syscall

    li $v0, 10
    syscall