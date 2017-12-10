package com.matangolan.pipelines.pipeline;


import com.matangolan.pipelines.core.ITask;

/**
 * Interface that represents a Pipeline of {@link ITask} objects.
 * The pipeline get two generic type that represents the initial input class type and the final output class type.
 * @param <FIN> The type of the input object.
 * @param <FOUT> The type of the output object.
 */
public interface IPipeline<FIN, FOUT> {

    /**
     * Appends a new task to be run after the last one in the {@link IPipeline} list.
     * @param task the task to append.
     * @param <TOUT> the output type of the provided task.
     * @return Anew {@link IPipeline} object with the provided task added to it.
     */
    <TOUT> IPipeline<FIN, TOUT> append(ITask<FOUT, TOUT> task);

    /**
     * This method will run the entire {@link IPipeline} (each {@link ITask} in it) and will return the final result.
     * @param input the input for the first {@link ITask}.
     * @return the output of the entire {@link IPipeline}
     */
    FOUT run(FIN input);

    default String getName(){
        return this.getClass().getName();
    }
}
