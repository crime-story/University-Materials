.data
matrix: .space 16000			# int v[100];
	
roles: .space 400
queue: .space 400
visited: .space 400
str: .space 100

	lines:   .word 11
	spStr:   .asciiz " "
	nLStr:	 .asciiz "\n"
	spatiu: .asciiz " "
	mesaj1: .asciiz "switch malitios index "
	mesaj2: .asciiz "controller index " 
	mesaj3: .asciiz ": "
	mesaj4: .asciiz "; "
	mesaj5: .asciiz "host index " 
	mesaj6: .asciiz "switch index "
	cer2: .asciiz "cerinta 2"
	cer3: .asciiz "cerinta 3"
	Yes: .asciiz "Yes"
	No: .asciiz "No"
	malitios1: .asciiz "\neste un malitios"
	nemalitios: .asciiz "\nnu este un malitios"

.text

main:

	#lw $t0, lines				
	
	li $v0, 5	# CITIRE $t0 va fi numarul de NODURI
	syscall
	move $t0, $v0			# $t0 este numarul de noduri

	li $v0, 5	# CITIRE $t0 va fi numarul de legaturi
	syscall
	move $t1, $v0			# $t0 este numarul de noduri
	
	li $t2, 0
	li $t6, 1
legaturi:
	bge $t2, $t1, role1
	

	li $v0, 5	# CITIRE $t0 va fi numarul de NODURI
	syscall
	
	mul $t3, $v0, $t0		# $t5 = lineIndex * columns
	
	li $v0, 5	
	syscall
	
	add $t3, $t3, $v0		# $t5 = $t5 + columnIndex
	mul $t3, $t3, 4			# $t5 = $t5 * 4

	li $t6, 1
	sw $t6,matrix($t3)

	addi $t2, $t2,1
	j legaturi

role1:
	li $t2, 0
	li $t3, 0
role:
	

	bge $t2, $t0, numarcerinta
	
	li $v0, 5	
	syscall
	sw $v0, roles($t3)			# mut din $v0 in v($t2)


	addi $t2, 1
	add $t3, $t3, 4
	j role

#AICI CITIM NUMARUL CERINTEI
numarcerinta:	
	li, $s0,1
	li, $s1,2
	li, $s2,3
	
	li $v0, 5	
	syscall
	
	beq $v0,$s0,start  #daca am cerinta 1
	beq $v0,$s1,cerinta2
	beq $v0,$s2, cerinta3
#----------------------------------------------aici incepe problema
start:

	li $t2, 0				#t2 este pentru linie
	li $t6, 0				#t6 este pentru roles
	li $t3, 3				#t7 este pentru malitios
	li $t9,1
	li $s3,0
	li $s4,1				


for_lines:
	li $t8,0
	bge $t2, $t0, et_exit  #daca $t2 este  >=$t0 atunci sare la et_exit
	

	lw $v0,roles($t6)		#stocam elementul in array  v($t2)=$v0

	beq $v0,$t3,malitios		#daca este malitios intru pe coloane
	bne $v0,$t3, cont_for_lines  	#daca nu e malitios merg la 
malitios:
	beq $s3,$s4,ENTER
	bne $s3,$s4,FARAENTER
ENTER:
	la $a0, nLStr
	li $v0, 4			# PRINT STRING
	syscall
FARAENTER:
	la $a0, mesaj1
	li $v0, 4			# PRINT STRING
	syscall

	li $s3,1

	move $a0,$t2   #incarca mesajul msg in $a0
	li $v0,1   #afiseaza mesajul
	syscall

	la $a0, mesaj3
	li $v0, 4			# PRINT STRING
	syscall
	j col

#-----------------incep pe coloane
col:
	li $t4, 0

	for_columns:
		bge $t4, $t0, cont_for_lines

		mul $t5, $t2, $t0		# $t5 = lineIndex * columns
		add $t5, $t5, $t4		# $t5 = $t5 + columnIndex
		mul $t5, $t5, 4			# $t5 = $t5 * 4

		lw $v0,matrix($t5)

		mul $t5, $t4, $t0		# $t5 = lineIndex * columns
		add $t5, $t5, $t2		# $t5 = $t5 + columnIndex
		mul $t5, $t5, 4			# $t5 = $t5 * 4

		lw $v1,matrix($t5)

		li $t1, 1
		beq $v0,$t1,afis1
		beq $v1,$t1,afis1
		#bne $v0,$t1,eti
		j eti

