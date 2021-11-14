package milestone1fase1.domain;

import java.util.Date;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "partida")
public class Game {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date registration;
	
	@ManyToOne
	private Player jugador;
	
	@Column(name ="dau1")
	private short die1;
	
	@Column(name ="dau2")
	private short die2;
	
	protected Game() {}
	
	private Game(Player jugador, short die1, short die2) {
		this.jugador = jugador;
		this.die1 = die1;
		this.die2 = die2;
	}
	
	public static Game newGame(Player jugador) {
		int[] rolls = new Random().ints(2L, 1, 6).toArray();
		
		return new Game(jugador, (short)rolls[0], (short)rolls[1]);
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
	
	public Player getJugador() {
		return jugador;
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
	
	public void setJugador(Player jugador) {
		this.jugador = jugador;
	}
	
	public void setDie1(short die1) {
		this.die1 = die1;
	}
	
	public void setDie2(short die2) {
		this.die2 = die2;
	}
	
}
