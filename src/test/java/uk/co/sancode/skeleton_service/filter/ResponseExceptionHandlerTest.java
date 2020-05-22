package uk.co.sancode.skeleton_service.filter;

import ch.qos.logback.classic.Level;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import uk.co.sancode.skeleton_service.controller.DuplicateRecordException;
import uk.co.sancode.skeleton_service.log.TestLogAppender;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static uk.co.sancode.skeleton_service.log.LogCategory.VALIDATION;

@RunWith(JUnitParamsRunner.class)
public class ResponseExceptionHandlerTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private Exception mockException;

    @Mock
    private Model mockModel;

    @Test
    public void handle_handlesAllExceptions() throws NoSuchMethodException {
        // Setup

        // Exercise

        var handleMethod = ResponseExceptionHandler.class.getMethod("handle", HttpServletRequest.class, Exception.class, Model.class);
        var annotation = AnnotationUtils.getAnnotation(handleMethod, ExceptionHandler.class);

        // Verify

        assertNotNull(annotation);
        var values = annotation.value();
        assertEquals(1, values.length);
        assertEquals(Exception.class, values[0]);
    }

    @Test
    @Parameters(method = "parametersForApiEndpointsExceptions")
    public void givenApiEndpoint_andExceptionIsThrown_handle_logsAndReturnsStatusCode(Exception exception, HttpStatus expectedStatus) {
        // Setup

        var mockUrl = "mock url";
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(mockUrl));
        var sut = generateSut();
        TestLogAppender.reset();

        // Exercise

        var actual = (ResponseEntity) sut.handle(mockRequest, exception, mockModel);

        // Verify

        assertEquals(expectedStatus, actual.getStatusCode());
        assertEquals(extractReason(exception.getClass()), actual.getBody());
        assertTrue(exception.getMessage() == null ? TestLogAppender.hasLog(Level.ERROR) : TestLogAppender.hasLog(exception.getMessage(), Level.ERROR));
    }

    @Test
    @Parameters(method = "getValidationExceptions")
    public void givenApiEndpointAndValidationExceptions_handle_logsWithValidationAndReturnsResponseEntity(Class<Exception> exceptionClass) {
        // Setup

        var mockUrl = "mock url";
        when(mockRequest.getRequestURL()).thenReturn(new StringBuffer(mockUrl));
        var mockMessage = "mock message";
        mockException = mock(exceptionClass);
        when(mockException.getMessage()).thenReturn(mockMessage);
        var sut = generateSut();
        TestLogAppender.reset();

        // Exercise

        var actual = (ResponseEntity) sut.handle(mockRequest, mockException, mockModel);

        // Verify

        assertEquals(BAD_REQUEST, actual.getStatusCode());
        assertEquals(extractReason(exceptionClass), actual.getBody());
        assertTrue(TestLogAppender.hasLog(String.format("%s %s", VALIDATION, mockMessage), Level.ERROR));
    }

    private Object[] parametersForApiEndpointsExceptions() {
        return new Object[][]{
                {new DuplicateRecordException(), CONFLICT},
                {new ValidationException("validation error"), HttpStatus.INTERNAL_SERVER_ERROR}
        };
    }

    private Object[] getValidationExceptions() {
        return new Object[]{ConstraintViolationException.class, HttpMessageNotReadableException.class};
    }

    private <T> String extractReason(final Class<T> exceptionClass) {
        ResponseStatus annotation = exceptionClass.getAnnotation(ResponseStatus.class);
        return annotation == null ? null : annotation.reason();
    }

    private ResponseExceptionHandler generateSut() {
        return new ResponseExceptionHandler();
    }

}
