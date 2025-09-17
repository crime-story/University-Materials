# ProjectManagementApp 📊

ProjectManagementApp este o aplicație web dedicată gestionării proiectelor, sarcinilor și echipelor. Platforma permite crearea și organizarea proiectelor, alocarea task-urilor către membrii echipei, monitorizarea progresului și colaborarea într-un mediu securizat, cu autentificare și roluri definite.

---

## I) Cerințe proiect MVC

- Relații complexe între entități (one-to-one, one-to-many, many-to-many) ✔️

- Implementarea tuturor operațiilor CRUD ✔️

- Utilizarea profilelor și a două baze de date diferite pentru medii distincte (test/dev/prod) ✔️

- pentru testare: H2 ✔️

- pentru dezvoltare/producție: MySQL ✔️

- Testare unitară și de integrare ✔️

- View-uri, formulare, validări și gestionarea excepțiilor ✔️

- Loguri și utilizarea aspectelor pentru cross-cutting concerns ✔️

- Funcționalități de paginare și sortare ✔️

- Spring Security pentru autentificare și autorizare ✔️

---

## II) Proiect Microservicii

Aplicația a fost migrată la o arhitectură bazată pe microservicii, utilizând ecosistemul Spring Cloud.

### Structură

- User Service – gestionează utilizatorii și rolurile acestora

- Project Service – administrează proiectele și task-urile asociate

- Fiecare serviciu are propria bază de date și responsabilități bine delimitate.

### Comunicarea între microservicii

- Descoperirea serviciilor se realizează cu Eureka

- Comunicare sincronă prin WebClient

- Service Discovery disponibil la: http://localhost:8761

### Scalabilitate și monitorizare

- Scalabilitate și load balancing ✔️

- Monitorizare și metrici pentru tracing ✔️

### Reziliență

- **Circuit Breaker + Fallback.** Dacă microserviciile dependente nu sunt disponibile, aplicăm un mecanism de fallback care previne crash-ul aplicației sau al endpoint-ului și livrează un răspuns degradat, dar util.

- **Endpoint:** ```GET /taskSessions/date=YYYY-MM-DD```

Returnează sesiunile de task pentru data cerută.

- **Comportament normal (serviciile sunt online):** pentru fiecare *task* session returnăm câmpurile ```id```, ```date```, ```notes```, ```status``` și întreg obiectul ```task``` (preluat din microserviciul task), nu doar ```taskId```. Obiectul include ```id``` și toate atributele lui (ex.: priority, size, description, etc.), cu metadate de versiune (prod/test).

- **Comportament în fallback (servicii indisponibile):** returnăm ```id```, ```date```, ```notes```, ```status``` **și doar** ```taskId```, împreună cu o marcă explicită a modului fallback/versiunii, astfel încât clientul să poată afișa o variantă minimalistă fără a eșua.

### Design Patterns folosite

- API Gateway Pattern – centralizează accesul către microservicii

- Service Discovery Pattern – microserviciile se descoperă dinamic

- Centralized Configuration Pattern – configurările sunt gestionate într-un singur loc

- Circuit Breaker Pattern – detectează indisponibilitatea unui serviciu și activează fallback logic

- DTO Pattern – separă entitățile domeniului de datele expuse către client
