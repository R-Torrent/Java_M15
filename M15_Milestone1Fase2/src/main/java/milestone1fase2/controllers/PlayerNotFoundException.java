package milestone1fase2.controllers;

import java.util.NoSuchElementException;

public class PlayerNotFoundException extends NoSuchElementException {
	
	private static final long serialVersionUID = 1L;
	
	public PlayerNotFoundException(long id) {
		super("Jugador " + id + " desconegut; operació abortada");
	}
	
}
