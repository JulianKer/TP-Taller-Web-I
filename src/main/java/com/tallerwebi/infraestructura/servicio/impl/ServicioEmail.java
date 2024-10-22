package com.tallerwebi.infraestructura.servicio.impl;

import com.tallerwebi.dominio.entidades.Transaccion;
import com.tallerwebi.dominio.entidades.Usuario;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDate;

@Service
public class ServicioEmail {


    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String formarMensaje(Usuario usuario, Transaccion nuevaTransaccion) {
        /*
        este lo habia puesto nomas para probar, el otro tiene "estilos", se ve mejor y no tod junto
        return "¡Hola, " + usuario.getNombre() + " " + usuario.getApellido() + "!" +
                ",\n\nGracias por tu transacción." +
                "\n Resumen: " +
                "\n\n-Tipo: " + nuevaTransaccion.getTipo() +
                "\n-Fecha: " + nuevaTransaccion.getFechaDeTransaccion() +
                "\n-Criptomoneda: " + nuevaTransaccion.getCriptomoneda().getNombreConMayus() + " (" + nuevaTransaccion.getCriptomoneda().getSimbolo() + ") " +
                "\n-Cantidad: " + nuevaTransaccion.getCantidadDeCripto() +
                "\n-Precio unitario: " + nuevaTransaccion.getPrecioAlQueSehizo() + " USD" +
                "\n-Monto total: " + nuevaTransaccion.getMontoTotal() + " USD" +
                "\n-N° Transacción: " + nuevaTransaccion.getId();*/

        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Resumen de Transacción</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            font-size: 1.2em;\n" +
                "            line-height: 1.5;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 20px;\n" +
                "            color: #FFF;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: #282828;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 20px;\n" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #EEB913;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 0.9em;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>¡Hola, " + usuario.getNombre().toUpperCase() + " " + usuario.getApellido().toUpperCase() + "!</h1>\n" +
                "        <p style='color: #EEB913'>Gracias por tu transacción.</p>\n" +
                "        <p style='color: #FFF'><strong>Resumen:</strong></p>\n" +
                "        <ul>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Tipo:</strong> " + nuevaTransaccion.getTipo() + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Fecha:</strong> " + nuevaTransaccion.getFechaDeTransaccion() + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Criptomoneda:</strong> " + nuevaTransaccion.getCriptomoneda().getNombreConMayus() + " (" + nuevaTransaccion.getCriptomoneda().getSimbolo() + ")</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Cantidad:</strong> " + nuevaTransaccion.getCantidadDeCripto() + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Precio unitario:</strong> " + nuevaTransaccion.getPrecioAlQueSehizo() + " USD</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Monto total:</strong> " + nuevaTransaccion.getMontoTotal() + " USD</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>N° Transacción:</strong> " + nuevaTransaccion.getId() + "</li>\n" +
                "        </ul>\n" +
                "        <div class=\"footer\">\n" +
                "            <p style='color: #FFF'>Este es un correo automático, por favor no respondas.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    public String dameElMensajeParaSuscripcion(Usuario usuario, String status, String payment_id, String payment_type) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Resumen de Transacción</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            font-size: 1.2em;\n" +
                "            line-height: 1.5;\n" +
                "            background-color: #f4f4f4;\n" +
                "            padding: 20px;\n" +
                "            color: #FFF;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: #282828;\n" +
                "            border-radius: 5px;\n" +
                "            padding: 20px;\n" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #EEB913;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 20px;\n" +
                "            font-size: 0.9em;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>¡Hola, " + usuario.getNombre().toUpperCase() + " " + usuario.getApellido().toUpperCase() + "!</h1>\n" +
                "        <p style='color: #EEB913'>¡Has obtenido una suscripción!</p>\n" +
                "        <p style='color: #FFF'><strong>Resumen del pago:</strong></p>\n" +
                "        <ul>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Fecha:</strong> " + LocalDate.now() + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Precio:</strong> " + 20 + " USD</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>N° Identificador:</strong> " + payment_id + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Medio:</strong> " + payment_type + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Estado:</strong> " + status + "</li>\n" +
                "            <li style='color: #FFF'><strong style='color: #FFF'>Beneficio:</strong> Podras programar transacciones con criptomonedas de manera fácil y conveniente.</li>\n" +
                "        </ul>\n" +
                "        <div class=\"footer\">\n" +
                "            <p style='color: #FFF'>Este es un correo automático, por favor no respondas.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
