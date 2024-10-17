$('#formulario').on('submit', function(e) {
    e.preventDefault();

    document.getElementById('loading').style.display = 'block';
    console.log("SE MUESTRA")
    var criterioDeBusqueda = $('input[name="criterioDeBusqueda"]').val();
    var moneda = $('#selectDivisa').val();

    $.ajax({
        url: window.location.origin + '/home',
        method: 'GET',
        data: {
            moneda: moneda,
            criterioDeBusqueda: criterioDeBusqueda
        }
    })
        .done(function(data) {
            setTimeout(function() {
                document.getElementById('loading').style.display = 'none'; // le pongo block pero no se muestra :v

                $('#result').empty();

                if (data.length > 0) {
                    data.forEach(function(entry) {
                        $('#result').append(`
                        <tr>
                            <td class="crypto-name">
                                <img src="/img/logoCriptomonedas/${entry.key.imagen}" class="crypto-icon" alt="Logo criptomoneda">
                                <span>${entry.key.nombreConMayus} (${entry.key.simbolo})</span>
                            </td>
                            <td class="crypto-chart">
                            
                            </td>
                            <td class="crypto-price">
                                ${entry.value.toFixed(7)} ${moneda.toUpperCase()}
                            </td>
                            <td class="crypto-actions">
                                <a href="/transacciones" class="boton boton-buy">Comprar</a>
                                <a href="/transacciones" class="boton boton-sell">Vender</a>
                            </td>
                        </tr>
                    `);
                    });
                } else {
                    $('#result').append('<tr><td colspan="4">No se encontraron criptos.</td></tr>');
                }
            }, 5000); // este lo puse pero pq no lo veo el mensaje de cargar, sera pq se encuentra tod muy rapido ?
        })
        .fail(function(xhr, status, error) {
            document.getElementById('loading').style.display = 'none';

            console.log(xhr.responseText);
            $('#result').html('<tr><td colspan="4">Hubo un error en la búsqueda.</td></tr>');
        });
});
