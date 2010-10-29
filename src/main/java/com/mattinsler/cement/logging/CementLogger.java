package com.mattinsler.cement.logging;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Sep 21, 2010
 * Time: 12:12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CementLogger {
    void debug(String message);
    void debug(Throwable throwable);
    void debug(String message, Throwable throwable);

    void info(String message);
    void info(Throwable throwable);
    void info(String message, Throwable throwable);

    void warn(String message);
    void warn(Throwable throwable);
    void warn(String message, Throwable throwable);

    void error(String message);
    void error(Throwable throwable);
    void error(String message, Throwable throwable);

    void fatal(String message);
    void fatal(Throwable throwable);
    void fatal(String message, Throwable throwable);
}
