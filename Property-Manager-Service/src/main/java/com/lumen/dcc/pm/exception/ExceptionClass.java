package com.lumen.dcc.pm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionClass {
	
	@ExceptionHandler(value=RuntimeException.class)
	public ProblemDetail handleInvalid(RuntimeException ex)
	{
		ProblemDetail pd=ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
		pd.setDetail(ex.getMessage());
		pd.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
		return pd;
	}
		
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	public ProblemDetail handleInvalidArgument(MethodArgumentNotValidException ex)
	{
		FieldError s=ex.getBindingResult().getFieldError();
		ProblemDetail pd=ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
		pd.setTitle(HttpStatus.BAD_REQUEST.getReasonPhrase());
		pd.setDetail(s.getDefaultMessage());
		return pd;
	}
	
}
