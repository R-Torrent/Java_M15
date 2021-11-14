package milestone1fase2.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "jugador")
public class Player {
	
	public static final String DEFAULT = "ANÒNIM";
	
	@Transient
	public static final String SEQUENCE_NAME = "players_sequence"; 
	
	@Id
	private long id;
	
	@Field
	private Date registration = new Date();
	
	@Field(name = "nom")
	private String name = DEFAULT;
	
	@Field(name = "partides")	
	private List<Game> games;
	
	protected Player() {}
	
	@Override
	public String toString() {
		long gW = gamesWon(), gP = gamesPlayed();
		return "Player{ id=" + id + ", nom=\'" + name + "\', partides=" + gW + "|" + gP +
				String.format(" (%.2f%%) }", 100.0 * gW/gP);
	}
	
	public long gamesPlayed() {
		return games == null ? 0 : games.size();
	}
	
	public long gamesWon() {
		return games == null ? 0 : games.stream().filter(game -> game.isWon()).count();
	}
	
	public boolean hasPlayed() {
		return gamesPlayed() > 0;
	}
	
	public double ratioWon() {
		return hasPlayed() ? (double)gamesWon()/gamesPlayed() : 0;
	}

	public long getId() {
		return id;
	}
	
	public Date getRegistration() {
		return registration;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Game> getGames() {
		return games;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setRegistration(Date registration) {
		this.registration = registration;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setGames(List<Game> games) {
		this.games = games;
	}
	
}
