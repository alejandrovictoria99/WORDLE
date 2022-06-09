package edu.uclm.esi.wordlegp.services;

//import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.wordlegp.dao.WordRepository;
import edu.uclm.esi.wordlegp.model.LocalMatch;
import edu.uclm.esi.wordlegp.model.Word;

@Service
public class LocalMatchService {
	
	private ConcurrentHashMap<String, LocalMatch> matches = new ConcurrentHashMap<>();

	@Autowired
	private WordRepository wordDAO;

	public String getRandomWord() {
		/*List<Word> todas = this.wordDAO.findAll();
		for (int i=0; i<todas.size(); i++) {
			Word word = todas.get(i);
			word.setPosicion(i+1);
			this.wordDAO.save(word);
		}*/
		
		int n = (int) this.wordDAO.count();
		int posicion = new Random().nextInt(n-1);
		
		Optional<Word> optWord = this.wordDAO.findByPosicion(posicion);
		if (optWord.isPresent())
			return optWord.get().getPalabra();
		return null;
	}
	
	public LocalMatch addMatch(JSONObject jsoMatch) {
		LocalMatch match = this.matches.get(jsoMatch.getString("id"));
		if (match==null) {
			match = new LocalMatch();
			match.setId(jsoMatch.getString("id"));
			match.setPlayerA(jsoMatch.optString("playerA"));
			this.matches.put(match.getId(), match);
		} else {
			match.setPlayerB(jsoMatch.optString("playerB"));
		}
		return match;
	}

	public LocalMatch find(String matchId) {
		return this.matches.get(matchId);
	}

}