package com.matangolan.pipelines.flow;


import com.matangolan.pipelines.core.ITaskStateListener;
import com.matangolan.pipelines.logging.LogFactory;
import com.matangolan.pipelines.logging.Logger;
import com.matangolan.pipelines.tasks.ParallelWorkerRunnable;
import com.matangolan.pipelines.tasks.TrackableTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matan on 08-Feb-17.
 * .
 */
public class EtlParallelLoopTask<IN, OUT> extends TrackableTask<List<IN>,List<OUT>> implements ITaskStateListener {

    private final static Logger logger = LogFactory.get(EtlParallelLoopTask.class);

    private IEtlFlowLoadNext<IN, OUT> task;
    private ExecutorService executorService;

    public EtlParallelLoopTask(int parallelism, IEtlFlowLoadNext<IN, OUT> task) {
        this.task = task;
        this.executorService = Executors.newFixedThreadPool(parallelism);
    }

    @Override
    public void onStarted() {}

    @Override
    public void onCompleted() {
        this.incrementProgress();
    }

    @Override
    public List<OUT> run(List<IN> input) {

        if(null!=input){

            this.setInputSize(input.size());

            input.forEach(item-> this.getExecutorService().execute(
                    new ParallelWorkerRunnable<>(this.getTask(), item, this) //Create new runnable to wrap the task
            ));

            //Stop receiving new tasks and shutdown the executor service:
            this.getExecutorService().shutdown();

            //Await for all tasks in the executor to finish:
            try {
                this.getExecutorService().awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                logger.error("Task execution was interrupted, Failed with exception", e);
            }

        }
        else{
            logger.error("The input collection was null, cannot process null input. Returning null.");
            return null;
        }

        return null;
    }

    protected ExecutorService getExecutorService() {
        return executorService;
    }

    public IEtlFlowLoadNext<IN, OUT> getTask() {
        return task;
    }
}
