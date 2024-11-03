function comprar(button) {
    const mp = new MercadoPago('APP_USR-ff839ce6-3f26-4d53-81b1-af7643333c5f');

    // Obtener el ID de la criptomoneda del botón
    let suscripcion = button.getAttribute('data-id');

    // Obtener el valor de la suscripcion usando su ID, y quitar el símbolo $.
    let suscripcionValor = document.getElementById('precio-diamante').textContent.split(" ")[0].replace('$', '').replace('/mes', '').trim();

    // Llamada al servidor para crear la preferencia
    fetch(`/spring/comprar?suscripcion=${suscripcion}&suscripcionValor=${suscripcionValor}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(initPoint => {
            window.location.href = initPoint;
        })
        .catch(error => console.error('Error:', error));
}

function ingresarSaldo() {
    const valorSaldoIngresado = document.getElementById('valorSaldoIngresado').value;

    // Verifica que se haya ingresado un saldo
    if (!valorSaldoIngresado) {
        alert("Por favor, ingrese un saldo válido.");
        return;
    }

    fetch(`/spring/ingresarSaldo?ingresarSaldo=cargarSaldo&valorSaldoIngresado=${valorSaldoIngresado}`)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        })
        .then(initPoint => {
            // Redirige al usuario a Mercado Pago
            window.location.href = initPoint;
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Hubo un problema al ingresar el saldo: ' + error.message);
        });
}
