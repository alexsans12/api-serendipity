package com.serendipity.ecommerce.service.implementation;

import com.serendipity.ecommerce.enumeration.VerificationType;
import com.serendipity.ecommerce.exception.ApiException;
import com.serendipity.ecommerce.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String nombre, String email, String verificationUrl, VerificationType verificationType) {
        try {
            send(email, "eddalexcab@gmail.com", getEmailMessage(nombre, verificationUrl, verificationType), verificationType);
            log.info("Correo electrónico enviado a " + email);
        } catch (Exception e) {
            log.error("Error al enviar el correo electrónico a " + email, e);
        }
    }

    public void send(String receiver, String sender, String message, VerificationType verificationType) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            log.info("Enviando correo electrónico a " + receiver);
            log.info("De " + sender);
            log.info("Mensaje " + message);
            messageHelper.setTo(receiver);
            messageHelper.setFrom(sender);
            messageHelper.setSubject(String.format("Serendipity Ecommerce - %s Correo de Verificación", StringUtils.capitalize(verificationType.getType())));
            messageHelper.setText(message);
            // Si tienes un archivo para adjuntar, descomenta la línea siguiente y proporciona la ubicación y el nombre del archivo
            // messageHelper.addAttachment("Attachment", new File(filenameAndLocation));
        };
        this.mailSender.send(preparator);
    }

    /*@Override
    public void sendVerificationEmail(String nombre, String email, String verificationUrl, VerificationType verificationType) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("eddalexcab@gmail.com");
            message.setTo(email);
            message.setText(getEmailMessage(nombre, verificationUrl, verificationType));
            message.setSubject(String.format("Serendipity Ecommerce - %s Correo de Verificación", StringUtils.capitalize(verificationType.getType())));
            mailSender.send(message);
            log.info("Correo electrónico enviado a " + email);
        } catch (Exception e) {
            log.error("Error al enviar el correo electrónico a " + email, e);
        }
    }*/

    private String getEmailMessage(String nombre, String verificationUrl, VerificationType verificationType) {
        switch (verificationType) {
            case PASSWORD -> { return "Hola " + nombre + ",\n\n" +
                    "Para restablecer su contraseña, haga clic en el siguiente enlace:\n" +
                    verificationUrl + "\n\n" +
                    "Si no solicitó restablecer su contraseña, ignore este correo electrónico.\n\n" +
                    "Saludos cordiales,\n" +
                    "El equipo de Serendipity Ecommerce";}
            case ACCOUNT -> { return "Hola " + nombre + ",\n\n" +
                    "Su nueva cuenta ha sido creada con éxito.\n\n" +
                    "Para verificar su cuenta, haga clic en el siguiente enlace:\n" +
                    verificationUrl + "\n\n" +
                    "Saludos cordiales,\n" +
                    "El equipo de Serendipity Ecommerce";}
            default -> throw new ApiException("No se puede enviar el correo electrónico. Tipo de verificación no válido.");
        }
    }
}
