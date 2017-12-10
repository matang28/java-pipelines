package com.matangolan.pipelines.logging;

/**
 * Created by matan on 27/08/2017.
 */
public class LogFactory {

    public static Logger get(Class cls){
        return get(cls.getSimpleName());
    }

    public static Logger get(String contextName){
        return new StdLogger(contextName);
    }

}
