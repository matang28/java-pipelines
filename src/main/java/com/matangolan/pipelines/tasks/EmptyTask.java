package com.matangolan.pipelines.tasks;


import com.matangolan.pipelines.core.ITask;

/**
 * The simplest task, it does nothing but returning the value in the input.
 */
public class EmptyTask<IN_OUT> implements ITask<IN_OUT,IN_OUT> {
    @Override
    public IN_OUT run(IN_OUT input) {
        return input;
    }
}
