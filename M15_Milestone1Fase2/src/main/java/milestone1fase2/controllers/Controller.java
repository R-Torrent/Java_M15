package milestone1fase2.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Streams;

import milestone1fase2.domain.*;
import milestone1fase2.persistence.PlayerRepository;
import milestone1fase2.service.SequenceGeneratorService;

@RestController
@RequestMapping("/players")
public class Controller {
	
	private final PlayerRepository repoJugadors;
	private final PlayerModelAssembler assemblerJugador;
	
	@Autowired
	private SequenceGeneratorService sequenceGenerator;
	
	private Player jugador;
	
	public Controller (PlayerRepository playerRepository, PlayerModelAssembler assemblerPlayer) {
		repoJugadors = playerRepository;
		assemblerJugador = assemblerPlayer;
	}
	
	// Crear un jugador
	@PostMapping
	public ResponseEntity<?> createPlayer(@RequestBody Player nouJugador) {
		String nom = nouJugador.getName();
		if(nom != Player.DEFAULT && repoJugadors.findPlayerByName(nom) != null)
			throw new NomDuplicatException(nom);
		
		nouJugador.setId(sequenceGenerator.generateSequence(Player.SEQUENCE_NAME));
		jugador = nouJugador;
		EntityModel<Player> entityModel = assemblerJugador.toModel(repoJugadors.save(jugador));
		
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	// Trobar un jugador concret
	@GetMapping("/{id}")
	public EntityModel<Player> findOnePlayer(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		
		return assemblerJugador.toModel(jugador);
	}
	
	// Modificar el nom de l'últim jugador visionat
	@PutMapping
	public ResponseEntity<?> modifyPlayer(@RequestBody Player novesDades) {
		jugador.setName(novesDades.getName());
		EntityModel<Player> entityModel = assemblerJugador.toModel(repoJugadors.save(jugador));
		
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	// Partida de daus
	@PostMapping("/{id}/games")
	public ResponseEntity<?> playGame(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		
		Game partida = Game.newGame();
		partida.setId(sequenceGenerator.generateSequence(Game.SEQUENCE_NAME));
		if(jugador.getGames() == null)
			jugador.setGames(List.of(partida));
		else	
			jugador.getGames().add(partida);
		EntityModel<Player> entityModel = assemblerJugador.toModel(repoJugadors.save(jugador));
		
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	// Trobar una partida concreta
	@GetMapping("/games/{id}")
	public EntityModel<Player> findOneGame(@PathVariable long id) {
		for(Player jugador : repoJugadors.findAll()) {
			if(jugador.getGames() == null)
				continue;
			if(jugador.getGames().stream().mapToLong(g -> g.getId()).anyMatch(gid -> gid == id))
				return assemblerJugador.toModel(jugador);
		}
		
		throw new GameNotFoundException(id);
	}
	
	// Elimina les tirades del jugador
	@DeleteMapping("/{id}/games")
	public ResponseEntity<?> deleteGames(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		jugador.getGames().clear();
		jugador.setGames(null);
		repoJugadors.save(jugador);
		
		return ResponseEntity.noContent().build();
	}
	
	// Llistat de jugadors
	@GetMapping
	public CollectionModel<EntityModel<Player>> findAllPlayers() {
		List<EntityModel<Player>> jugadors = repoJugadors.findAll().stream()
				.map(assemblerJugador::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(jugadors,
				linkTo(methodOn(Controller.class).listRanking()).withRel("ranking_jugadors"),
				linkTo(methodOn(Controller.class).findWinner()).withRel("millor_jugador"),
				linkTo(methodOn(Controller.class).findLoser()).withRel("pitjor_jugador"),
				linkTo(methodOn(Controller.class).findAllPlayers()).withSelfRel());
	}
	
	// Llistat de partides d'un jugador
	@GetMapping("/{id}/games")
	public CollectionModel<Game> findAllGames(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		
		List<Game> partidas = jugador.getGames();
		return CollectionModel.of((partidas == null ? List.of() : partidas),
				linkTo(methodOn(Controller.class).findOnePlayer(id)).withRel("jugador"),
				linkTo(methodOn(Controller.class).findAllGames(id)).withSelfRel());
	}
	
	// Rànquing dels jugadors
	@GetMapping("/ranking")
	public CollectionModel<String> listRanking() {
		if(repoJugadors.findAll().stream().filter(Player::hasPlayed).count() == 0)
			throw new RankingBuitException();
		
		return CollectionModel.of(Streams
				.zip(LongStream.iterate(1, n -> n + 1).mapToObj(n -> n + ": "),
						repoJugadors.findAll().stream()
								.filter(Player::hasPlayed)
								.sorted(Comparator.comparingDouble(Player::ratioWon).reversed()),
						(ordinal, jugador) -> ordinal + jugador.toString())
				.collect(Collectors.toList()),
				linkTo(methodOn(Controller.class).listRanking()).withRel("llistat_jugadors"),
				linkTo(methodOn(Controller.class).findAllPlayers()).withSelfRel());
	}
	
	// Pitjor jugador
	@GetMapping("/ranking/loser")
	public EntityModel<Player> findLoser() {
		Player jugador= repoJugadors.findAll().stream()
				.filter(Player::hasPlayed)
				.reduce((p1, p2) -> p1.ratioWon() < p2.ratioWon() ? p1 : p2)
				.orElseThrow(RankingBuitException::new);
		
		return assemblerJugador.toModel(jugador);
	}
	
	// Millor jugador
	@GetMapping("/ranking/winner")
	public EntityModel<Player> findWinner() {
		Player jugador= repoJugadors.findAll().stream()
				.filter(Player::hasPlayed)
				.reduce((p1, p2) -> p1.ratioWon() < p2.ratioWon() ? p2 : p1)
				.orElseThrow(RankingBuitException::new);
		
		return assemblerJugador.toModel(jugador);
	}
	
}
