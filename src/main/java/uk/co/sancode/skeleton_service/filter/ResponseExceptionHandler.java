package uk.co.sancode.skeleton_service.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static uk.co.sancode.skeleton_service.log.LogCategory.VALIDATION;

@ControllerAdvice
public class ResponseExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Object handle(final HttpServletRequest request, final Exception exception, final Model model) {
        if (isViolationException(exception)) {
            String message = String.format("%s %s", VALIDATION, exception.getMessage());
            logger.error(message, exception);
        } else {
            logger.error(exception.getMessage(), exception);
        }

        return ResponseEntity.status(extractErrorStatus(exception)).body(extractReason(exception));
    }

    private HttpStatus extractErrorStatus(final Exception exception) {
        if (isViolationException(exception)) {
            return BAD_REQUEST;
        }

        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        return annotation == null ? HttpStatus.INTERNAL_SERVER_ERROR : annotation.value();
    }

    private String extractReason(final Exception exception) {
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        return annotation == null ? null : annotation.reason();
    }

    private boolean isViolationException(final Exception exception) {
        return exception instanceof ConstraintViolationException
                || exception instanceof HttpMessageNotReadableException;
    }
}
