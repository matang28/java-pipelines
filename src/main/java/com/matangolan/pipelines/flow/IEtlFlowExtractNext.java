package com.matangolan.pipelines.flow;


import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.pipeline.IPipeline;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public interface IEtlFlowExtractNext<FIN, FOUT> {

    /**
     * Creates the transform phase of the ETL.
     * @param pipeline the pipeline to be executed during this phase.
     * @param <TOUT> the type of the pipeline's output.
     * @return a {@link IEtlFlowTransformNext}
     */
    <TOUT> IEtlFlowTransformNext<FIN, TOUT> transform(IPipeline<FOUT, TOUT> pipeline);

    /**
     * Creates the transform phase of the ETL.
     * @param task the task to be executed during this phase.
     * @param <TOUT> the type of the task's output.
     * @return a {@link IEtlFlowTransformNext}
     */
    <TOUT> IEtlFlowTransformNext<FIN, TOUT> transform(ITask<FOUT, TOUT> task);

}
