package com.tallerwebi.infraestructura.servicio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // elijo que lo envio a traves de gmail (porque la cuenta que cree del proyecto es de gmail)
        mailSender.setPort(587); // aca pongo el puerto que se usa para envio de correos por smtp(tengo entendido que es un protocolo)

        mailSender.setUsername("criptomonedastw1@gmail.com"); // correo del proyecto que cree
        mailSender.setPassword("iyld ixgx mvtt dury"); // contraseña para apps que saque del gmail

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}