afis1:
	mul $t7, $t4, 4
	lw $v0,roles($t7)
	
	li $t1, 1
	beq $v0,$t1,host

	li $t1, 2
	beq $v0,$t1,switch

	li $t1, 4
	beq $v0,$t1,controler

host:
	la $a0, mesaj5
	li $v0, 4			# PRINT STRING
	syscall
	li $t8,1
	j eti1

switch:
	la $a0, mesaj6
	li $v0, 4			# PRINT STRING
	li $t8,1
	syscall
	j eti1

controler:
	la $a0, mesaj2
	li $v0, 4
	li $t8,1			# PRINT STRING
	syscall
	j eti1
eti1:
	move $a0,$t4   #incarca mesajul msg in $a0
	li $v0,1   #afiseaza mesajul
	syscall

	la $a0, mesaj4   #afiseaza ;
	li $v0, 4			# PRINT STRING
	syscall
	j eti
	
eti:
	addi $t4, 1
	j for_columns


cont_for_lines:
	
	addi $t2, 1
	addi $t6, $t6, 4
	#beq $t8, $t9, enter
	#bne $t8, $t9, for_lines
#enter:
	#la $a0, nLStr
	#li $v0, 4			# PRINT STRING
	#syscall	
	j for_lines


cerinta2:
#-------------------------
	li $s0,0
	li $t9,1   #sa fie mereu 1 initializat
	li $t1, 0 #queueLen=0
	li $t2, 0 #queueIdx=0
	li $v0,0
	sw $v0, queue($t1)  #queue($t1)=$v0

	addi $t1,1  #queueLen := queueLen + 1
	
	li $v0,1
	sw $v0, visited($t2)   #visited[0] := 1
while:
	
	beq $t2,$t1, yes
	addi $s0,1
	mul $t8,$t2,4
	lw $v0,queue($t8) #$v0=queue($t2)
	move $t3, $v0 #currentNode := queue[queueIdx], $t3=$v0
	addi $t2,1	#queueIdx := queueIdx + 1
	
	mul $t8,$t3,4
	lw $v0,roles($t8) #in $t3 retin CurrentNode
	beq $v0,$t9,print  #if roles[currentNode] == 1
	bne $v0,$t9,sar #if roles[currentNode] != 1
print:
	la $a0,mesaj5
	li $v0,4
	syscall
	move $a0,$t3  #muta  ($t3) in $a0
	li $v0,1    #afiseaza currentnode
	syscall
	la $a0,mesaj4
	li $v0,4
	syscall
	la $a0,spatiu
	li $v0,4
	syscall
	
sar:
	li $t4, 0 #columnIndex := 0
	
whilemic:
	bge $t4, $t0, while #daca columnIndex >= N sar la while
	mul $t5, $t3, $t0		# $t5 = lineIndex * columns
	add $t5, $t5, $t4		# $t5 = $t5 + columnIndex
	mul $t5, $t5, 4			# $t5 = $t5 * 4	

	lw $v0,matrix($t5)
	
	mul $t5, $t4, $t0		# $t5 = lineIndex * columns
	add $t5, $t5, $t3		# $t5 = $t5 + columnIndex
	mul $t5, $t5, 4			# $t5 = $t5 * 4	

	lw $v1,matrix($t5)
	bne $v0, $t9, next #if graph[currentNode][columnIndex] != 1
	beq $v0, $t9, next1 #if graph[currentNode][columnIndex] != 1

next:
	bne $v1, $t9, cresc #if graph[currentNode][columnIndex] != 1

next1:
	mul $t8,$t4,4
	lw $v0, visited($t8)
	beq $v0, $t9, cresc #if visited[columnIndex] == 1

	
	#lw $v0,queue($t1)
	#move $v0,$t4
	#sw $v0, queue($t1)  #queue($t1)=$v0
	mul $t8,$t1,4
	sw $t4,queue($t8)

	addi $t1,1  #queueLen := queueLen + 1
	li $v0,1
	mul $t8,$t4,4
	sw $v0, visited($t8)   #visited[0] := 1
cresc:
	addi $t4,1 #columnIndex +=1
	j whilemic

yes:
	beq $s0,$t0, DA
	bne $s0,$t0,NU

DA:	
	la $a0,nLStr
	li $v0,4
	syscall	
	la $a0,Yes
	li $v0,4
	syscall
	j et_exit
NU:
	la $a0,nLStr
	li $v0,4
	syscall
	la $a0,No
	li $v0,4
	syscall
	j et_exit
	
cerinta3:
	
