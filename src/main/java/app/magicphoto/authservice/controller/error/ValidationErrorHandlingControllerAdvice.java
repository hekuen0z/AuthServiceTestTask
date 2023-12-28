package app.magicphoto.authservice.controller.error;

import app.magicphoto.authservice.error.validation.ValidationErrorResponse;
import app.magicphoto.authservice.error.validation.Violation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Tag(name = "Контроллер для обработки ошибок валидации")
public class ValidationErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(summary = "Метод для отправки ValidationErrorResponse при возникновении ошибки ConstraintValidationException")
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException exception) {

        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Operation(summary = "Метод для отправки ValidationErrorResponse после получения неправильных данных через DTO")
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        final List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(
                        violation -> new Violation(
                                violation.getField(),
                                violation.getDefaultMessage()
                        )
                )
                .collect(Collectors.toList());

        return new ValidationErrorResponse(violations);
    }
}
