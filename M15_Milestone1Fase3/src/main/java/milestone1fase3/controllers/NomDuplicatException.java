package milestone1fase3.controllers;

public class NomDuplicatException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public NomDuplicatException(String nom) {
		super("El jugador \'" + nom + "\' ja existeix; operació abortada");
	}
	
}
