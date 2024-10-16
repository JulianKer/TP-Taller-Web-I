let selectFiltro = document.getElementById("transaction-filter")

let formulario= document.getElementById("form_filtro")

selectFiltro.addEventListener("change", ()=>{
    formulario.submit()
})