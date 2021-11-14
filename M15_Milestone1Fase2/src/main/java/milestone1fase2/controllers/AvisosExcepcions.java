package milestone1fase2.controllers;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AvisosExcepcions {
	
	@ResponseBody
	@ExceptionHandler({PlayerNotFoundException.class, GameNotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String ElementNoTrobatHandler(NoSuchElementException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler({NomDuplicatException.class, RankingBuitException.class})
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	String ExcepcionsHandler(RuntimeException ex) {
		return ex.getMessage();
	}
	
}
