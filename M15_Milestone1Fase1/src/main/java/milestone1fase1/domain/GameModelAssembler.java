package milestone1fase1.domain;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

import milestone1fase1.controllers.Controller;

@Component
public class GameModelAssembler implements RepresentationModelAssembler<Game, EntityModel<Game>> {
	
	@Override
	public EntityModel<Game> toModel(Game partida) {
		
		long idPartida = partida.getId();
		long idJugador = partida.getJugador().getId();
		
		return EntityModel.of(partida,
				linkTo(methodOn(Controller.class).playGame(idJugador)).withRel("JUGAR!"),
				linkTo(methodOn(Controller.class).findAllGames(idJugador)).withRel("llistat_partides"),
				linkTo(methodOn(Controller.class).deleteGames(idJugador)).withRel("esborrar_partides"),
				linkTo(methodOn(Controller.class).findOneGame(idPartida)).withSelfRel());
	}
	
}
