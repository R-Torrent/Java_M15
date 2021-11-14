package milestone1fase1.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import milestone1fase1.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
	
	List<Game> findByJugador_Id(long id);
	
}
