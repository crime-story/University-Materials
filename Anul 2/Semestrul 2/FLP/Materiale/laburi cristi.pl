% lab1

% 2
%-- baza de cunostinte
female(mary).
female(sandra).
female(juliet).
female(lisa).
male(peter).
male(paul).
male(dony).
male(bob).
male(harry).
parent(bob, lisa).
parent(bob, paul).
parent(bob, mary).
parent(juliet, lisa).
parent(juliet, paul).
parent(juliet, mary).
parent(peter, harry).
parent(lisa, harry).
parent(mary, dony).
parent(mary, sandra).
%--

father_of(Father, Child):- parent(Father, Child), male(Father).
mother_of(Mother, Child):- parent(Mother, Child), female(Mother).

grandfather_of(Grandfather, Child):- male(Grandfather), parent(Grandfather, Parent), parent(Parent, Child).
grandmother_of(Grandmother, Child):- female(Grandmother), parent(Grandmother, Parent), parent(Parent, Child).

sister_of(Sister, Person):- female(Sister), parent(Parent, Sister), female(Parent), parent(Parent, Person), Sister \= Person.
brother_of(Brother, Person):- male(Brother), parent(Parent, Brother), parent(Parent, Person), Brother \= Person.

aunt_of(Aunt,Person):- parent(Parent, Person), sister_of(Parent, Aunt).
uncle_of(Uncle,Person):- parent(Parent, Person), brother_of(Parent, Uncle).


%3
not_parent(X,Y):- not(parent(X,Y)), male(X); female(X), male(Y); female(Y).

%4

distance((X1, Y1), (X2, Y2), X):- X is sqrt((X1 - X2)*(X1 - X2) + (Y1 - Y2)*(Y1 - Y2)).



%% lab 2
ancestor_of(X,Y) :- parent(X,Y).
ancestor_of(X,Y) :- parent(X,Z), ancestor_of(Z,Y).

% 1

fib(0, 1).
fib(1, 1).
fib(X, Y):- X >= 2, X1 is X - 1, X2 is X - 2, fib(X1, Y1), fib(X2, Y2), Y is Y1 + Y2.

% 2

printCh(0, _):- write('\n').
printCh(N, Char):- N > 0, write(Char), N1 is N - 1, printCh(N1, Char).

printLines(0, _).
printLines(N, Nr, Char):- N > 0, printCh(Nr, Char), N2 is N - 1, printLines(N2, Nr, Char).

square(N, Char):- printLines(N, N, Char).

% 3
% a
all_a([]).
all_a([a | Tail]):- all_a(Tail).

%b
translate([], []).
translate([a | Tail1], [b | Tail2]):- translate(Tail1, Tail2).
trans_a_b(L1, L2):- all_a(L1), translate(L1, L2).

%4 
% a
scalarMult(_, [], []).
scalarMult(C, [Head | Tail], [Res | Tail2]):- Res is C * Head, scalarMult(C, Tail, Tail2).

% b
dot([], [], 0).
dot([Head1 | Tail1], [Head2 | Tail2], Result):- dot(Tail1, Tail2, Result2), Result is Head1 * Head2 + Result2.

%5

max([], -1).
max([Head | Tail], Result):- max(Tail, Result2), (Head > Result2 -> Result is Head; Result is Result2).

%6

revers([], []).
revers([Head | Tail], Result):- revers(Tail, Res), append(Res, [Head], Result).

equals([], []).
equals([X | Tail1], [X | Tail2]):- equals(Tail1, Tail2).

palindrome(List):- revers(List, RevList), equals(List, RevList).

%7

remove_duplicates([], []).
remove_duplicates([Head | Tail], Result):- remove_duplicates(Tail, Res), (member(Head, Res) -> Result = Res; Result = [Head | Res]).

%8

replace([], _, _,[]).
replace([Head | Tail1], Elem1, Elem2, [Res | Tail2]):- (Head == Elem1, Res = Elem2; Res = Head), replace(Tail1, Elem1, Elem2, Tail2).


%% Lab 3

%1

word(abalone,a,b,a,l,o,n,e).
word(abandon,a,b,a,n,d,o,n).
word(enhance,e,n,h,a,n,c,e).
word(anagram,a,n,a,g,r,a,m).
word(connect,c,o,n,n,e,c,t).
word(elegant,e,l,e,g,a,n,t).

crosswd(V1, V2, V3, H1, H2, H3):-
word(V1, _, L22, _, L42, _, L62, _),
word(V2, _, L24, _, L44, _, L64, _),
word(V3, _, L26, _, L46, _, L66, _),
word(H1, _, L22, _, L24, _, L26, _),
word(H2, _, L42, _, L44, _, L46, _),
word(H3, _, L62, _, L64, _, L66, _).


