package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ControladorMercadoPagoTest {

    @Test
    public void checkoutTest() throws MPException {
        // poner la credencial del public key
        MercadoPagoConfig.setAccessToken("TEST-707fc8ce-5190-4b1a-a150-cf2fe3c3356f");
        // Aquí podrías crear una preferencia de pago como ejemplo:
        Preference preference = new Preference();

        System.out.println("SDK initialized successfully");
    }

    @Test
    public void checkoutProTest() throws MPException, MPApiException {

        // Configurar las credenciales de la SDK
        MercadoPagoConfig.setAccessToken("TEST-4077212954048145-100710-f958eca3ce99bf23ab82eac121556ecc-1685531410");

        // Simulamos una criptomoneda como item de la preferencia
        String nombreDeCripto = "Bitcoin";

        // Crear el objeto de item para la preferencia
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("1")
                .title(nombreDeCripto)
                .description("Compra de " + nombreDeCripto)
                .quantity(1)  // Cantidad
                .currencyId("ARS")  // Tipo de moneda (Peso argentino)
                .unitPrice(new BigDecimal("1000.0"))  // Precio por unidad
                .build();

        // Lista de items (en este caso solo es uno, pero podrías agregar más)
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // Crear la preferencia con los items
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)  // Lista de items
                .build();

        // Crear cliente para gestionar preferencias
        PreferenceClient client = new PreferenceClient();

        // Generar la preferencia en Mercado Pago
        Preference preference = client.create(preferenceRequest);

        // Imprimir la URL
        System.out.println("Redirige al usuario a: " + preference.getInitPoint());
    }

    @Test
    public void buscarError() throws MPException, MPApiException {
        // Configurar las credenciales de la SDK
        MercadoPagoConfig.setAccessToken("TEST-4077212954048145-100710-f958eca3ce99bf23ab82eac121556ecc-1685531410");

        // Simulamos una criptomoneda como item de la preferencia
        String nombreDeCripto = "Bitcoin";

        // Crear el objeto de item para la preferencia
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id("1")  // Un identificador para el producto
                .title(nombreDeCripto)  // Nombre de la criptomoneda
                .description("Compra de " + nombreDeCripto)  // Descripción
                .quantity(1)  // Cantidad
                .currencyId("ARS")  // Tipo de moneda (Peso argentino)
                .unitPrice(new BigDecimal("1000.0"))  // Precio por unidad
                .build();
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .build();
        try {
            // Crear cliente para gestionar preferencias
            PreferenceClient client = new PreferenceClient();

            // Generar la preferencia en Mercado Pago
            Preference preference = client.create(preferenceRequest);

            // Imprimir la URL
            System.out.println("Redirige al usuario a: " + preference.getInitPoint());

        } catch (MPApiException e) {
            System.err.println("Error al crear la preferencia: " + e.getApiResponse().getContent());
        }
    }
}