#-------------------------
	li $s7,0  #pp ca nu se interpune un malitios
	li $v0, 5  #citim primul host
	syscall
	move $s5, $v0	

	li $v0, 5  #citim al doilea host
	syscall
	move $s6, $v0	

	li $v0, 8
	la $a0, str  #citim stringul
	li $a1, 100
	syscall


	li $s0,0
	li $t9,1   #sa fie mereu 1 initializat
	li $t7,3  #malitios

	li $t1, 0 #queueLen=0
	li $t2, 0 #queueIdx=0
	move $v0,$s5
	sw $v0, queue($t1)  #queue($t1)=$v0  incarc in coada primul host

	addi $t1,1  #queueLen := queueLen + 1
	
	li $v0,1
	sw $v0, visited($t2)   #visited[0] := 1
while1:
	
	beq $t2,$t1, yes1
	addi $s0,1
	mul $t8,$t2,4
	lw $v0,queue($t8) #$v0=queue($t2)
	move $t3, $v0 #currentNode := queue[queueIdx], $t3=$v0
	addi $t2,1	#queueIdx := queueIdx + 1
	
	mul $t8,$t3,4
	lw $v0,roles($t8) #in $t8 retin CurrentNode
	
	#move $a0,$t3  #muta  ($t3) in $a0
	#li $v0,1    #afiseaza NODURILE DIN PARCURGERE
	#syscall
	
	beq $t3, $s6, yes1  #daca am ajuns cu parcurgerea la hostul 2 vreau sa ies

	beq $v0,$t7,print1  #if roles[currentNode] == 3 adica malitios
	bne $v0,$t7,sar1 #if roles[currentNode] != 3  adica nu e malitios



print1:
	li $s7,1   #s-a interpus un malitios
	
sar1:
	li $t4, 0 #columnIndex := 0
	
whilemic1:
	bge $t4, $t0, while1 #daca columnIndex >= N sar la while
	mul $t5, $t3, $t0		# $t5 = lineIndex * columns
	add $t5, $t5, $t4		# $t5 = $t5 + columnIndex
	mul $t5, $t5, 4			# $t5 = $t5 * 4	

	lw $v0,matrix($t5)
	
	mul $t5, $t4, $t0		# $t5 = lineIndex * columns
	add $t5, $t5, $t3		# $t5 = $t5 + columnIndex
	mul $t5, $t5, 4			# $t5 = $t5 * 4	

	lw $v1,matrix($t5)
	bne $v0, $t9, next2 #if graph[currentNode][columnIndex] != 1
	beq $v0, $t9, next3 #if graph[currentNode][columnIndex] != 1

next2:
	bne $v1, $t9, cresc1 #if graph[currentNode][columnIndex] != 1

next3:
	mul $t8,$t4,4
	lw $v0, visited($t8)
	beq $v0, $t9, cresc1 #if visited[columnIndex] == 1

	
	#lw $v0,queue($t1)
	#move $v0,$t4
	#sw $v0, queue($t1)  #queue($t1)=$v0
	mul $t8,$t1,4
	sw $t4,queue($t8)

	addi $t1,1  #queueLen := queueLen + 1
	li $v0,1
	mul $t8,$t4,4
	sw $v0, visited($t8)   #visited[0] := 1
cresc1:
	addi $t4,1 #columnIndex +=1
	j whilemic1

yes1:
	beq $s7,$t9, DA1   #daca s-a interpus un malitios
	bne $s0,$t0,NU1

DA1:	
	#------------------------------------

	li $t0, 0 		# contorul care imi sare locatii de memorie din 1 in 1
for:
	lb $t1, str($t0)	# $t1 este caracterul curent din sirul de caractere
				# in C ar fi un str[i]
	beq $t1, $0, NU1	# daca am ajuns pe '\0' = Terminatorul de sir, merg la exit
				# beqz $t1, exit

	addi $t1, -10		# byte-ul $t1 este incrementat cu 1
				# astfel ca se va transforma in caracterul imedia urmator
				# de exemplu, S -> T
				#	      t -> u etc.

	sb $t1, str($t0)	# asta inseamna ca str($t0) devine acum noul $t1		

	addi $t0, 1		# incrementam $t0 pentru a sari la urmatoarea locatie din memorie
	j for

	#----------------------------------------------
	
	j et_exit
NU1:
	la $a0, str
	li $v0, 4
	syscall

	
	j et_exit

et_exit:
	# PRINT STRING
	li $v0, 10
	syscall
