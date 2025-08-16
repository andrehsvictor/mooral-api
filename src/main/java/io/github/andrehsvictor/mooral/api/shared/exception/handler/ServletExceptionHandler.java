package io.github.andrehsvictor.mooral.api.shared.exception.handler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.github.andrehsvictor.mooral.api.shared.dto.exception.ErrorDto;
import io.github.andrehsvictor.mooral.api.shared.dto.exception.ValidationErrorDto;
import io.github.andrehsvictor.mooral.api.shared.exception.ErrorCode;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ServletExceptionHandler extends ResponseEntityExceptionHandler {

    private final Tracer tracer;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ValidationErrorDto> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.VALIDATION_FAILED.name())
                .message("Validation failed")
                .path(extractPath(request))
                .traceId(extractTraceId())
                .errors(validationErrors)
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.MALFORMED_REQUEST.name())
                .message("Malformed JSON request")
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String supportedMethods = String.join(", ", ex.getSupportedMethods());
        String message = String.format("Request method '%s' not supported. Supported methods are: %s",
                ex.getMethod(), supportedMethods);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.METHOD_NOT_ALLOWED.name())
                .message(message)
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String supportedTypes = ex.getSupportedMediaTypes().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
        String message = String.format("Content type '%s' not supported. Supported types are: %s",
                ex.getContentType(), supportedTypes);

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.UNSUPPORTED_MEDIA_TYPE.name())
                .message(message)
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = String.format("Required request parameter '%s' for method parameter type %s is not present",
                ex.getParameterName(), ex.getParameterType());

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.VALIDATION_FAILED.name())
                .message(message)
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());

        ErrorDto errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now().toString())
                .code(ErrorCode.RESOURCE_NOT_FOUND.name())
                .message(message)
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    // Handler para MethodArgumentTypeMismatchException (não é tratada automaticamente pelo ResponseEntityExceptionHandler)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        String message = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        return createErrorResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_FAILED,
                message,
                request);
    }

    private ValidationErrorDto mapFieldError(FieldError fieldError) {
        return ValidationErrorDto.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build();
    }

    private ResponseEntity<ErrorDto> createErrorResponse(HttpStatus status, ErrorCode errorCode, String message,
            WebRequest request) {
        ErrorDto errorDto = ErrorDto.builder()
                .status(status.value())
                .timestamp(Instant.now().toString())
                .code(errorCode.name())
                .message(message)
                .path(extractPath(request))
                .traceId(extractTraceId())
                .build();
        return new ResponseEntity<>(errorDto, status);
    }

    private String extractPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }

    private String extractTraceId() {
        return tracer.currentSpan() != null ? tracer.currentSpan().context().traceId() : null;
    }
}
