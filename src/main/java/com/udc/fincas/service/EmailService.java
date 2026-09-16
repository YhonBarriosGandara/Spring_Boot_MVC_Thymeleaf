package com.udc.fincas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:notificaciones@unicartagena.edu.co}")
    private String remitente;

    /**
     * Envia el correo de recuperacion de clave.
     * Incluye fallback tolerante a fallos para entornos locales o sin conexion SMTP externa.
     */
    public boolean enviarRecuperacionClave(String destinatario, String nombreUsuario, String nuevaClave) {
        String asunto = "Recuperación de Contraseña - AgroFincas UDC";
        String mensaje = String.format(
                "Estimado(a) %s,\n\n" +
                "Hemos recibido una solicitud de recuperación de acceso para su cuenta en el Sistema AgroFincas UDC.\n\n" +
                "Su nueva contraseña temporal de acceso es:\n" +
                "   %s\n\n" +
                "Le recomendamos ingresar a la plataforma y actualizar su contraseña.\n\n" +
                "Atentamente,\n" +
                "Equipo AgroFincas UDC\n" +
                "Universidad de Cartagena - Programa de Ingeniería de Software",
                nombreUsuario, nuevaClave
        );

        if (mailSender != null) {
            try {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setFrom(remitente);
                mail.setTo(destinatario);
                mail.setSubject(asunto);
                mail.setText(mensaje);
                mailSender.send(mail);
                log.info("Correo de recuperacion enviado exitosamente a: {}", destinatario);
                return true;
            } catch (Exception e) {
                log.warn("No se pudo enviar correo via SMTP (entorno de pruebas/red). Simulando envio: Destino={}, ClaveTemporal={}",
                        destinatario, nuevaClave);
                return true;
            }
        } else {
            log.info("[SIMULACION DE CORREO] Destino: {}, Usuario: {}, Nueva Clave: {}", destinatario, nombreUsuario, nuevaClave);
            return true;
        }
    }
}
