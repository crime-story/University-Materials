//let form = document.getElementById('Rezervari'); 
//form.addEventListener('submit', (event) => { console.log(event); });

let rezervari = [];

function myFunction() {
    var x = document.getElementById("nume");
    var y = document.getElementById("data");
    var z = document.getElementById("telefon");

    rezervari.push([x.value, y.value , z.value]);

    let div = document.getElementById("Text");
    div.innerText = " ";

    for (let i = 0; i < rezervari.length; i++) {
        let div = document.getElementById("Text");
        let rezervare = document.createElement("rezervare");
        rezervare.innerText = rezervari[i] + " " + "\n";
        // console.log(rezervare);
        var deleteButton = document.createElement("eliminaRezervare");

        div.appendChild(rezervare);
    }
    return false;
}
var currentDogId;

function fetchDogs() {
    let content = document.getElementById("content");

    let element = document.getElementsByClassName("element")[0];
    let p = document.createElement("p");
    p.innerText = "loading...";
    p.setAttribute("id", "loading");
    element.appendChild(p);

    fetch('http://localhost:3000/Restaurant', {
        method: 'get' // sunt 4 requesturi principale: GET, POST, PUT, DELETE
    }).then(function(response) {
        response.json().then((data) => {
            if(data.length) {
                element.removeChild(p);
            }
            // console.log(data[0].name);
            for(let i = 0; i < data.length; i++){
                let nume = document.createElement("p");
                let data1 = document.createElement("p");
                let telefon = document.createElement("p");
                nume.innerText = data[i].nume;
                nume.className = "NumeClass";
                data1.innerText = data[i].data1;
                data1.className = "DataClass";
                telefon.innerText = data[i].telefon;
                telefon.className = "TelefonClass";

                content.appendChild(nume);
                content.appendChild(data1);
                content.appendChild(telefon);

                let editButton = document.createElement("button");
                let editText = document.createTextNode("Modifica");
                editButton.className = "Edit";
                editButton.appendChild(editText);
                editButton.onclick = function() {
                    document.getElementById("nume").value = data[i].nume;
                    document.getElementById("data").value = data[i].data1;
                    document.getElementById("telefon").value = data[i].telefon;
                    currentDogId = data[i].id;
                }
                content.appendChild(editButton);

                let deleteButton = document.createElement("button");
                let deleteText = document.createTextNode("Sterge");
                deleteButton.className = "Delete";
                deleteButton.appendChild(deleteText);
                deleteButton.onclick = function() {
                    deleteDog(data[i].id);
                }
                content.appendChild(deleteButton);
            }
        })
    })
}

fetchDogs()

function addDog() {
    var x = document.getElementById("nume").value;
    var y = document.getElementById("data").value;
    var z = document.getElementById("telefon").value;
    var newDog = {
        nume: x,
        data1: y,
        telefon: z
    }
    fetch('http://localhost:3000/Restaurant', {
        method: 'post', // semnalam faptul ca vrem sa introducem ceva nou in baza de date
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(newDog)
    }).then(function(response) {
        // console.log(response);
        window.location.reload();
    })
}

function editDog() {
    var x = document.getElementById("nume").value;
    var y = document.getElementById("data").value;
    var z = document.getElementById("telefon").value;
    var newDog = {
        nume: x,
        data1: y,
        telefon: z
    }

    fetch('http://localhost:3000/Restaurant/' + currentDogId, { //ca sa schimbam datele unui caine, trebuie sa il identificam prin id. Acesta se afla in currentDogId. La final url-ul va fi http://localhost:3000/dogs/1
        method: 'put', // semnalam faptul ca vrem sa actualizam ceva deja existent in baza de date
        headers: {
            'Content-Type': 'application/json' //semnalam faptul ca lucram cu fisiere de tip json
        },
        body: JSON.stringify(newDog) // in body vom pune noul obiect (newDog). Acest body va ajunge in baza de date. Pentru a putea fi pus in BD trebuie sa fie sub forma de string, de aceea folosim JSON.stringify
    }).then(function(response) {
        window.location.reload();
    })
}

function deleteDog(id) {
    fetch('http://localhost:3000/Restaurant/' + id, {
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(function(response) {
        window.location.reload();
    })
}