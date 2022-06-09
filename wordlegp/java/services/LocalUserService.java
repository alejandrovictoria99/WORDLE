package edu.uclm.esi.wordlegp.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import edu.uclm.esi.wordlegp.http.Client;
import edu.uclm.esi.wordlegp.http.LocalManager;

@Service
public class LocalUserService {


	public JSONObject register(JSONObject jso) {
		Client client= new Client();
		String url = LocalManager.get().getConfiguration().getString("SC");
		url=url+"/user/registrer";
		return client.sendPost(url, jso);
	}

	public void confirmar(String token) {
		Client client= new Client();
		String url = LocalManager.get().getConfiguration().getString("SC");
		url=url+"/user/confirmar/"+token;
		client.sendGet(url);
		
	}

}
