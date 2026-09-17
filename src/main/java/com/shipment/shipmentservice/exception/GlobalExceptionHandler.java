package com.shipment.shipmentservice.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.shipment.shipmentservice.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception,
			HttpServletRequest request) {

		Map<String, String> fieldError = exception.getBindingResult().getFieldErrors().stream().collect(Collectors
				.toMap(FieldError::getField, FieldError::getDefaultMessage, (existing, replacement) -> existing));

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				"VALIDATION ERROR", "Request Validation Failed", request.getRequestURI(), fieldError);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(ShipmentIdNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleShipmentNotFoundException(ShipmentIdNotFoundException exception, HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "SHIPMENT_NOT_FOUND",
	            exception.getMessage(),
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(errorResponse);
	}

	
	@ExceptionHandler(InvalidShipmentStatusTransitionException.class)
	public ResponseEntity<ErrorResponse> handleInvalidShipmentStatusTransitionException(InvalidShipmentStatusTransitionException exception, HttpServletRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            "INVALID_STATUS_TRANSITION",
	            exception.getMessage(),
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(ShipmentCancletionException.class)
	public ResponseEntity<ErrorResponse> handleShipmentCancellationException(
			ShipmentCancletionException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            "SHIPMENT_CANNOT_BE_CANCELLED",
	            exception.getMessage(),
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
	        MethodArgumentTypeMismatchException exception,
	        HttpServletRequest request) {

	    String message =
	            "Invalid value '" + exception.getValue()
	            + "' for parameter '" + exception.getName() + "'";

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "INVALID_REQUEST_PARAMETER",
	            message,
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(errorResponse);
	}
}
