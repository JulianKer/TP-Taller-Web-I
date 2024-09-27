// original
/*const ctx = document.getElementById('line-chart').getContext('2d');
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Datos de aaaa',
            data: [6,2, 3, 5, 2, 3],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});*/

/*Esto es para ocultar el label de datos de ejemplo*/
/*
const ctx = document.getElementById('line-chart').getContext('2d');
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Datos de Ejemplo', // Puedes cambiar o eliminar esta etiqueta según prefieras.
            data: [6, 2, 3, 5, 2, 3],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
    },
    options: {
        plugins: {
            legend: {
                display: false // Esto oculta la leyenda
            }
        },
        scales: {
            y: {
                beginAtZero: true
            }
        }
    }
});*/
/*
const ctx = document.getElementById('line-chart').getContext('2d');
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Datos de aaaa',
            data: [6, 2, 3, 5, 2, 3],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true
            }
        },
        plugins: {
            legend: {
                display: false // Desactiva la leyenda
            }
        }
    }
});*/

//Con esto puedo ver el grafico en grafico.html pero no el home.html solo en contenedor
/*
const ctx = document.getElementById('line-chart').getContext('2d');
const myChart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Datos de aaaa',
            data: [6, 4, 3, 5, 2, 7],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'grey',
            borderWidth: 3, // Cambia el grosor de la línea aquí
            tension: 0.4 // Esto suaviza la curva de la línea (opcional)
        }]
    },
    options: {
        scales: {
            x: {
                ticks: {
                    color: 'black' // Cambia el color de las etiquetas del eje X
                }
            },
            y: {
                beginAtZero: true,
                ticks: {
                    color: 'black' // Cambia el color de las etiquetas del eje Y
                }
            }
        },
        plugins: {
            legend: {
                display: false // Desactiva la leyenda
            }
        }
    }
});
*/
//Con esto puedo ver el grafico en el home.html pero no en el grafico.html
// Gráfico para Bitcoin (BTC)
const ctx1 = document.getElementById('line-chart-1').getContext('2d');
const myChart1 = new Chart(ctx1, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Bitcoin (BTC)',
            data: [6, 4, 3, 5, 2, 7],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'grey',
            borderWidth: 3,
            tension: 0.4
        }]
    },
    options: {
        scales: {
            x: {
                ticks: {
                    color: 'black'
                }
            },
            y: {
                beginAtZero: true,
                ticks: {
                    color: 'black'
                }
            }
        },
        plugins: {
            legend: {
                display: false
            }
        }
    }
});

// Gráfico para Ethereum (ETH)
const ctx2 = document.getElementById('line-chart-2').getContext('2d');
const myChart2 = new Chart(ctx2, {
    type: 'line',
    data: {
        labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio'],
        datasets: [{
            label: 'Ethereum (ETH)',
            data: [2, 3, 6, 5, 1, 2],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'grey',
            borderWidth: 3,
            tension: 0.4
        }]
    },
    options: {
        scales: {
            x: {
                ticks: {
                    color: 'black'
                }
            },
            y: {
                beginAtZero: true,
                ticks: {
                    color: 'black'
                }
            }
        },
        plugins: {
            legend: {
                display: false
            }
        }
    }
});
