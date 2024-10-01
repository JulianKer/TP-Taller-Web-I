let selectDivisa = document.getElementById("selectDivisa")

let formulario= document.getElementById("formulario")

selectDivisa.addEventListener("change", ()=>{
    formulario.submit()
})