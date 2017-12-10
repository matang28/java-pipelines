package com.matangolan.pipelines.flow;

import com.matangolan.pipelines.core.IProgressListener;

import java.util.List;

/**
 * Created by Matan on 08-Feb-17.
 * .
 */
public class EtlParallelLoop<IN,OUT> extends EtlLoopFlow<IN,OUT>{

    //The default is num of cpus:
    private Integer parallelism;
    private IProgressListener progressListener = null;

    public static <IN,OUT> IEtlLoopFlowNext<IN,OUT> doLoopOn(Integer parallelism, IEtlFlowLoadNext<IN, OUT> etlFlow){
        return doLoopOn(parallelism, null, etlFlow);
    }

    public static <IN,OUT> IEtlLoopFlowNext<IN,OUT> doLoopOn(Integer parallelism, IProgressListener progressListener, IEtlFlowLoadNext<IN, OUT> etlFlow){

        EtlParallelLoop<IN, OUT> result = new EtlParallelLoop<>();

        result.parallelism = parallelism;

        result.progressListener = progressListener;

        result.loopFlow = etlFlow;

        return result;
    }

    @Override
    public List<OUT> run() {
        EtlParallelLoopTask<IN,OUT> parallelLoopTask = new EtlParallelLoopTask<>(this.parallelism, this.loopFlow);
        parallelLoopTask.setProgressListener(this.progressListener);
        return parallelLoopTask.run(this.inputList);
    }
}
