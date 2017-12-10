package com.matangolan.pipelines;

import com.matangolan.pipelines.core.ITask;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public class DummyTasksUtils {

    public static class AddOneTask implements ITask<Integer, Integer> {
        @Override
        public Integer run(Integer input) {
            return input+1;
        }
    }

    public static class MultipleByTenTask implements ITask<Integer, Integer> {
        @Override
        public Integer run(Integer input) {
            return input*10;
        }
    }

    public static class IntegerToStringTask implements ITask<Integer, String> {
        @Override
        public String run(Integer input) {
            if(null!=input){
                return String.valueOf(input);
            }
            return null;
        }
    }

    public static class ExceptionThrowingTask implements ITask<Integer, Integer> {

        @Override
        public Integer run(Integer input) {
            throw new NullPointerException("Just a dummy exception");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> generify(Class<?> cls) {
        return (Class<T>)cls;
    }

}
