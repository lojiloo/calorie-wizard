package project.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.errors.exception.ConditionViolatedException;
import project.errors.exception.NoSuchEnumValueException;
import project.errors.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class ExceptionHandlers {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiError notFoundHandler(NotFoundException e) {
        return new ApiError(e.getMessage(),
                LocalDateTime.now().format(formatter),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ApiError conditionViolatedException(ConditionViolatedException e) {
        return new ApiError(e.getMessage(),
                LocalDateTime.now().format(formatter),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiError noSuchEnumValueException(NoSuchEnumValueException e) {
        return new ApiError(e.getMessage(),
                LocalDateTime.now().format(formatter),
                HttpStatus.BAD_REQUEST);
    }
}
