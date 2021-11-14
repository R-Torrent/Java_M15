package milestone1fase1.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jugador")
public class Player {
	
	public static final String DEFAULT = "ANÒNIM";
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date registration;
	
	@Column(columnDefinition = "varchar(50)")
	private String nom = DEFAULT;
	
	@OneToMany(mappedBy = "jugador") @JsonIgnore	
	private List<Game> partides;
	
	protected Player() {}
	
	@Override
	public String toString() {
		long gW = gamesWon(), gP = gamesPlayed();
		return "Player{ id=" + id + ", nom=\'" + nom + "\', partides=" + gW + "|" + gP +
				String.format(" (%.2f%%) }", 100.0 * gW/gP);
	}
	
	public long gamesPlayed() {
		return partides == null ? 0 : partides.size();
	}
	
	public long gamesWon() {
		return partides == null ? 0 : partides.stream().filter(game -> game.isWon()).count();
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
	
	public String getNom() {
		return nom;
	}
	
	public List<Game> getPartides() {
		return partides;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setRegistration(Date registration) {
		this.registration = registration;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public void setPartides(List<Game> partides) {
		this.partides = partides;
	}
	
}
