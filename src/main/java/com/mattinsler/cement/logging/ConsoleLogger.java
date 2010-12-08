package com.mattinsler.cement.logging;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: Dec 6, 2010
 * Time: 12:17:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsoleLogger extends AbstractCementLogger {
    @Override
    protected void writeLog(String requestId, Date timestamp, Level level, String message, Throwable throwable) {
        System.out.printf("[%s @ %s] %s - %s\n", level, timestamp, requestId, message);
        if (throwable != null) {
            throwable.printStackTrace(System.err);
        }
    }
}
