let cards = document.querySelectorAll(".card")

cards.forEach((card)=>{
    console.log(card)
    let nombreCripto = card.id;
    card.addEventListener("click", ()=>{
        window.location.href = `/spring/inicio/${nombreCripto}`;
    })
})




let fechas = [];
let precios = [];
let nombreCripto = "";

historialDePrecios.forEach(item => {
    fechas.push(item.fechaDelPrecio);
    precios.push(item.precioActual);
    if (nombreCripto === ""){
        nombreCripto = item.criptomoneda.nombreConMayus;
    }
});

document.title = `${nombreCripto} | Crypto`;

var data = [
    {
        x: fechas,
        y: precios,
        type: 'scatter',
        mode: 'lines+markers',
        line: {
            color: '#7c7c7c',
            width: 3,
            dash: 'solid',
            //shape: 'spline',  este es pa q sea curva la line
        },
        marker: {
            color: '#EEB913',
            size: 10,
            symbol: 'diamond'
        },
    }
];


var layout = {
    title: {
        text: `${nombreCripto}`,
        font: {
            size: 24,
            color: '#333'
        }
    },
    xaxis: {
        title: 'Fecha',
        titlefont: {
            size: 18,
            color: '#555'
        },
        tickformat: '%Y-%m-%d',
        showgrid: true,
        gridcolor: '#e0e0e0'
    },
    yaxis: {
        title: 'Precio',
        titlefont: {
            size: 18,
            color: '#555'
        },
        showgrid: true,
        gridcolor: '#e0e0e0'
    },
    responsive: true,
    plot_bgcolor: 'none',
    paper_bgcolor: 'none'
};

Plotly.newPlot('myDiv', data, layout);

window.addEventListener('resize', function() {
    Plotly.Plots.resize('myDiv');
});