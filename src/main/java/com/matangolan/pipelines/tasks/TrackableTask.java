package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.IProgressListener;
import com.matangolan.pipelines.core.ITask;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public abstract class TrackableTask<IN, OUT> implements ITask<IN, OUT> {
    private static final DecimalFormat formatting = new DecimalFormat("#.##");
    private Integer inputSize = 1;
    private AtomicInteger progress = new AtomicInteger(0);

    private IProgressListener progressListener;

    protected synchronized void incrementProgress(){
        //Increment the current progress:
        Integer progress = this.progress.incrementAndGet();

        //Notify the listener:
        if(this.getProgressListener()!=null){
            this.getProgressListener().onProgress(getProgressPercentage(progress), getProgressPercentage(progress));
        }
    }

    protected Integer getInputSize() {
        return inputSize;
    }

    protected void setInputSize(Integer inputSize) {
        this.inputSize = inputSize;
    }

    protected Integer getCurrentProgress(){
        return this.progress.get();
    }

    protected Float getProgressPercentage(Integer progress){
        return Float.valueOf(formatting.format((float)progress / this.getInputSize()*100f));
    }

    public IProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(IProgressListener progressListener) {
        this.progressListener = progressListener;
    }
}
