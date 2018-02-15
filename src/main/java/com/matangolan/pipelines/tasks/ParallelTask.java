package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.core.ITaskStateListener;
import com.matangolan.pipelines.logging.LogFactory;
import com.matangolan.pipelines.logging.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * This is an extension to the normal {@link ITask} class adding the ability to run some
 * task on a list of inputs in a multi-threaded way.
 * The task that should run on each item of the input can be provided using the "getTask()" method and each class
 * extending this one should override it with the required functionality.
 * @param <IN> the type of the input class.
 * @param <OUT> the type of the output class.
 */
public abstract class ParallelTask<IN, OUT> extends TrackableTask<List<IN>,List<OUT>> implements ITaskStateListener {

    private final static Logger logger = LogFactory.get(ParallelTask.class);

    private ExecutorService executorService;
    private BlockingQueue<OUT> blockingQueue;
    private CountDownLatch latch = null;
    private int parallelism = 0;

    public ParallelTask() {
        //New executor with the number of available cpu.
        this.parallelism = Runtime.getRuntime().availableProcessors();

        //New blocking queue:
        blockingQueue = new LinkedBlockingQueue<>();
    }

    public ParallelTask(int parallelism) {
        this.parallelism = parallelism;
        blockingQueue = new LinkedBlockingQueue<>();
    }

    public ParallelTask(BlockingQueue<OUT> blockingQueue) {
        this.parallelism = Runtime.getRuntime().availableProcessors();
        this.blockingQueue = blockingQueue;
    }

    public ParallelTask(int parallelism, BlockingQueue<OUT> blockingQueue) {
        this.parallelism = parallelism;
        this.blockingQueue = blockingQueue;
    }

    /**
     * @return the {@link ITask} to be executed on each input item.
     */
    protected abstract ITask<IN, OUT> getTask();

    @Override
    public List<OUT> run(List<IN> input) {

        if(null != input){

            this.setInputSize(input.size());
            this.latch = new CountDownLatch(input.size());

            //For each item in the input collection:
            input.forEach(item->{
                //Execute the task using the executor service:
                this.getExecutorService().execute(
                        new ParallelTaskRunnable<>(this.getTask(), item, this.getBlockingQueue(), this, latch) //Create new runnable to wrap the task
                );
            });

            //Stop receiving new tasks and shutdown the executor service:
            this.getExecutorService().shutdown();

            //Await for all tasks in the executor to finish:
            try {
                //this.getExecutorService().awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
                this.latch.await(Integer.MAX_VALUE, TimeUnit.SECONDS);
                this.getExecutorService().shutdownNow();
            }
            catch (InterruptedException e) {
                logger.error("Task execution was interrupted, Failed with exception", e);
            }
        }
        else{
            logger.error("The input collection was null, cannot process null input. Returning null.");
            return null;
        }

        //Create new list to hold the results:
        List<OUT> resultList = new LinkedList<>();

        //Drain the queue to the result list:
        this.getBlockingQueue().drainTo(resultList);

        return resultList;
    }

    @Override
    public void onCompleted() {
        this.incrementProgress();
    }

    @Override
    public void onStarted() {
        //Nothing...
    }

    public ExecutorService getExecutorService() {
        if(this.executorService==null || (this.executorService.isTerminated()))
            this.executorService = createExecutor(this.parallelism);

        return executorService;
    }

    private static ExecutorService createExecutor(int parallelism){
        return Executors.newFixedThreadPool(parallelism);
    }

    public BlockingQueue<OUT> getBlockingQueue() {
        return blockingQueue;
    }
}
