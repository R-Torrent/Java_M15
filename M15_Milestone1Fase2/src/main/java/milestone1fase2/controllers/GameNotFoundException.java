package milestone1fase2.controllers;

import java.util.NoSuchElementException;

public class GameNotFoundException extends NoSuchElementException {
	
	private static final long serialVersionUID = 1L;
	
	public GameNotFoundException(long id) {
		super("Partida " + id + " desconeguda; operació abortada");
	}
	
}
