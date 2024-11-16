let form = document.getElementById("form-criterioDeOrdenamiento")
let select = document.getElementById("criterioDeOrdenamiento")

select.addEventListener("change", ()=>{
    form.submit();
})
