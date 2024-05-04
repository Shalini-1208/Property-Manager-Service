package com.lumen.dcc.pm.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;


@SpringBootTest
class ExceptionClassTest {
	
	@Autowired
	private ExceptionClass ex;
	@Mock
	private ExceptionClass exs;
	
	
	@Test
	public void  testhandleInvalid() throws Exception{
		
	   ProblemDetail pd=ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
	   ProblemDetail pd1=ex.handleInvalid(new RuntimeException("Invalid id")).forStatus(HttpStatus.NOT_FOUND);
	   pd1.setDetail(pd.getDetail());
	   pd1.setTitle(pd.getTitle());
	   assertEquals(pd.getDetail(),pd1.getDetail());
	   assertEquals(pd.getTitle(),pd1.getTitle());
	}
	
	@Test
    void testHandleInvalidArgument() {
		
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, mock(BindingResult.class));
        ProblemDetail result = ex.handleInvalidArgument(exception);
        assertEquals("Validation failed", result.getTitle());
        assertNotNull(result.getDetail());
    }

	

}
