let selectDivisa = document.getElementById("selectDivisa")

let formulario= document.getElementById("formulario")

selectDivisa.addEventListener("change", ()=>{
    formulario.submit()
})

let filasCriptos = document.querySelectorAll(".filaCripto")

filasCriptos.forEach((fila)=>{
    console.log(fila)
    let nombreCripto = fila.id;
    fila.addEventListener("click", ()=>{
        window.location.href = `/spring/inicio/${nombreCripto}`;
    })
})