parent(dean,bob).
parent(jane,bob).
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

ancestor_of(X,Y) :- parent(X,Y).
ancestor_of(X,Y) :- parent(X,Z), ancestor_of(Z,Y).

%fib(0,1).
%fib(1,1).
%fib(N,X) :- N > 1, X is fib(N-1,X) + fib(N-1,X).

%Ex1
fib(0,1).
fib(1,1).
fib(N, X) :- N > 1, 
    N1 is N-1, N2 is N-2, 
    fib(N1, X1), fib(N2, X2), 
    X is X1 + X2.

%Ex1 cont
fib2(0, 1, 1).
fib2(1, 1, 1).
fib2(N, Xn, Xn1) :- N > 1,
    N1 is N-1, 
    fib2(N1, Xn1, Xn2),
    Xn is Xn1 + Xn2.

%Ex2
square(X,Symbol):-
  square1(X,X,Symbol).

square1(0,_,_).      
square1(X,Y,Symbol):-
  X1 is X - 1,
  square2(Y,Symbol), 
  nl,
  square1(X1,Y,Symbol).

square2(0,_).    
square2(X,Symbol):-
  X1 is X - 1,
  write(Symbol),
  square2(X1,Symbol).

%Ex3 a)
all_a([]).
all_a([X|Y]):-help(Y,X).

help([],_).
help([Y|X],Y) :- help(X,Y).

%b)
trans_a_b([], []).
trans_a_b([a|La], [b|Lb]) :-
    trans_a_b(La, Lb).

%Ex4 a)
scalarMult(_,[],[]).
scalarMult(N,[Head1|Tail1],[Head2|Tail2]) :-
              Head2 is N * Head1,
              scalarMult(N,Tail1,Tail2).
    
%b)
dot([], [], 0).
dot([Head1|Tail1], [Head2|Tail2], Result) :- 
  Prod is Head1 * Head2,
  dot(Tail1, Tail2, Remaining),
  Result is Prod + Remaining.

%Ex5
max([R], R).
max([X|Xs], R):- 
    max(Xs, T), 
    (X > T -> R = X ; R = T).

%Ex6
palindrome([]).
palindrome([_]).
palindrome(Pal) :-
    append([H|T], [H], Pal),
    palindrome(T).

%Ex7
remove_duplicates([], []).
remove_duplicates([Head | Tail], Result) :-
    member(Head, Tail), !,
    remove_duplicates(Tail, Result).
remove_duplicates([Head | Tail], [Head | Result]) :-
    remove_duplicates(Tail, Result).

%Ex8
replace(_, _, [], []).
replace(O, R, [O|T], [R|T2]) :- replace(O, R, T, T2).
replace(O, R, [H|T], [H|T2]) :- dif(H,O), replace(O, R, T, T2).