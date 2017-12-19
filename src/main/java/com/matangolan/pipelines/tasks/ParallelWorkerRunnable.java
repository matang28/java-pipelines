package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.core.ITaskStateListener;
import com.matangolan.pipelines.logging.LogFactory;
import com.matangolan.pipelines.logging.Logger;

/**
 * Created by Matan on 14-Feb-17.
 * .
 */
public class ParallelWorkerRunnable<IN> implements Runnable {

    private ITask<IN, ?> task;
    private IN input;
    private ITaskStateListener taskListener;

    private Logger logger = LogFactory.get(ParallelWorkerRunnable.class);

    public ParallelWorkerRunnable(ITask<IN, ?> task, IN input, ITaskStateListener taskListener) {
        this.task = task;
        this.input = input;
        this.taskListener = taskListener;
    }

    @Override
    public void run() {

        notifyStart();

        //Run the task:
        this.getTask().wrappedRun(this.getInput());

        notifyCompleted();
    }

    public IN getInput() {
        return input;
    }

    public ITask<IN, ?> getTask() {
        return task;
    }

    private void notifyStart(){
        if(taskListener!=null)
            taskListener.onStarted();
    }

    private void notifyCompleted(){
        if(taskListener!=null)
            taskListener.onCompleted();
    }
}
