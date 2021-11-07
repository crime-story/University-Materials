const RezervareRepository = require("../repository/RezervareRepository");
const uuid = require("uuid");

module.exports.getAllRezervari = () => {
    let rezervari = RezervareRepository.readJSONFile();
    return rezervari;
}

module.exports.addRezervari = (newRezervare) => {
    const rezervari = RezervareRepository.readJSONFile();
    newRezervare.id = uuid.v4.apply();
    // adaugam noua rezervare in lista noastra
    rezervari.push(newRezervare);
    RezervareRepository.writeJSONFile(rezervari);
    // trimit raspuns catre frontend ca totul a fost in regula
    return rezervari;
}

module.exports.getRezervareById = (id) => {
    const rezervari = RezervareRepository.readJSONFile();
    let rezervare = null;
    rezervari.forEach(dog => {
        if(dog.id === id) {
            rezervare = dog;
        }
    })
    return rezervare;
}

module.exports.updateRezervare = (id, rezervare) => {
    const rezervari = RezervareRepository.readJSONFile();
    //let update = null;
    for(let i = 0; i < rezervari.length; i++) {
        if(rezervari[i].id === id) {
            if(rezervare.nume) {
                rezervari[i].nume = rezervare.nume;
            }

            if(rezervare.data1) {
                rezervari[i].data1 = rezervare.data1;
            }

            if(rezervare.telefon) {
                rezervari[i].telefon = rezervare.telefon;
            }
            // update = rezervari[i];
            // break;
            RezervareRepository.writeJSONFile(rezervari);
            return rezervari[i];
        }
    }
    // RezervareRepository.writeJSONFile(rezervari);
    // return update;
    return null;
}

module.exports.deleteRezervare = (id) => {
    const rezervari = RezervareRepository.readJSONFile();
    //let exista = false; // daca exista rezervarea
    for(let i = 0; i < rezervari.length; i++) { // iteram prin toate rezervarile si o cautam dupa id
        if(rezervari[i].id === id) {
            //exista = true;
            rezervari.splice(i, 1); // sterg rezervarea de pe pozitia i
            // splice  sterge de la indexul i atatea elemente cate indica al doilea argument
            RezervareRepository.writeJSONFile(rezervari);
            return true;
        }
    }
    return false;
}