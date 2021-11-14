package milestone1fase1.controllers;

public class NomDuplicatException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public NomDuplicatException(String nom) {
		super("El jugador \'" + nom + "\' ja existeix; operació abortada");
	}
	
}
