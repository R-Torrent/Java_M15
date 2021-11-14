package milestone1fase1.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

import milestone1fase1.domain.*;
import milestone1fase1.persistence.GameRepository;
import milestone1fase1.persistence.PlayerRepository;

@RestController
@RequestMapping("/players")
public class Controller {
	
	private final PlayerRepository repoJugadors;
	private final GameRepository repoPartides;
	private final PlayerModelAssembler assemblerJugador;
	private final GameModelAssembler assemblerPartida;
	
	private Player jugador;
	
	public Controller (PlayerRepository playerRepository, GameRepository gameRepository,
			PlayerModelAssembler assemblerPlayer, GameModelAssembler assemblerGame) {
		repoJugadors = playerRepository;
		repoPartides = gameRepository;
		assemblerJugador = assemblerPlayer;
		assemblerPartida = assemblerGame;
	}
	
	// Crear un jugador
	@PostMapping
	public ResponseEntity<?> createPlayer(@RequestBody Player nouJugador) {
		String nom = nouJugador.getNom();
		if(nom != Player.DEFAULT && repoJugadors.findPlayerByNom(nom) != null)
			throw new NomDuplicatException(nom);
		
		jugador = nouJugador;
		EntityModel<Player> entityModel = assemblerJugador.toModel(repoJugadors.save(nouJugador));
		
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
		jugador.setNom(novesDades.getNom());
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
		
		EntityModel<Game> entityModel = assemblerPartida
				.toModel(repoPartides.save(Game.newGame(jugador)));
		
		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}
	
	// Trobar una partida concreta
	@GetMapping("/games/{id}")
	public EntityModel<Game> findOneGame(@PathVariable long id) {
		Game partida = repoPartides.findById(id)
				.orElseThrow(() -> new GameNotFoundException(id));
		
		return assemblerPartida.toModel(partida);
	}
	
	// Elimina les tirades del jugador
	@DeleteMapping("/{id}/games")
	public ResponseEntity<?> deleteGames(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		
		List<Game> partides = repoPartides.findByJugador_Id(id);
		repoPartides.deleteAll(partides);
		
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
	public CollectionModel<EntityModel<Game>> findAllGames(@PathVariable long id) {
		jugador = repoJugadors.findById(id)
				.orElseThrow(() -> new PlayerNotFoundException(id));
		
		List<EntityModel<Game>> partides = repoPartides.findAll().stream()
				.filter(p -> p.getJugador().getId() == id)
				.map(assemblerPartida::toModel)
				.collect(Collectors.toList());
		
		return CollectionModel.of(partides,
				linkTo(methodOn(Controller.class).findOnePlayer(id)).withRel("jugador"),
				linkTo(methodOn(Controller.class).findAllGames(id)).withSelfRel());
	}
	
	// Rànquing dels jugadors
	@GetMapping("/ranking")
	public List<String> listRanking() {
		if(repoJugadors.findAll().stream().filter(Player::hasPlayed).count() == 0)
			throw new RankingBuitException();
		
		return Streams
				.zip(LongStream.iterate(1, n -> n + 1).mapToObj(n -> n + ": "),
						repoJugadors.findAll().stream()
								.filter(Player::hasPlayed)
								.sorted(Comparator.comparingDouble(Player::ratioWon).reversed()),
						(ordinal, jugador) -> ordinal + jugador.toString())
				.collect(Collectors.toList());
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
