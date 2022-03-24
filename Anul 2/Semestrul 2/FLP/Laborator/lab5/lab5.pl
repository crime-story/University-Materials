% Ex1
num_aparitii([],_,0).
num_aparitii([X|T],X,Y):- num_aparitii(T,X,Z), Y is 1+Z.
num_aparitii([X1|T],X,Z):- X1\=X,num_aparitii(T,X,Z).

% Ex2
lista_cifre(N, [N]) :- N < 10, !.
lista_cifre(N, T1) :- %N >= 10, 
    N1 is floor(N/10),
    A is N mod 10,
    lista_cifre(N1, T),
    append(T,[A], T1).

% Ex3
listpermcirc(L, C):-
  append(Head, [First|Tail], L),
  append([First|Tail], Head, C).

% Ex4
% a)
elimina(L, _, R):- [] = L, [] = R.
elimina(L, X, R):- [Y|K] = L, [Y|M] = R, elimina(K, X, M), (Y \= X).
elimina(L, X, R):- [X|K] = L, [Y|_] = R, elimina(K, X, R), (Y \= X).

elimina2(List, Elem, Result) :- delete(List, Elem, Result).

% b)
multime([], []).
multime([Elem | List], [Elem | Result]) :- elimina2(List, Elem, ListWithoutElem),
    							  		   multime(ListWithoutElem, Result).

% c)
emult(List) :- multime(List, ListToSet),
    		   List = ListToSet.

% Ex5
inters([], _, []).
inters([Elem | List1], List2, [Elem | Result]) :- 
    member(Elem, List2),
    inters(List1, List2, Result), !.

inters([_ | List1], List2, Result) :- 
    inters(List1, List2, Result).

diff(R, [], R) :- !.
diff(List1, [Elem | List2], Result) :- elimina(List1, Elem, NewList1),
    								   diff(NewList1, List2, Result).

prod_cartezian([A|_], Bs, [A, B]) :- member(B, Bs).
prod_cartezian([_|As], Bs, X) :- prod_cartezian(As, Bs, X).

% Ex6
% a)
srd(nil, []).
srd(arb(R, St, Dr), L) :- srd(St, L1), srd(Dr, L2), append(L1, [R|L2], L).

rsd(nil, []).
rsd(arb(R, St, Dr), L) :- rsd(Dr, L1), rsd(St, L2), append([R|L2], L1, L).

sdr(nil, []).
sdr(arb(R, St, Dr), L) :- sdr(St, L1), sdr(Dr,L2), append(L1, L2, L3), append(L3, [R], L).


% b)
frunze(nil, []).
frunze(arb(X, nil, nil), [X]) :- !.
frunze(arb(_, St, Dr), L) :-
    frunze(St, L1),
    frunze(Dr, L2),
    append(L1, L2, L).
    %L \= [].