%2
born(jan, date(20,3,1977)).
born(jeroen, date(2,2,1992)).
born(joris, date(17,3,1995)).
born(jelle, date(1,1,2004)).
born(joan, date(24,12,0)).
born(joop, date(30,4,1989)).
born(jannecke, date(17,3,1993)).
born(jaap, date(16,11,1995)).

%a

year(Year, Person):- born(Person, date(_, _, Year2)), Year2 == Year. 

%b

before(date(D1, M1, Y1), date(D2, M2, Y2)):- Y1 < Y2; (Y1 == Y2, (M1 < M2; (M1 == M2, D1 < D2))).

%c

older(Person1, Person2):- born(Person1, Date1), born(Person2, Date2), before(Date1, Date2).


%3

% Maze
connected(1,2).
connected(3,4).
connected(5,6).
connected(7,8).
connected(9,10).
connected(12,13).
connected(13,14).
connected(15,16).
connected(17,18).
connected(19,20).
connected(4,1).
connected(6,3).
connected(4,7).
connected(6,11).
connected(14,9).
connected(11,15).
connected(16,12).
connected(14,17).
connected(16,19).

pathAux(N1, N2, _):- connected(N1, N2).
pathAux(N1, N2, List):- connected(N1, X), not(member(X, List)), pathAux(X, N2, [N1 | List]).

path(N1, N2):- pathAux(N1, N2, []).

%4
% a

successor(List, [x | List]).

% b
plus(N1, N2, Res):- append(N1, N2, Res).

%c

times([], _, []).
times([_ | Tail], List2, Result):- times(Tail, List2, Res), append(List2, Res, Result).

%5

element_at_aux([Head | _], Pos, Head, Pos).
element_at_aux([_ | Tail], Pos, X, Nr):- Nr2 is Nr + 1, element_at_aux(Tail, Pos, X, Nr2).

element_at(List, Pos, X):- element_at_aux(List, Pos, X, 1).

%6

check([], _).
check(_, []):- false.
check([X1 | Tail1], [X2 | Tail2]):- X1 = X2, check(Tail1, Tail2).

longestCommon([], _, _, _):- false.
longestCommon([Head1 | Tail1], List2, Length, Res):- check([Head1 | Tail1], List2), Res is Length; (NewLength is Length - 1, longestCommon(Tail1, List2, NewLength, Result), Res is Result).

skip(List, 0, List).
skip([_ | Tail], Count, Result):- NewCount is Count - 1, skip(Tail, NewCount, Result).

mutant(X):- animal(A1), animal(A2), name(A1, X1), name(A2, X2), X1 \= X2, length(X1, Length), longestCommon(X1, X2, Length, Common), skip(X2, Common, List2), append(X1, List2, Name), name(X, Name).

%lab 4

%1
la_dreapta(X, Y):- X is Y + 1.
la_stanga(X, Y):- X is Y - 1.
langa(X, Y):- la_dreapta(X, Y); la_stanga(X, Y).

solutie(Strada,PosesorZebra) :-
    Strada = [
        casa(1,_,_,_,_,_),
        casa(2,_,_,_,_,_),
        casa(3,_,_,_,_,_),
        casa(4,_,_,_,_,_),
        casa(5,_,_,_,_,_)],
        member(casa(_,englez,rosie,_,_,_), Strada),
        member(casa(_,spaniol,_,caine,_,_), Strada),
        member(casa(_,_,verde,_,cafea,_), Strada),
        member(casa(_,ucrainean,_,_,ceai,_), Strada),
        member(casa(NrVerde,_,verde,_,_,_), Strada),
        member(casa(NrBej,_,bej,_,_,_), Strada),
        la_dreapta(NrVerde, NrBej),
        member(casa(_,_,_,melci,_,"Old Gold"), Strada),
        member(casa(_,_,galbena,_,_,"Kools"), Strada),
        member(casa(3,_,_,_,lapte,_), Strada),
        member(casa(1,norvegian,_,_,_,_), Strada),
        member(casa(_,_,_,melci,_,"Old Gold"), Strada),
        member(casa(NrChe,_,_,_,_,"Chesterfield"), Strada),
        member(casa(NrVul,_,_,vulpe,_,_), Strada),
        langa(NrChe, NrVul),
        member(casa(NrKoo,_,_,_,_,"Kools"), Strada),
        member(casa(NrCal,_,_,cal,_,_), Strada),
        langa(NrKoo, NrCal),
        member(casa(_,_,_,_,"suc de portocale","Lucky Strike"), Strada),
        member(casa(_,japonez,_,_,_,"Parliaments"), Strada),
        member(casa(NrNor,norvegian,_,_,_,_), Strada),
        member(casa(NrAlbastru,_,albastra,_,_,_), Strada),
        langa(NrNor, NrAlbastru),
        member(casa(_,PosesorZebra,_,zebra,_,_), Strada).

%2

:- [words].

