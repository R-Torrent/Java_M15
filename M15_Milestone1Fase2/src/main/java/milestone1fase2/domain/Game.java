package milestone1fase2.domain;

import java.util.Date;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

public class Game {
	
	@Transient
	public static final String SEQUENCE_NAME = "games_sequence";
	
	@Id
	private long id;
	
	@Field
	private Date registration = new Date();;
	
	@Field(name ="dau1")
	private short die1;
	
	@Field(name ="dau2")
	private short die2;
	
	protected Game() {}
	
	private Game(short die1, short die2) {
		this.die1 = die1;
		this.die2 = die2;
	}
	
	public static Game newGame() {
		int[] rolls = new Random().ints(2L, 1, 6).toArray();
		
		return new Game((short)rolls[0], (short)rolls[1]);
	}
	
	public boolean isWon() {
		return die1 + die2 == 7;
	}
	
	public long getId() {
		return id;
	}
	
	public Date getRegistration() {
		return registration;
	}
		
	public short getDie1() {
		return die1;
	}
	
	public short getDie2() {
		return die2;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setRegistration(Date registration) {
		this.registration = registration;
	}
	
	public void setDie1(short die1) {
		this.die1 = die1;
	}
	
	public void setDie2(short die2) {
		this.die2 = die2;
	}
	
}
