let selectFiltro = document.getElementById("transaction-filter")
let selectFiltroProgramadas = document.getElementById("transaction-filter-programadas")

let formulario= document.getElementById("form_filtro")
let formularioProgramadas= document.getElementById("form_filtro-programadas")

selectFiltro.addEventListener("change", ()=>{formulario.submit()})
selectFiltroProgramadas.addEventListener("change", ()=>{formularioProgramadas.submit()})