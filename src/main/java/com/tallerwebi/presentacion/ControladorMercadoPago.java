package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.HttpStatus;
import com.mercadopago.resources.preference.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorMercadoPago {

    /*
     Este es seria el comprador,si pones el usuario y contraseña te tira error por que no te podes compara a vos mismo
     Nombre de usuario: TESTUSER789186867
     Contraseña: AqqoYJT8bU

     Este es el usuario que seria el cliente y tiene saldo para poder comprar
     Nombre de usuario:TESTUSER130842149
     contraseña: eJie7vcjR0
     */
    @GetMapping("/comprar")
    public ResponseEntity<String> comprar(@RequestParam String suscripcion, @RequestParam String suscripcionValor) {
        MercadoPagoConfig.setAccessToken("APP_USR-5475377288084995-101015-d8c2349afb05691bdaaa53b80fd5c174-2031088844");

        BigDecimal precioReal = new BigDecimal(suscripcionValor);
       // BigDecimal precioReal;
      /* try {
            System.out.println(suscripcionValor);
            precioReal = new BigDecimal(suscripcionValor);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid subscription value");
        }*/

        // creacion del producto
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(suscripcion)
                .title("Suscripcion diamante")
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(precioReal)
                .pictureUrl("/imageswebapp/resources/core/img/suscripcionDiamante.webp") //Esto no funciona
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // Esto seria el item que el usuario va a comprar
        PreferenceRequest preferenceRequest = PreferenceRequest.builder().items(items).build();
        // Se crea un cliente de Mercado Pago que se utiliza que va a tener la solicitud de creación de la preferencia de pago.
        PreferenceClient client = new PreferenceClient();

        try {
            // envía la solicitud de creación de la preferencia a Mercado Pago.
            Preference preference = client.create(preferenceRequest);
            // el InitPoint es el enlace que Mercado Pago para comprar el producto
            return ResponseEntity.ok(preference.getInitPoint());
        } catch (MPException | MPApiException e) {
            // son exepciones de un error en la comunicacion de mp o de la creacion de la preferencia
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/error");
        }

    }
}
