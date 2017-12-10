package com.matangolan.pipelines.pipeline;

import com.matangolan.pipelines.core.ITask;

import java.util.LinkedList;

/**
 * Class that represents a Pipeline of {@link ITask} objects.
 * The pipeline get two generic type that represents the initial input class type and the final output class type.
 * The only way to instantiate it is to use the static start method that indicates the first task in the pipeline
 * afterwards you can use the append method to add more tasks that need to be executed (Builder pattern).
 * The {@link Pipeline} uses a {@link LinkedList} to save the individual {@link ITask} objects.
 * @param <FIN> The type of the input object.
 * @param <FOUT> The type of the output object.
 */
public class Pipeline<FIN,FOUT> implements IPipeline<FIN, FOUT> {

    private LinkedList<ITask<?,?>> tasks; //A linked list containing all the individual task to be run.

    //No one can instantiate this class.
    private Pipeline(){}

    /**
     * Creates a new {@link IPipeline} object with the first task to be executed.
     * @param firstTask the first task to be executed.
     * @param <TIN> the type of first task's input.
     * @param <TOUT>  the type of first task's output.
     * @return A new {@link IPipeline} object.
     */
    public static <TIN,TOUT> IPipeline<TIN,TOUT> start(ITask<TIN,TOUT> firstTask){
        //Create a new pipeline object:
        Pipeline<TIN,TOUT> pipeline = new Pipeline<>();

        //Instantiate the tasks list:
        pipeline.tasks = new LinkedList<>();

        //Add the provided task as the first one in the list.
        pipeline.tasks.addFirst(firstTask);

        //Return the new pipeline:
        return pipeline;
    }

    /**
     * Appends a new task to be run after the last one in the {@link IPipeline} list.
     * @param task the task to append.
     * @param <TOUT> the output type of the provided task.
     * @return Anew {@link IPipeline} object with the provided task added to it.
     */
    public <TOUT> IPipeline<FIN, TOUT> append(ITask<FOUT,TOUT> task){

        //Create a new pipeline:
        Pipeline<FIN, TOUT> pipeline = new Pipeline<>();

        //Copy the contents of the current pipeline to the new one:
        pipeline.tasks = tasks;

        //Add the provided task to the end of the tasks list.
        pipeline.tasks.addLast(task);

        //Return the new pipeline:
        return pipeline;
    }

    /**
     * This method will run the entire {@link IPipeline} (each {@link ITask} in it) and will return the final result.
     * @param input the input for the first {@link ITask}.
     * @return the output of the entire {@link IPipeline}
     */
    @SuppressWarnings("unchecked")
    public FOUT run(FIN input){

        //Declare Java's objects to hold each step's input/output parameter:
        Object in = input;
        Object out = null;

        //For each task in the tasks list:
        for(ITask t : tasks){

            //Run the task and save the output of it:
            out = t.run(in);

            //The input of the next task is the output of the current one:
            in = out;
        }

        //Return the final result (this is safe because the append method is type-safe)
        return (FOUT) out;
    }

}
