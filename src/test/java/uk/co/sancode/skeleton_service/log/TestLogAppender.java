package uk.co.sancode.skeleton_service.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestLogAppender extends AppenderBase<ILoggingEvent> {
    private static final String CONSOLE_APPENDER = "CONSOLE";
    private static List<ILoggingEvent> logList = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent loggingEvent) {
        logList.add(loggingEvent);
    }

    public static ILoggingEvent getLog(int index) {
        return logList.size() > index ? logList.get(index) : null;
    }

    public static List<ILoggingEvent> getLogs() {
        return logList;
    }

    public static int getLogCount() {
        return logList.size();
    }

    public static boolean hasLog(Level level) {
        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static boolean hasLog(String logMessage, Level level) {
        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> x.getFormattedMessage().equals(logMessage)
                        && x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static boolean hasLogContainingInEncodedMessage(String logMessage, Level level) {
        var rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        var appender = (ConsoleAppender<ILoggingEvent>) rootLogger.getAppender(CONSOLE_APPENDER);
        var encoder = (PatternLayoutEncoder) appender.getEncoder();

        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> new String(encoder.encode(x)).contains(logMessage)
                        && x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static boolean hasLogContaining(String logMessage, Level level) {
        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> x.getFormattedMessage().contains(logMessage)
                        && x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static boolean hasLogMatchingRegex(String pattern, Level level) {
        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> x.getFormattedMessage().matches(pattern)
                        && x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static boolean hasLogger(String loggerName, Level level) {
        Optional<ILoggingEvent> log = logList.
                stream()
                .filter(x -> x.getLoggerName().contains(loggerName)
                        && x.getLevel() == level)
                .findFirst();
        return log.isPresent();
    }

    public static int logCountContaining(String logMessage, Level level) {
        return (int) logList.
                stream()
                .filter(x -> x.getFormattedMessage().contains(logMessage)
                        && x.getLevel() == level)
                .count();
    }

    public static void reset() {
        logList.clear();
    }
}