let imgBurger = document.getElementById("imgBurger");
let contLinksResponsive = document.getElementById("contLinksResponsive");


imgBurger.addEventListener("click", ()=>{
    if (contLinksResponsive.style.display === "none"){
        contLinksResponsive.style.display = "flex";
    }else{
        contLinksResponsive.style.display = "none";
    }
})

window.addEventListener("resize", ()=>{
    if(window.innerWidth > 960){
        contLinksResponsive.style.display = "none";
    }
})

window.addEventListener("scroll", ()=>{
    contLinksResponsive.style.display = "none";
})



