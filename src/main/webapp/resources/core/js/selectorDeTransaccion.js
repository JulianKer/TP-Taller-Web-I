let tipoTransaccion = document.getElementById("tipoDeTransaccion");

let divCriptoADar = document.querySelector(".divCriptoADar");
let divCantidadDeCriptoADar = document.querySelector(".divCantidadDeCriptoADar")
let divCriptoAObtener = document.querySelector(".divCriptoAObtener")

let selectorCriptoADar = document.getElementById("nombreDeCripto");
let opcionesSelectorCriptoADar = selectorCriptoADar.options;

let selectorCriptoAObtener = document.getElementById("nombreDeCripto2");
let opcionesSelectorCriptoAObtener = selectorCriptoAObtener.options;


document.addEventListener("DOMContentLoaded",()=>{verTipo(tipoTransaccion.value);})

tipoTransaccion.addEventListener("change", ()=>{verTipo(tipoTransaccion.value);})

function verTipo(tipoTransaccionSeleccionada) {
    switch (tipoTransaccionSeleccionada){
        case "INTERCAMBIO":divCriptoAObtener.style.display = "block";break;
        default:divCriptoAObtener.style.display = "none";break;
    }
}
