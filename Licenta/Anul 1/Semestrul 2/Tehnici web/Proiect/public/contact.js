let h1Precizari = document.getElementById("precizari");
let h1Program = document.getElementById("program");
let h1Adresa = document.getElementById("adresa");

let PrecizariContainer = document.getElementById("precizari_container");
let ProgramContainer = document.getElementById("program_container");
let AdresaContainer = document.getElementById("adresa_container");


// h1Precizari.addEventListener("click", );

let containere = [PrecizariContainer, ProgramContainer, AdresaContainer];
let h1s = [h1Precizari, h1Program, h1Adresa];

for (let i = 0; i < containere.length; i++) {
    h1s[i].addEventListener("click", function (e) {
        containere[i].classList.toggle("hide"); // cand are clasa hide o scoate si invers o pune
    });
}