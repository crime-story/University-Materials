.data

    sir:.asciiz "Va salut, dragi studenti." # sirul declarat in memorie

.text

invert:

    subu $sp, 4                             # punem $fp pe stiva    
    sw $fp, 0($sp)                          # acum stiva este $sp: ($fp) (adresa_sir) (lungime)

    addi $fp, $sp, 4                        # face $fp sa pointeeze catre cadrul nostru de apel
                                            # avem $sp: ($fp) <fp_pointeaza_aici> (adresa_sir) (lungime)

    subu $sp, 4                             # pune $ra pe stiva
    sw $ra, 0($sp)                          # stiva este: $sp: ($ra) ($fp) (adresa_sir) (lungime)

    subu $sp, 4                             # pune $s0 pe stiva
    sw $s0, 0($sp)                          # stiva este: $sp: ($s0) ($ra) ($fp) (adresa_sir) (lungime)

    subu $sp, 4                             # pune $s1 pe stiva
    sw $s1, 0($sp)                          # stiva este: $sp: ($s1) ($s0) ($ra) ($fp) (adresa_sir) (lungime)

    lw $s0, 0($fp)                          # in $s0 avem adresa catre inceputul sirului de caractere
    lw $s1, 4($fp)                          # in $s1 avem momentan lungimea sirului de caractere

    add $s1, $s0, $s1                       # adunam la $s1 valoarea lui $s0 pentru a avea in el un pointer
                                            # catre sfarsitul sirului de caractere
    subu $s1, 1                             # academ 1 deoarece prin adunarea de mai sus ajungem la caracterul \0
                                            # pe care nu trebuie sa il luam in considerare in algoritmul de inversare

    inv_loop:                               # loop-ul care inverseaza

        bge $s0, $s1, sf_inv                # daca $s0>=$s1 se opreste, adica se executa cat timp $s0<$s1
                                            # asa cum scrie in enunt

        lb $t0, 0($s0)                      # folosim $t0 si $t1 ca registrii auxiliari pentru interschimbare
        lb $t1, 0($s1)

        sb $t0, 0($s1)                      # interschimbam valorile
        sb $t1, 0($s0)

        addu $s0, 1                         # incrementam registrul $s0 (pentru ca el merge de la inceput spre sfarsit)
        subu $s1, 1                         # decrementam registrul $s1 (pentru ca el merge de la sfarsit spre inceput)

        j inv_loop                          # continua algoritmul de inversare

sf_inv:

    lw $s1, -16($fp)                        # restaureaza registrii care au fost pusi pe stiva
    lw $s0, -12($fp)                        # adica ($s1, $s0, $ra, $fp)
    lw $ra, -8($fp)
    lw $fp, -4($fp)

    addu $sp, 16                            # da pop la acesti registrii de pe stiva

    j $ra                                   # procedura s-a terminat

main:

    li $t0, 0                               # vom afla lungimea sirului in registrul $t0
    
    lb $t1, sir($t0)                        # caracterul curent din parcurgerea pentru aflarea
                                            # lungimii

    calc_lung:                              # afla lungimea sirului

        beqz $t1, sf_calc_lung              # daca am ajuns la \0 ne oprim

        addu $t0, 1                         # incrementeaza contorul
        lb $t1, sir($t0)                    # ia un caracter din sir

        j calc_lung                         # continua parcurgerea

sf_calc_lung:

    subu $sp, 4                             # pune lungimea sirului pe stiva
    sw $t0, 0($sp)

    la $t1, sir                             # pun adresa sirului de caractere pe stiva
    subu $sp, 4
    sw $t1, 0($sp)
                                            # stiva este: $sp: (adresa_sir) (lungime)
    jal invert                              # apeleaza procedura care inverseaza sirul

    addu $sp, 8                             # da pop la argumentele de pe stiva

    la $a0, sir                             # afiseaza sirul cu syscall cum cere problema
    li $v0, 4
    syscall

    li $v0, 10
    syscall