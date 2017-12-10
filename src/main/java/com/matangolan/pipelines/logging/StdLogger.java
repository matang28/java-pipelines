package com.matangolan.pipelines.logging;

import java.text.MessageFormat;
import java.time.LocalDateTime;

/**
 * Created by matan on 27/08/2017.
 */
public class StdLogger implements Logger{

    private String context;

    public StdLogger(String context) {
        this.context = context;
    }

    @Override
    public void debug(String message, Object... params) {
        print("DEBUG", this.context, null, message, params);
    }

    @Override
    public void debug(Throwable throwable, String message, Object... params) {
        print("DEBUG", this.context, throwable, message, params);
    }

    @Override
    public void info(String message, Object... params) {
        print("INFO", this.context, null, message, params);
    }

    @Override
    public void info(Throwable throwable, String message, Object... params) {
        print("INFO", this.context, throwable, message, params);
    }

    @Override
    public void warn(String message, Object... params) {
        print("WARNING", this.context, null, message, params);
    }

    @Override
    public void warn(Throwable throwable, String message, Object... params) {
        print("WARNING", this.context, throwable, message, params);
    }

    @Override
    public void error(String message, Object... params) {
        print("ERROR", this.context, null, message, params);
    }

    @Override
    public void error(Throwable throwable, String message, Object... params) {
        print("ERROR", this.context, throwable, message, params);
    }

    @Override
    public void fatal(String message, Object... params) {
        print("FATAL", this.context, null, message, params);
    }

    @Override
    public void fatal(Throwable throwable, String message, Object... params) {
        print("FATAL", this.context, throwable, message, params);
    }

    private static void print(String level, String context, Throwable throwable, String message, Object... params){

        System.out.println(
                MessageFormat.format("{0} - {1} - {2} -> {3}", level, context, LocalDateTime.now().toString(), MessageFormat.format(message, params))
        );

        if(throwable!=null)
            throwable.printStackTrace();
    }
}
