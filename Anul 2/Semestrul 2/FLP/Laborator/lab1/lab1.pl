%Exercitii
%Ex1
%1. f, 'Hello' sunt atomi
%2. A, Paul, _, _abc sunt variabile



%Ex2
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


father_of(Father, Child) :- parent(Father, Child), male(Father).
mother_of(Mother, Child) :- parent(Mother, Child), female(Mother).
grandfather_of(Grandfather, Child) :- parent(Grandfather, X), parent(X, Child), male(Grandfather).
grandmother_of(Grandmother, Child) :- mother_of(Grandmother, X), parent(X, Child).
sister_of(Sister,Person) :- parent(X, Sister), parent(Person, X), female(Sister).
brother_of(Brother,Person) :- parent(X, Brother), parent(Person, X), male(Brother).
aunt_of(Aunt,Person) :- parent(X, Person), sister_of(Aunt, X).
uncle_of(Uncle,Person) :- parent(X, Person), brother_of(X, Uncle).


%Ex3
parent_of(X, Y) :- father_of(X, Y); mother_of(X, Y).


not_parent(X,Y) :- (male(X); female(X)), (male(Y); female(Y)), not(parent_of(X,Y)), X \= Y.


%Ex4
distance((Xa, Ya), (Xb, Yb), X) :- X is sqrt((Xb-Xa)^2 + (Yb-Ya)^2)