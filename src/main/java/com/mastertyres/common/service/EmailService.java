package com.mastertyres.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.mastertyres.common.utils.MensajesAlert.mostrarError;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public void enviarCorreoRecuperarPassword(String destino, String asunto, String mensaje){

        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(destino);
            mail.setSubject(asunto);
            mail.setText(mensaje);

            javaMailSender.send(mail);

        }catch (MailException e){
            e.printStackTrace();
            mostrarError("Error interno","","Ocurrio un error al enviar la nueva contraseña al correo. Vuelva a intentarlo mas tarde");
        }


    }

}//class
