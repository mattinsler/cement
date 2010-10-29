package com.mattinsler.cement.logging;

import com.google.inject.Inject;
import com.mattinsler.cement.CementRequestInfo;
import com.mattinsler.cement.filter.CementRequestFilter;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 26, 2010
 * Time: 10:56:32 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractCementLogger implements CementLogger {
    public enum Level {
        Debug,
        Info,
        Warn,
        Error,
        Fatal
    }

    private CementRequestFilter _requestFilter;

    @Inject
    void setDependencies(CementRequestFilter requestFilter) {
        _requestFilter = requestFilter;
    }

    protected abstract void writeLog(String requestId, Date timestamp, Level level, String message, Throwable throwable);

    private void log(Level level, String message) {
        log(level, message, null);
    }

    private void log(Level level, Throwable throwable) {
        log(level, null, throwable);
    }

    private void log(Level level, String message, Throwable throwable) {
        CementRequestInfo requestInfo = _requestFilter.getCurrentRequestInfo();
        writeLog(requestInfo.identity, new Date(), level, message, throwable);
    }

    public void debug(String message) {
        log(Level.Debug, message);
    }
    public void debug(Throwable throwable) {
        log(Level.Debug, throwable);
    }
    public void debug(String message, Throwable throwable) {
        log(Level.Debug, message, throwable);
    }

    public void info(String message) {
        log(Level.Info, message);
    }
    public void info(Throwable throwable) {
        log(Level.Info, throwable);
    }
    public void info(String message, Throwable throwable) {
        log(Level.Info, message, throwable);
    }

    public void warn(String message) {
        log(Level.Warn, message);
    }
    public void warn(Throwable throwable) {
        log(Level.Warn, throwable);
    }
    public void warn(String message, Throwable throwable) {
        log(Level.Warn, message, throwable);
    }

    public void error(String message) {
        log(Level.Error, message);
    }
    public void error(Throwable throwable) {
        log(Level.Error, throwable);
    }
    public void error(String message, Throwable throwable) {
        log(Level.Error, message, throwable);
    }

    public void fatal(String message) {
        log(Level.Fatal, message);
    }
    public void fatal(Throwable throwable) {
        log(Level.Fatal, throwable);
    }
    public void fatal(String message, Throwable throwable) {
        log(Level.Fatal, message, throwable);
    }
}
