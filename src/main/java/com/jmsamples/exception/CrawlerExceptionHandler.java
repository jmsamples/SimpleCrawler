package com.jmsamples.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class CrawlerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ CrawlerException.class })
  protected ResponseEntity<Object> handleNotFound(
    Exception ex, WebRequest request) {
    return handleExceptionInternal(ex, "Crawler Exception Encountered",
      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }
}
