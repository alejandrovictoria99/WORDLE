package edu.uclm.esi.wordlegp.http;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.wordlegp.model.LocalMatch;
import edu.uclm.esi.wordlegp.services.LocalMatchService;
import edu.uclm.esi.wordlegp.services.LocalUserService;

@RestController
@RequestMapping("user")
public class LocalUserController {
	
	@Autowired
	private LocalUserService userService;
	
	@PutMapping("/register")
	public void register(@RequestBody Map<String, Object> info) {
		try {
		JSONObject jso= new JSONObject (info);
		String userName= jso.getString("userName");
		String pwd1 = jso.getString("pwd1");
		String pwd2 = jso.getString("pwd2");
		String email = jso.getString("email");
		if(userName.length()<4) 
			throw new Exception ("El nombre necesita tener 5 caracteres");
		
		if(!pwd1.equals(pwd2))
			throw new Exception ("Tienen que coincidir las contraseñas");
		
		if(pwd1.length()<5) 
			throw new Exception ("La contraseña necesita tener 5 o mas caracteres");
		String token=this.userService.register(jso).getString("token");
		this.enviarConGMail(email, "Confirmar registro", "http://localhost/user/confirmar/{token}");
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		
	}
	
	private static void enviarConGMail(String destinatario, String asunto, String cuerpo) {
	    // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.
	    String remitente = "jacagi72@gmail.com";  //Para la dirección nomcuenta@gmail.com

	    Properties props = System.getProperties();
	    props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
	    props.put("mail.smtp.user", remitente);
	    props.put("mail.smtp.clave", "1234diseno");    //La clave de la cuenta
	    props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
	    props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
	    props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

	    Session session = Session.getDefaultInstance(props);
	    MimeMessage message = new MimeMessage(session);

	    try {
	        message.setFrom(new InternetAddress(remitente));
	        message.addRecipients(Message.RecipientType.TO, destinatario);   //Se podrían añadir varios de la misma manera
	        message.setSubject(asunto);
	        message.setText(cuerpo);
	        Transport transport = session.getTransport("smtp");
	        transport.connect("smtp.gmail.com", remitente, "1234diseno");
	        transport.sendMessage(message, message.getAllRecipients());
	        transport.close();
	    }
	    catch (MessagingException me) {
	        me.printStackTrace();   //Si se produce un error
	    }
	}
	@GetMapping("/confirmar/{token}")
	public void confirmar(@PathVariable String token) {
		this.userService.confirmar(token);
		
	}
}