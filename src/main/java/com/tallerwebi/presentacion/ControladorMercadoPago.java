package com.tallerwebi.presentacion;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
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

        // creacion del producto
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(suscripcion)
                .title("Suscripcion diamante")
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(precioReal)
                //.pictureUrl("http://192.168.1.54:8080/spring/img/suscripcionDiamante.webp") //este lo arregle pq yo habia configurado q para las imgs puedo acceder con el img/ directamente y como MP es externo, tiene q acceder por toda la url publica, nose, aunq ponga una imagen publica no la muestra jaj
                .build();

        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // con este puedo definir a q urls ir en los distintos casos asiq para cad uno le pongo la misma pq quiero q te mande ahi
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("http://localhost:8080/spring/procesarRespuestaDeSuscripcion")  // este por si salio tod o okk
                .failure("http://localhost:8080/spring/procesarRespuestaDeSuscripcion")  // por si hubo algun error, like no se pudo hacer el pago o demas
                .pending("http://localhost:8080/spring/procesarRespuestaDeSuscripcion")  // por si es pago pendiente, osea si se esta procesando seria
                .build();

        // Crear la preferencia de pago
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .autoReturn("approved")  // con esto puedo hacer q si el pago se hizo okk, que te redirija automaticamente a la que defini mas arriba
                .build();

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
