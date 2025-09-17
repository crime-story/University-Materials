# Diagrame de interacțiune (secvență)

Diagramele de interacțiune surprind comunicarea între obiecte, nu neapărat manipularea datelor asociată comunicării. Ele pun accentul pe mesajele propriu-zise trimise de obiecte și cum aceste mesaje se compun în funcționalități. Dacă diagramele structurale arată cum obiectele se compun pentru a îndeplini o cerință de sistem, diagramele de interacțiune arată precis *cum* realizează obiectele acest lucru. 

Diagramele de interacțiune sunt deținute de elemente ale sistemului. De exemplu, o diagramă de secvență asociată unui subsistem poate arăta cum subsistemul realizează un serviciu pe care îl oferă în interfața sa publică. Asociem o diagramă cu un element al sistemului atașând o notă ce face referire la diagramă.

Există mai multe notații disponibile pentru detalierea unei interacțiuni: diagrame de secvență, diagrame de comunicare, diagrame *timing*, tabele de interacțiune, *interaction overviews*. Cea mai comună notație e cea a [diagramelor de secvență](https://mermaid.js.org/syntax/sequenceDiagram.html).

<img alt="sequence diagram" src="images/sd.png" width="600px">

## Participanți

Participanții la o interacțiune sunt reprezentați ca dreptunghiuri din care pornește o linie punctată denumită *lifeline*, ce arată cât timp există un obiect. 

Numele unui participant se scrie folosind notația:

`object_name [ selector ] : class_name ref decomposition`

- `object_name`: numele instanței implicate în interacțiune
- `selector`: opțional; pentru elemente multivaluate
- `class_name`: numele tipului participantului
- `decomposition`: optional; referință la o altă diagramă de interacțiune ce detaliază cum procesează participantul mesajele pe care le primește

Distrugerea unui participant este reprezentată printr-un simbol de `stop`, sub care linia punctată nu mai continuă.

<img alt="sequence diagram" src="images/sd2.png" width="600px">

## Mesaje

Principalul scop al diagramelor de interacțiune este reprezentarea comunicării dintre lifelines. Comunicarea se poate face sub mai multe forme: apeluri de metode, trimiterea unui semnal, crearea unei instanțe, distrugerea unui obiect etc. Toate acestea sunt denumite generic *mesaje*. 

Un mesaj specifică tipul de comunicare, expeditorul și destinatarul. Cel mai adesea, prin mesaje reprezentăm apeluri de metode între două obiecte. Atunci, putem nota parametrii trimiși metodei în sintaxa mesajului: atribute ale obiectului ce trimite mesajul, constante, valori simbolice (expresii care arată care sunt valorile admise), parametri expliciți ai interacțiunii, atribute ale clasei ce deține interacțiunea.  

Sintaxa unui mesaj:
`attribute = signal_or_operation_name (arguments ) : return_value`

Un mesaj este reprezentat grafic ca o săgeată cu linie continuă de la lifeline-ul expeditorului la lifeline-ul destinatarului. Dacă mesajul este asincron (callerul nu este blocat în timp ce așteaptă procesarea mesajului de către destinatar), se folosește un capăt de săgeată deschis (open). 

<img alt="sequence diagram" src="images/sd3.png" width="600px">

<img alt="sequence diagram" src="images/sd4.png" width="500px">

### Crearea de obiecte

Dacă un mesaj reprezintă crearea unui obiect, folosim o linie punctată, cu un cap de săgeată deschis spre lifeline-ul obiectului nou-creat. Putem sublinia momentul creării obiectului prin plasarea dreptunghiului participantului în dreptul mesajului.

### Tipuri speciale de mesaje

UML definește două tipuri de mesaje speciale: 

- mesaje pierdute: mesaje trimise dar care nu ajung la destinație
- mesaje găsite: mesaje care sunt primite de un obiect, dar expeditorul este necunoscut. Utile pentru modelarea unui mecanism de gestiune a excepțiilor: trimiterea excepției este irelevantă pentru mecanism în sine, o putem modela ca un mesaj găsit. 

Reprezentăm mesajele pierdute/găsite folosind discuri în rolul destinatarului/expeditorului.

<img alt="sequence diagram" src="images/sd5.png" width="400px">

## Ocurențe de execuție

Aratăm că un obiect este implicat în execuția unui tip de acțiune (apelare de metodă) pentru o durată de timp folosind *ocurențe de execuție* (focus control/execution occurrencs). Acestea se reprezintă folosind dreptunghiuri de-a lungul unui lifeline. 

<img alt="sequence diagram" src="images/sd6.png" width="500px">

### Invarianți de stare

Putem plasa etichete de-a lungul unui lifeline pentru a specifica condiții ce trebuie să fie adevărate pentru ca restul interacțiunii să fie validă. Condițiile se numesc invarianți de stare și sunt de regulă expresii booleene, dar putem folosi și stări UML. Le scriem între acolade `{}` pe lifeline-ul obiectului pe care dorim să îl verificăm.

### Ocurențe de evenimente

 Ocurențele de evenimente sunt cele mai mici elemente din diagramele de interacțiune; reprezintă momente în timp în care se întâmplă ceva (e.g. trimiterea și primirea de mesaje); acțiuni asociate unui obiect. 

<img alt="sequence diagram" src="images/sd7.png" width="500px">

*Trace*-urile sunt secvențe de ocurențe de evenimente. Diagramele permit combinarea de fragmente astfel încât ocurențele să se întrepătrundă. 

## Fragmente combinate

Uneori, o secvență de ocurențe de evenimente are constrângeri sau proprietăți speciale. De exemplu, o zonă critică din interacțiune, în care o mulțime de apeluri de metode trebuie să se execute atomic, sau o buclă care iterează peste o colecție. Aceste părți mai mici ale interacțiunii se numesc în UML *fragmente*. 

Fragmentele de interacțiune pot avea condiții de gardă care specifică când sunt valide (pot fi executate), precum în condițiile `if-then`. Fragmentele pot fi adăugate în containere numite *fragmente combinate*. Acestea sunt formate dintr-un operator de interacțiune și unul sau mai multe fragmente. Sunt reprezentate prin dreptunghiuri cu un pentagon în colțul din stânga sus în care este trecut operatorul.

### Operatori de interacțiune

- alternative: `alt` 

<img alt="sequence diagram" src="images/sd8.png" width="600px">

- opțiune: `opt`

<img alt="sequence diagram" src="images/sd9.png" width="500px">

- întrerupere: `break`

<img alt="sequence diagram" src="images/sd10.png" width="600px">

- paralelism: `par`

<img alt="sequence diagram" src="images/sd12.png" width="600px">

- buclă: `loop`

<img alt="sequence diagram" src="images/sd13.png" width="600px">




## Extra reading
Ocurențe de interacțiune (copierea unei interacțiuni în alta), descompunere, continuări (definirea mai multor ramuri ale unei interacțiuni alternative în afara alternativei respective).   

## Exercițiu 

Modelarea programării unui eveniment (nerd night out) folosind diagrame de secvență. 

```mermaid 
sequenceDiagram
 
    actor Alice
    actor other_participants
    participant app
    participant database

    Alice ->> +app: createEvent(date, location)
    activate Alice 
    app ->> +database: create entity
    activate database
    database -->> app: created
    deactivate database
    activate other_participants
    par all other participants 
        app ->> +other_participants: new event notification    
    end
    deactivate other_participants
    app -->> Alice: event created
    par each participant
        other_participants ->> app: vote
        activate other_participants
        app ->> database: store vote
        activate database
        database -->> app: ok
        deactivate database 
        app -->> other_participants: vote registered
        deactivate other_participants
    end
    alt Alice is happy with votes
        Alice ->> app: confirmEvent()
        app ->> database: update event status
        activate database
        database -->> app: updated
        deactivate database
        par all confirming participants
            activate other_participants
            app ->> other_participants: It's a date!
        end
        deactivate other_participants
        app -->> Alice: ok
    else insufficient people responded positively
        Alice ->> app: cancelEvent()
        app ->> database: update event status
        activate database 
        database -->> app: updated
        deactivate database
        app -->> Alice: ok
    end
    
``` 

## Exercițiu 

Modelarea unui flux de code versioning folosind diagrame de secvență. 

```mermaid 

sequenceDiagram
actor developer
participant develop 
participant integration 
actor tester 
participant test_environment 
participant master 

loop as many times as needed
    developer ->> +develop: push code changes
    activate developer
    develop -->> developer: ok
end

developer ->> +integration: merge develop 
integration ->> develop: gimme the changes :D 
develop -->> integration: here you go 
integration ->> integration: apply changes

note over developer,production: notify tester through other means that he can start testing
tester ->> integration: checkout 
activate tester 
integration -->> tester: here you go 
tester ->> tester: build image 
tester ->> +test_environment: deploy 
test_environment -->> tester: deploy ok
tester ->> test_environment: run tests
alt tests ok
    test_environment -->> tester: tests passed
    tester ->> +master: merge integration 
    master ->> integration: gimme the changes :D 
    integration -->> master: here you go 
    master ->> master: apply changes 
    master ->> +production: through magic!
else failed tests 
    test_environment -->> tester: failed tests
    tester ->> developer: do your job properly!
    developer -->> tester: ok, ok!
end

``` 


## Cu ce desenăm diagrame?

1. [Mermaid](http://mermaid.js.org/), 4 Github, a la Markdown.
2. [Lucidchart](https://www.lucidchart.com)
3. [app.diagrams](https://app.diagrams.net/)
4. [Visual Paradigm](https://online.visual-paradigm.com/diagrams/solutions/free-class-diagram-tool/)

## Bibliografie

 - *UML 2.0 in a Nutshell*, Dan Pilone, Neil Pitman - Chapter 10, Interaction Diagrams
 - *Using UML*, Perdita Stevens, Rob Pooley - Chapter 9, Essentials of interaction diagrams & Chapter 10, More on interaction diagrams
