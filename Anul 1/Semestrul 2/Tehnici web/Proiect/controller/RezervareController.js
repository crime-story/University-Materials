var express = require("express");
var router = express.Router();

const rezervareService = require("../service/RezervareService");

// Read All
router.get("/restaurant", (req, res) => {
    const rezervari = rezervareService.getAllRezervari();
    if (rezervari!= undefined && rezervari.length !=0){
        res.status(200).send(rezervari);
    }
    else {
        res.status(404).send("Nu exista rezervari!");
    }
});

// Create
router.post("/restaurant", (req, res) => {
    let newRezervare = rezervareService.addRezervari(req.body);
    res.status(200).send(newRezervare); // trimitem noua rezervare care sa aiba setat atributul de id
    // status 200 inseamna success
});

// Read One
router.get("/restaurant/:id", (req, res) => {
    let id = req.params.id; // luam id-ul pe care il cautam pentru rezervare
    let rezervare = rezervareService.getRezervareById(id);
    if(rezervare != null && rezervare != undefined) { // se uita pe tipul de date ===
        res.status(200).send(rezervare);
    } else {
        res.status(404).send("Nu exista rezervarea!");
    }
});

// Update
router.put("/restaurant/:id", (req, res) => {
    let id = req.params.id; // id-ul rezervarii pe care vreau sa o actualizez
    let rezervari = rezervareService.updateRezervare(id, req.body);
    // let exista = false; // daca exista rezervarea
    if(rezervari != null && rezervari != undefined) { // daca am gasit o rezervare si am actualizat-o
        // suntem obligati sa rescriem fisierul db.json
        // updateRezervare.writeJSONFile(rezervari);
        res.status(200).send(rezervari);
    }
    else { // daca nu am gasit rezervarea
        res.status(404).send("Nu exista rezervarea!");
        // conventia 404 este pentru Not Found!
    }
});

// Delete
router.delete("/restaurant/:id", (req, res) => {
    //const rezervari = readJSONFile();
    let id = req.params.id; // id-ul rezervarii pe care vreau sa o stergem
    let exista = rezervareService.deleteRezervare(id);

    if(exista === true) {
        res.status(200).send("Rezervarea a fost stersa!");
    } else {
        res.status(404).send("Nu exista rezervarea!");
    }
});

module.exports = router;