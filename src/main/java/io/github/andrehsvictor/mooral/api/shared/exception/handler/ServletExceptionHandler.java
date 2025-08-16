package io.github.andrehsvictor.mooral.api.shared.exception.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ServletExceptionHandler extends ResponseEntityExceptionHandler {

    private final Tracer tracer;

}
