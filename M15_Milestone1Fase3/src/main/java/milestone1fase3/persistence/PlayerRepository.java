package milestone1fase3.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import milestone1fase3.domain.Player;

public interface PlayerRepository extends MongoRepository<Player, Long> {
	
	Player findPlayerByName(String name);
	
}
