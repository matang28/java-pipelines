package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.core.ITaskStateListener;
import com.matangolan.pipelines.logging.LogFactory;
import com.matangolan.pipelines.logging.Logger;

import java.util.concurrent.BlockingQueue;

/**
 * This class is a wrapper to Java's {@link Runnable} to work with {@link ParallelTaskRunnable}
 */
public class ParallelTaskRunnable<IN, OUT> implements Runnable{

    private Logger logger = LogFactory.get(ParallelTask.class);

    private ITask<IN, OUT> task;
    private BlockingQueue<OUT> queue;
    private IN input;
    private ITaskStateListener taskListener;

    public ParallelTaskRunnable(ITask<IN, OUT> task, IN input, BlockingQueue<OUT> queue) {
        this.task = task;
        this.queue = queue;
        this.input = input;
    }

    public ParallelTaskRunnable(ITask<IN, OUT> task, IN input, BlockingQueue<OUT> queue, ITaskStateListener taskListener) {
        this.task = task;
        this.queue = queue;
        this.input = input;
        this.taskListener = taskListener;
    }

    private void notifyStart(){
        if(taskListener!=null)
            taskListener.onStarted();
    }

    private void notifyCompleted(){
        if(taskListener!=null)
            taskListener.onCompleted();
    }

    @Override
    public void run() {

        notifyStart();

        try {
            //Run the task:
            OUT result = this.getTask().run(this.getInput());

            //Publish the result to the result queue:
            this.getQueue().put(result);
        }
        catch (InterruptedException e) {
            logger.error("Cannot complete task execution. Failed with exception.", e);
        }

        notifyCompleted();
    }

    public ITask<IN, OUT> getTask() {
        return task;
    }

    public BlockingQueue<OUT> getQueue() {
        return queue;
    }

    public IN getInput() {
        return input;
    }
}
