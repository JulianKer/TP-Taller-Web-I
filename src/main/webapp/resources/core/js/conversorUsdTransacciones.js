let cripto = null;
let nombreDeCriptoSeleccionada = "";


//------------ para transacciones comunes -----------------
let inputCantCriptoComun = document.getElementById("cantidadDeCripto");
let inputUsdComun = document.getElementById("cantEnUsdTransaccionComun");
let selectorCriptoComun = document.getElementById("nombreDeCripto");
// ---------------------------------------------------------

selectorCriptoComun.addEventListener("change", ()=>{
    nombreDeCriptoSeleccionada = selectorCriptoComun.value;
    cripto = criptomonedas.find(c => c.nombre === nombreDeCriptoSeleccionada);

    inputCantCriptoComun.addEventListener("input", ()=>{
        let cantidadIngresada = inputCantCriptoComun.value;
        console.log("cantidad ingresada: "+cantidadIngresada)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputUsdComun.value = cripto.precioActual * cantidadIngresada;
        console.log("converison a DOLARES: " + inputUsdComun.value)
    })

    inputUsdComun.addEventListener("input", ()=>{
        let montoIngresado = inputUsdComun.value;
        console.log("DOLARES ingresados: "+montoIngresado)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputCantCriptoComun.value = montoIngresado / cripto.precioActual;
        console.log("converison a DOLARES: " + inputCantCriptoComun.value)

    })
})

function hacerLasConversionesParaTransaccionesComunes() {
    nombreDeCriptoSeleccionada = selectorCriptoComun.value;
    cripto = criptomonedas.find(c => c.nombre === nombreDeCriptoSeleccionada);

    inputCantCriptoComun.addEventListener("input", ()=>{
        let cantidadIngresada = inputCantCriptoComun.value;
        console.log("cantidad ingresada: "+cantidadIngresada)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputUsdComun.value = cripto.precioActual * cantidadIngresada;
        console.log("converison a DOLARES: " + inputUsdComun.value)
    })

    inputUsdComun.addEventListener("input", ()=>{
        let montoIngresado = inputUsdComun.value;
        console.log("DOLARES ingresados: "+montoIngresado)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputCantCriptoComun.value = montoIngresado / cripto.precioActual;
        console.log("converison a DOLARES: " + inputCantCriptoComun.value)

    })
}












//------------ para transacciones programadas -----------------
let inputCantCriptoProgramadas = document.getElementById("cantidadDeCripto-programada");
let inputUsdProgramadas = document.getElementById("cantEnUsdTransaccionProgramada");
let selectorCriptoProgramadas = document.getElementById("selector-cripto-programada");
// ---------------------------------------------------------

selectorCriptoProgramadas.addEventListener("change", ()=>{
    nombreDeCriptoSeleccionada = selectorCriptoProgramadas.value;
    cripto = criptomonedas.find(c => c.nombre === nombreDeCriptoSeleccionada);

    inputCantCriptoProgramadas.addEventListener("input", ()=>{
        let cantidadIngresada = inputCantCriptoProgramadas.value;
        console.log("cantidad ingresada: "+cantidadIngresada)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputUsdProgramadas.value = cripto.precioActual * cantidadIngresada;
        console.log("converison a DOLARES: " + inputUsdProgramadas.value)
    })

    inputUsdComun.addEventListener("input", ()=>{
        let montoIngresado = inputUsdProgramadas.value;
        console.log("DOLARES ingresados: "+montoIngresado)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputCantCriptoProgramadas.value = montoIngresado / cripto.precioActual;
        console.log("converison a DOLARES: " + inputCantCriptoProgramadas.value)

    })
})

function hacerLasConversionesParaTransaccionesProgramadas() {
    nombreDeCriptoSeleccionada = selectorCriptoProgramadas.value;
    cripto = criptomonedas.find(c => c.nombre === nombreDeCriptoSeleccionada);

    inputCantCriptoProgramadas.addEventListener("input", ()=>{
        let cantidadIngresada = inputCantCriptoProgramadas.value;
        console.log("cantidad ingresada: "+cantidadIngresada)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputUsdProgramadas.value = cripto.precioActual * cantidadIngresada;
        console.log("converison a DOLARES: " + inputUsdProgramadas.value)
    })

    inputUsdProgramadas.addEventListener("input", ()=>{
        let montoIngresado = inputUsdProgramadas.value;
        console.log("DOLARES ingresados: "+montoIngresado)
        console.log("Precio de la cripto : "+cripto.precioActual)
        inputCantCriptoProgramadas.value = montoIngresado / cripto.precioActual;
        console.log("converison a DOLARES: " + inputCantCriptoProgramadas.value)

    })
}





document.addEventListener("DOMContentLoaded", ()=>{
    nombreDeCriptoSeleccionada = selectorCriptoComun.value;
    hacerLasConversionesParaTransaccionesComunes();

    nombreDeCriptoSeleccionada = selectorCriptoProgramada.value;
    hacerLasConversionesParaTransaccionesProgramadas();
})




