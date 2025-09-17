solutie(Strada,PosesorZebra) :-
Strada = [
    casa(1,_,_,_,_,_),
    casa(2,_,_,_,_,_),
    casa(3,_,_,_,_,_),
    casa(4,_,_,_,_,_),
    casa(5,_,_,_,_,_)],
  member(casa(_,englez,rosie,_,_,_), Strada),
  member(casa(_,spaniol,_,caine,_,_), Strada),
  member(casa(Verde,_,verde,_,cafea,_), Strada),
  member(casa(Bej,_,bej,_,_,_), Strada), laDreapta(Verde,Bej),
  member(casa(_,ucrainean,_,_,ceai,_), Strada),
  member(casa(_,_,_,melci,_,oldgold), Strada),
  member(casa(_,_,verde,_,_,kools), Strada), 
  member(casa(3,_,_,_,lapte,_), Strada),
  member(casa(1,norvegian,_,_,_,_), Strada), 
  member(casa(Chesterfields,_,_,_,_,chesterfields), Strada),
  member(casa(Vulpe,_,_,vulpe,_,_), Strada),
  langa(Chesterfields,Vulpe),
  member(casa(Kools,_,_,_,_,kools), Strada),
  member(casa(Cal,_,_,cal,_,_), Strada),
  langa(Kools, Cal),
  member(casa(_,_,_,_,sucdeportocale,luckyStrike), Strada),
  member(casa(_,japonez,_,_,_,parliaments), Strada),
  member(casa(Norvegian,norvegian,_,_,_,_), Strada),
  member(casa(Albastru,_,albastru,_,_,_), Strada),
  langa(Norvegian, Albastru),
  member(casa(_,PosesorZebra,_,zebra,_,_), Strada).


laDreapta(X,Y) :- X is Y + 1.
laStanga(X,Y) :- Y is X + 1.
langa(X,Y) :- laDreapta(X,Y); laStanga(X,Y).

word_letters(X,R) :- atom_chars(X, R).
sterge(Elem, ListaInit, ListaModif) :- 
cover([Head|Tail], List2) :- 

    
remove_list(L, [], L):- !.
remove_list([X|Tail], [X|Rest], Result):- !, 
    remove_list(Tail, Rest, Result).
remove_list([X|Tail], L2, [X|Result]):- 
    remove_list(Tail, L2, Result).
	

% Ex2

 :- include('words.pl').

word_letters(Word, Result) :- atom_chars(Word, Result).

cover([], _).
cover([Head1 | Tail1], List2):-
	select(Head1, List2, New),
	cover(Tail1, New).

solution(List, Word, Numar) :-
	word(Word),
	word_letters(Word, Letters),
	length(Letters, Numar),
	cover(Letters, List).

makeSolution(Litere, Word, Len) :-
	solution(Litere, Word, Len);
	L1 is Len - 1,
	makeSolution(Litere, Word, L1).

topSolution(Litere, Word) :-
	length(Litere, Len),
	makeSolution(Litere, Word, Len).