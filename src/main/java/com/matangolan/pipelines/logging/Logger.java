package com.matangolan.pipelines.logging;

/**
 * Created by matan on 27/08/2017.
 */
public interface Logger {

    void debug(String message, Object... params);

    void debug(Throwable throwable, String message, Object... params);

    void info(String message, Object... params);

    void info(Throwable throwable, String message, Object... params);

    void warn(String message, Object... params);

    void warn(Throwable throwable, String message, Object... params);

    void error(String message, Object... params);

    void error(Throwable throwable, String message, Object... params);

    void fatal(String message, Object... params);

    void fatal(Throwable throwable, String message, Object... params);

}
