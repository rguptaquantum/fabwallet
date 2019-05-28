package com.rguptaquantum.fabwallet;

import com.rguptaquantum.fabwallet.exception.ErrorDetails;
import com.rguptaquantum.fabwallet.exception.WalletException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class FabWalletExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage());
        return handleExceptionInternal(ex, errorMessage,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = { WalletException.class })
    protected ResponseEntity<ErrorDetails> handleWalletException(
            WalletException ex, WebRequest request) {
        logger.error(ex.toString());
        HttpStatus status = HttpStatus.valueOf(ex.getErrorCode());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleExceptionInternal(ex,body,headers,status, request);
        logger.error(ex.toString());
        String message = (body != null)?body.toString(): ex.getMessage();
        ErrorDetails errorDetails = new ErrorDetails(new Date(),message,request.getDescription(false));
        return new ResponseEntity<>(errorDetails, status);

    }
}
