package edu.uclm.esi.wordlesc.http;

import java.util.Map;

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

import edu.uclm.esi.wordlesc.services.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public Map<String, Object> register(Map<String, Object> info) {
		try {
		JSONObject jso= new JSONObject (info);
		String userName= jso.getString("userName");
		String pwd1 = jso.getString("pwd1");
		String pwd2 = jso.getString("pwd2");
		String email = jso.getString("email");
		if(userName.length()<5) 
			throw new Exception ("El nombre necesita tener 5 caracteres");
		
		if(!pwd1.equals(pwd2))
			throw new Exception ("Tienen que coincidir las contraseñas");
		
		if(pwd1.length()<5) 
			throw new Exception ("La contraseña necesita tener 5 o mas caracteres");
		String token=this.userService.register(jso);
		JSONObject result=new JSONObject().put("token", token);
		return result.toMap();
		}catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@GetMapping("/confirmar/{token}")
	public void confirmar(@PathVariable String token) throws Exception {
		this.userService.confirmar(token);
	}
}