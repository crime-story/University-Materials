// Import packages
const express = require("express");
const morgan = require("morgan");
const bodyParser = require("body-parser");
const cors = require("cors");
const uuid = require("uuid");

const fs = require("fs");

// Aplicatia
const app = express();

// Middleware
app.use(morgan("tiny"));
app.use(bodyParser.json());
app.use(cors());

// Create
app.post("/restaurant", (req, res) => {
    const rezervareList = readJSONFile();
    let newRezervare = {
        nume: req.body.nume,
        data1: req.body.data1,
        telefon: req.body.telefon,
        id: uuid.v4.apply()
    }
    // adaugam noua rezervare in lista noastra
    rezervareList.push(newRezervare);
});

// Read One
app.get("/restaurant/:id", (req, res) => {
    const dogsList = readJSONFile();
    // Completati cu codul vostru aici
});

// Read All
app.get("/restaurant", (req, res) => {
    const dogsList = readJSONFile();
    // Completati cu codul vostru aici
});

// Update
app.put("/restaurant/:id", (req, res) => {
    const dogsList = readJSONFile();
    // Completati cu codul vostru aici
});

// Delete
app.delete("/restaurant/:id", (req, res) => {
    const dogsList = readJSONFile();
    // Completati cu codul vostru aici
});

// Functia de citire din fisierul db.json
function readJSONFile() {
    return JSON.parse(fs.readFileSync("db.json"))["restaurant"];
}

// Functia de scriere in fisierul db.json
function writeJSONFile(content) {
    fs.writeFileSync(
        "db.json",
        JSON.stringify({ dogs: content }),
        "utf8",
        err => {
            if (err) {
                console.log(err);
            }
        }
    );
}

// Pornim server-ul
app.listen("3000", () =>
    console.log("Server started at: http://localhost:3000")
);