lookup(_, [], _):- false.
lookup(X, [Y | Tail], Res):- (X = Y) -> (Res = Tail); (lookup(X, Tail, Result), Res = [Y | Result]).

cover([], _).
cover(_, []):- false.
cover([X | Tail], X2):- lookup(X, X2, Res), cover(Tail, Res).

%2 c
solution(List, Word, Length):- word(Word), word_letters(Word,Letters), length(Letters, Length), cover(Letters, List).

%2 d
topsolutionAux(List, Res, Length):- solution(List, Res, Length) -> true; (NewLength is Length - 1, topsolutionAux(List, Res, NewLength)).
topsolution(List, Res):- length(List, Length), topsolutionAux(List, Res, Length).

%lab 5

%1

num_aparitii([], _, 0).
num_aparitii([Head | Tail], Elem, Result):- num_aparitii(Tail, Elem, Res), (Head == Elem -> Result is Res + 1; Result is Res).

%2

lista_cifre(0, []).
lista_cifre(X, Result):- X > 0, N1 is X // 10, N2 is X mod 10, lista_cifre(N1, Res), append(Res, [N2], Result).

%3

permcirc(_, 0, []).
permcirc([Head | Tail], N, [Perm | Result]):- N > 0, append(Tail, [Head], Perm), NewN is N - 1, permcirc(Perm, NewN, Result).

listpermcirc(List, L):- length(List, Len), permcirc(List, Len, L).

%4

%a

elimina([], _, []).
elimina([Head | Tail], Elem, Result):- elimina(Tail, Elem, Res), (Head == Elem -> Result = Res; Result = [Head | Res]).

%b

multime([], []).
multime([Head | Tail], Result):- multime(Tail, Res), (member(Head, Res) -> Result = Res; Result = [Head | Res]).

%c

emult(List):- multime(List, List).

%5

%a

inters([], _, []).
inters([Head | Tail], List2, Result):- emult([Head | Tail]), emult(List2), inters(Tail, List2, Res), (member(Head, List2) -> Result = [Head | Res]; Result = Res).

%b

diff([], _, []).
diff([Head | Tail], List2, Result):- emult([Head | Tail]), emult(List2), diff(Tail, List2, Res), (not(member(Head, List2)) -> Result = [Head | Res]; Result = Res).

%c

prod_cart_aux(_, [], []).
prod_cart_aux(Elem, [Head | Tail], [(Elem, Head) | Result]):- prod_cart_aux(Elem, Tail, Result).

prod_cartezian([], _, []).
prod_cartezian([Head | Tail], List2, [Res | Result]):- prod_cartezian(Tail, List2, Result), prod_cart_aux(Head, List2, Res).

%6

%a

srd(nil, []).
srd(arb(Elem, ArbSt, ArbDr), L):- srd(ArbSt, Res), append(Res, [Elem], TempRes), srd(ArbDr, Res2), append(TempRes, Res2, L). 

rsd(nil, []).
rsd(arb(Elem, ArbSt, ArbDr), L):- rsd(ArbSt, Res), append([Elem], Res, TempRes), rsd(ArbDr, Res2), append(TempRes, Res2, L). 

sdr(nil, []).
sdr(arb(Elem, ArbSt, ArbDr), L):- sdr(ArbSt, Res), sdr(ArbDr, Res2), append(Res, Res2, Aux), append(Aux, [Elem], L).

frunze(nil, []).
frunze(arb(Elem, nil, nil), [Elem]).
frunze(arb(_, ArbSt, ArbDr), Res):- frunze(ArbSt, Res1), frunze(ArbDr, Res2), append(Res1, Res2, Res).

%prob din poza 1

p1_aux([], 0, _).
p1_aux([Head | Tail], Sum, N):- NewN is N + 1, p1_aux(Tail, SumInt, NewN), (N mod 2 =:= 0 -> Sum is Head + SumInt; Sum is SumInt).

p1(List, Sum):- p1_aux(List, Sum, 1).

p2([], _, []).
p2([Head | Tail], Elem, Result):- p2(Tail, Elem, Res), (Head mod 2 =:= 0 -> Result = [Head | Res]; Result = [Elem | Res]).

%model restanta 2021

%1
persoane([], _, []).
persoane([are(Nume, Obiecte) | Tail], Obiect, Persoane):- persoane(Tail, Obiect, Res), (member(Obiect, Obiecte) -> Persoane = [Nume | Res]; Persoane = Res).

%2


inmulteste(_, [], []).
inmulteste(Cifra, [Head | Tail], [Elem | Res]):- inmulteste(Cifra, Tail, Res), Elem is Head * 10 + Cifra.

numereAux(0, [0]).
numereAux(N, List):- NewN is N - 1, numereAux(NewN, Res), inmulteste(1, Res, List1), inmulteste(2, Res, List2), inmulteste(3, Res, List3), append(List1, List2, Aux), append(Aux, List3, List).

numere(N, List):-numereAux(N, List).
