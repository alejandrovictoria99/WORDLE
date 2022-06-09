package edu.uclm.esi.wordlesc.services;

import java.util.UUID;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.wordlesc.dao.UserRepository;
import edu.uclm.esi.wordlesc.model.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userDAO;
	
	public String register(JSONObject jso) {
		User user = new User();
		user.setUserName(jso.getString("userName"));
		user.setEmail(jso.getString("email"));
		user.setPwd(jso.getString("pwd1"));
		user.setToken(UUID.randomUUID().toString());
		user.setHora(System.currentTimeMillis());
		this.userDAO.save(user);
		return user.getToken();
	}

	public void confirmar(String token) throws Exception {
		User user=userDAO.findByToken(token);
		long time=System.currentTimeMillis();
		if(time-user.getHora()>60*15*1000) {
			throw new Exception("Token no encontrado");
		}
		user.setToken(null);
		userDAO.save(user); 
	}
}