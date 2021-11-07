let PozeContainer = document.getElementById("PozeContainer");

let poze = ["./images/interior1.jpg", "./images/interior2.jpg", "./images/interior3.jpg",
            "./images/interior4.jpg", "./images/interior5.jpg", "./images/interior6.jpg",
            "./images/interior7.jpg", "./images/interior8.jpg", "./images/interior9.jpg",
            "./images/interior10.jpg", "./images/interior11.jpg"];

let img = document.createElement("img");

img.src = poze[0];

let PozeIndex = 0;

PozeContainer.appendChild(img);

function PozaAnterioara() {
    if (PozeIndex > 0) {
        PozeIndex -= 1;
    }
    else
        PozeIndex = poze.length - 1;
    PozeContainer.removeChild(img);
    img.src = poze[PozeIndex];
    PozeContainer.appendChild(img);
}

function PozaUrmatoare() {
    if (PozeIndex < poze.length - 1) {
        PozeIndex += 1;
    }
    else
        PozeIndex = 0;
    PozeContainer.removeChild(img);
    img.src = poze[PozeIndex];
    PozeContainer.appendChild(img);
}

window.addEventListener("keydown", function (e) {
    if (e.key == "ArrowLeft")
    {
        PozaAnterioara();
    }
    else if(e.key == "ArrowRight")
    {
        PozaUrmatoare();
    }
})


