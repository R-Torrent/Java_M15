package milestone1fase2.controllers;

public class RankingBuitException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public RankingBuitException() {
		super("El ranquing és buit; operació abortada");
	}

}
