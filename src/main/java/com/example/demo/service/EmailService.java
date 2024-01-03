package com.example.demo.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	/**
	 * Metodo para enviar un correo de verificacion, en el creamos un codigo de 4 dígitos que será con el que tendrá que validar su email
	 * @param destinatario
	 * @return
	 * @throws MessagingException
	 */
	public int enviarCodVerificacion(String destinatario) throws MessagingException{
		MimeMessage mensaje = emailSender.createMimeMessage();
		MimeMessageHelper helper;
		
		helper = new MimeMessageHelper(mensaje,true);
		
		//Generamos un número aleatorio de 4 dígitos
		//En caso que Math.random() devuelva el mínimo, 0, el resultado será 10000 + 0 * 90000, que es 10000.
		int codVerificacion = (int)(Math.random()*9999 + 1000);
		
		helper.setSubject("Verifica tu correo");
		helper.setTo(destinatario);
		helper.setText("¡Activa tu cuenta en Curritos!, el código de verificación es: " + codVerificacion,true);
		
		emailSender.send(mensaje);
		return codVerificacion;
	}

}
