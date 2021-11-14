package milestone1fase3.domain;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

import milestone1fase3.controllers.Controller;

@Component
public class PlayerModelAssembler implements RepresentationModelAssembler<Player, EntityModel<Player>> {
	
	@Override
	public EntityModel<Player> toModel(Player jugador) {
		long id = jugador.getId();
		
		return EntityModel.of(jugador,
				linkTo(methodOn(Controller.class).playGame(id)).withRel("JUGAR!"),
				linkTo(methodOn(Controller.class).findAllGames(id)).withRel("llistat_partides"),
				linkTo(methodOn(Controller.class).deleteGames(id)).withRel("esborrar_partides"),
				linkTo(methodOn(Controller.class).modifyPlayer(jugador)).withRel("editar_jugador"),
				linkTo(methodOn(Controller.class).findOnePlayer(id)).withSelfRel(),
				linkTo(methodOn(Controller.class).findAllPlayers()).withRel("llistat_jugadors"));
	}
	
}
