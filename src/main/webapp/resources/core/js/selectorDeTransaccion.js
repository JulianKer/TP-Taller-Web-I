let tipoTransaccion = document.getElementById("tipoDeTransaccion");
let divCriptoAObtener = document.querySelector(".divCriptoAObtener")
let divCriptoAObtenerProgramada = document.querySelector(".divCriptoAObtenerProgramada");

//-------------------------------------------------------------
document.addEventListener("DOMContentLoaded",()=>{verTipo(tipoTransaccion.value);})
tipoTransaccion.addEventListener("change", ()=>{verTipo(tipoTransaccion.value);})

function verTipo(tipoTransaccionSeleccionada) {
    switch (tipoTransaccionSeleccionada){
        case "INTERCAMBIO":
            divCriptoAObtener.style.display = "block"
            divCriptoAObtenerProgramada.style.display = "block"
            ;break;
        default:divCriptoAObtener.style.display = "none";break;
    }
}

let tipoTransaccionProgramada = document.getElementById("selector-transaccion-programada");
let divCriptoAObtenerPorgramada = document.querySelector(".divCriptoAObtenerProgramada");
let selectorCriptoProgramada = document.getElementById("selector-cripto-programada");
let labelCuando = document.querySelector(".labelCuando");

if (selectorCriptoProgramada){
    selectorCriptoProgramada.addEventListener("change", ()=>{
        labelCuando.textContent = `Cuando ${selectorCriptoProgramada.value.toUpperCase()} sea`
    })
}

document.addEventListener("DOMContentLoaded",()=>{
    if (selectorCriptoProgramada){
        labelCuando.textContent = `Cuando ${selectorCriptoProgramada.value.toUpperCase()} sea`
    }
});


if (tipoTransaccionProgramada){
    tipoTransaccionProgramada.addEventListener("change", ()=>{
        if (tipoTransaccionProgramada.value === "INTERCAMBIO"){
            divCriptoAObtenerPorgramada.style.display = "block";
        }else{
            divCriptoAObtenerPorgramada.style.display = "none";
        }
    })
}