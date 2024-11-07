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
            dash: 'solid', // 'solid', 'dash', 'dot'
        },
        marker: {
            color: '#EEB913',
            size: 10,
            symbol: 'diamond' // tipo 'circle', 'square', 'diamond'
        }
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
    plot_bgcolor: 'none', // Color de fondo del área del gráfico
    paper_bgcolor: 'none' // Color de fondo de todo el gráfico
};

Plotly.newPlot('myDiv', data, layout);