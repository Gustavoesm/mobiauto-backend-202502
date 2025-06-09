package com.gustavo.mobiauto_backend.controller.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.gustavo.mobiauto_backend.controller.dto.ErrorResponseDto;
import com.gustavo.mobiauto_backend.infra.exceptions.OfferNotFoundException;
import com.gustavo.mobiauto_backend.infra.exceptions.StoreNotFoundException;
import com.gustavo.mobiauto_backend.infra.exceptions.UserNotFoundException;
import com.gustavo.mobiauto_backend.infra.exceptions.VehicleNotFoundException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyActiveException;
import com.gustavo.mobiauto_backend.service.exceptions.AlreadyDeactivatedException;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedOfferException;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedStoreException;
import com.gustavo.mobiauto_backend.service.exceptions.DeactivatedUserException;
import com.gustavo.mobiauto_backend.service.exceptions.DuplicateException;
import com.gustavo.mobiauto_backend.service.exceptions.EntityInUseException;
import com.gustavo.mobiauto_backend.service.exceptions.TokenGenerationException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler({
                        UserNotFoundException.class,
                        StoreNotFoundException.class,
                        OfferNotFoundException.class,
                        VehicleNotFoundException.class
        })
        public ResponseEntity<ErrorResponseDto> handleNotFound(RuntimeException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                ex.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler({
                        AlreadyActiveException.class,
                        AlreadyDeactivatedException.class,
                        DeactivatedUserException.class,
                        DeactivatedStoreException.class,
                        DeactivatedOfferException.class
        })
        public ResponseEntity<ErrorResponseDto> handleBadRequest(RuntimeException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponseDto> handleValidation(IllegalArgumentException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponseDto> handleAuthentication(BadCredentialsException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                "Invalid credentials",
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        @ExceptionHandler({
                        DuplicateException.class,
                        EntityInUseException.class
        })
        public ResponseEntity<ErrorResponseDto> handleDataIntegrityConflict(RuntimeException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                ex.getMessage(),
                                HttpStatus.CONFLICT.value(),
                                "Conflict");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(TokenGenerationException.class)
        public ResponseEntity<ErrorResponseDto> handleTokenGeneration(TokenGenerationException ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                "Failed to generate authentication token",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex, WebRequest request) {
                ErrorResponseDto error = ErrorResponseDto.of(
                                "An unexpected error occurred",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}