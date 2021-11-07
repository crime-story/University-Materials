// Import packages
const express = require("express"); // importez frameworkuri
const morgan = require("morgan"); // optional probabil pt loguri
const bodyParser = require("body-parser"); // parseaza body il face sa fie json
// daca il scot nu as mai putea face body parser
const cors = require("cors"); // daca lasam gol il putea accesa oricine
//
const uuid = require("uuid");

// Aplicatia
const app = express();

// Middleware
app.use(morgan("tiny"));
app.use(bodyParser.json());
app.use(cors());
app.use(require("./controller/RezervareController"));

// Pornim server-ul
app.listen("3000", () =>
    console.log("Server started at: http://localhost:3000")
);