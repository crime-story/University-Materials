% Crossword puzzle

word(abalone,a,b,a,l,o,n,e).
word(abandon,a,b,a,n,d,o,n).
word(enhance,e,n,h,a,n,c,e).
word(anagram,a,n,a,g,r,a,m).
word(connect,c,o,n,n,e,c,t).
word(elegant,e,l,e,g,a,n,t).

% Ex1
crossword(V1,V2,V3,H1,H2,H3):- word(V1, _, V12, _, V14, _, V16, _),
                               word(V2, _, V22, _, V24, _, V26, _),
                               word(V3, _, V32, _, V34, _, V36, _),
                               word(H1, _, V12, _, V22, _, V32, _),
                               word(H2, _, V14, _, V24, _, V34, _),
                               word(H3, _, V16, _, V26, _, V36, _).

% Ex2
% a)
born(jan, date(20,3,1977)).
born(jeroen, date(2,2,1992)).
born(joris, date(17,3,1995)).
born(jelle, date(1,1,2004)).
born(joan, date(24,12,0)).
born(joop, date(30,4,1989)).
born(jannecke, date(17,3,1993)).
born(jaap, date(16,11,1995)).

year(Year, Person) :- born(Person,date(_,_,Year)).
                                     
                                     
% b)
before(date(X, Y, Z), date(X1, Y1, Z1)) :-
      Z1 > Z; Z1 = Z, Y1 > Y; Y1 = Y, Z1 = Z, X1 > X.

% c)
older(Person, X) :- 
    born(Person, D1),
    born(X, D2),
    before(D1,D2).
                                     
                                     
% Ex3
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
                      
path(A,B) :-  
  walk(A,B,[]).
walk(A,B,V) :-
  connected(A,X),
  not(member(X,V)),
  (B = X; walk(X,B,[A|V])).

% Ex4
% a)
successor([], [x]).
successor([Elem | List], [Elem | Result]) :- successor(List, Result).

% b)
plus([], List, List).
plus([Head|Tail], List, [Head|Rest]) :-
    plus(Tail, List, Rest).

% varianta2
pluss([x], [], [x]).
pluss([], [x], [x]).
pluss([Elem | List1], List2, [Elem | Result]) :- 
    pluss(List1, List2, Result).
pluss(List1, [Elem | List2], [Elem | Result]) :- 
    pluss(List1, List2, Result).

% c)
times([], _, []).
times([x | List1], List2, Result) :- 
    times(List1, List2, TimesList2),
    pluss(List2, TimesList2, Result).

% Ex5
element_at([X|_], 1, X) .
element_at([_|Xs], N , X) :-
  N > 0,
  N1 is N-1,
  element_at(Xs, N1, X).

% Ex6
/* Animal  database */
animal(alligator). 
animal(tortue).
animal(caribou).
animal (ours).
animal(cheval).
animal(vache).
animal(lapin).


check([], _).
check(_, []):- false.
check([X1 | Tail1], [X2 | Tail2]):- 
    X1 = X2, check(Tail1, Tail2).

longestCommon([], _, _, _):- false.
longestCommon([Head1 | Tail1], List2, Length, Res):- 
    check([Head1 | Tail1], List2), Res is Length; (NewLength is Length - 1, longestCommon(Tail1, List2, NewLength, Result), Res is Result).

skip(List, 0, List).
skip([_ | Tail], Count, Result):- 
    NewCount is Count - 1, skip(Tail, NewCount, Result).

mutant(X):- 
    animal(A1), animal(A2), 
    name(A1, X1), name(A2, X2), 
    X1 \= X2, length(X1, Length), 
    longestCommon(X1, X2, Length, Common), 
    skip(X2, Common, List2), append(X1, List2, Name), 
    name(X, Name).