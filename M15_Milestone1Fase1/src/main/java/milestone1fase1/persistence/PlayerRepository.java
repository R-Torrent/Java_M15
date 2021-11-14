package milestone1fase1.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import milestone1fase1.domain.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	
	Player findPlayerByNom(String nom);
	
}
