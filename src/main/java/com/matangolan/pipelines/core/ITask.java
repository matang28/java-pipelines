package com.matangolan.pipelines.core;

/**
 * An interface that abstracts a "Task" (e.g function).
 * a "Task" gets an input, process it and returns an output.
 * @param <IN>
 * @param <OUT>
 */
public interface ITask<IN, OUT> {

    /**
     * Runs the task on the provided input and return the result of the execution.
     * @param input the input of the task.
     * @return the output of the task.
     */
    OUT run(IN input);

    default String getName(){
        return this.getClass().getName();
    }

}
