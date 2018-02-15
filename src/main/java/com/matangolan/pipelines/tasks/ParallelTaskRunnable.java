package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.core.ITaskStateListener;
import com.matangolan.pipelines.logging.LogFactory;
import com.matangolan.pipelines.logging.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * This class is a wrapper to Java's {@link Runnable} to work with {@link ParallelTaskRunnable}
 */
public class ParallelTaskRunnable<IN, OUT> implements Runnable{

    private Logger logger = LogFactory.get(ParallelTask.class);

    private final ITask<IN, OUT> task;
    private final BlockingQueue<OUT> queue;
    private final IN input;
    private final ITaskStateListener taskListener;
    private final CountDownLatch latch;

    public ParallelTaskRunnable(ITask<IN, OUT> task, IN input, BlockingQueue<OUT> queue, final CountDownLatch latch) {
        this.task = task;
        this.queue = queue;
        this.input = input;
        this.taskListener = null;
        this.latch = latch;
    }

    public ParallelTaskRunnable(ITask<IN, OUT> task, IN input, BlockingQueue<OUT> queue, ITaskStateListener taskListener, final CountDownLatch latch) {
        this.task = task;
        this.queue = queue;
        this.input = input;
        this.taskListener = taskListener;
        this.latch = latch;
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

        try {
            notifyStart();

            //Run the task:
            OUT result = this.getTask().wrappedRun(this.getInput());

            //Publish the result to the result queue:
            this.getQueue().put(result);
        }
        catch (Exception e) {
            logger.error("Cannot complete task execution. Failed with exception.", e);
        }

        this.latch.countDown();

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
