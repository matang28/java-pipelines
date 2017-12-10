package com.matangolan.pipelines.flow;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.pipeline.IPipeline;
import com.matangolan.pipelines.pipeline.Pipeline;

import java.util.LinkedList;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public class EtlFlow<FIN, FOUT> implements IEtlFlowExtractNext<FIN, FOUT>, IEtlFlowTransformNext<FIN, FOUT>, IEtlFlowLoadNext<FIN, FOUT> {

    private LinkedList<IPipeline<?,?>> pipelines;

    private EtlFlow(){}

    public static <TIN, TOUT> IEtlFlowExtractNext<TIN, TOUT> extract(IPipeline<TIN, TOUT> pipeline){
        EtlFlow<TIN, TOUT> flow = new EtlFlow<>();

        flow.pipelines = new LinkedList<>();

        flow.pipelines.addFirst(pipeline);

        return flow;
    }

    public static <TIN, TOUT> IEtlFlowExtractNext<TIN, TOUT> extract(ITask<TIN, TOUT> task){
        return extract(Pipeline.start(task));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TOUT> IEtlFlowTransformNext<FIN, TOUT> transform(IPipeline<FOUT, TOUT> pipeline) {
        EtlFlow<FIN, FOUT> flow = new EtlFlow<>();

        flow.pipelines = this.pipelines;

        flow.pipelines.addLast(pipeline);

        return (IEtlFlowTransformNext<FIN, TOUT>) flow;
    }

    @Override
    public <TOUT> IEtlFlowTransformNext<FIN, TOUT> transform(ITask<FOUT, TOUT> task) {
        return this.transform(Pipeline.start(task));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TOUT> IEtlFlowLoadNext<FIN, TOUT> load(IPipeline<FOUT, TOUT> pipeline) {
        EtlFlow<FIN, FOUT> flow = new EtlFlow<>();

        flow.pipelines = this.pipelines;

        flow.pipelines.addLast(pipeline);

        return (IEtlFlowLoadNext<FIN, TOUT>) flow;
    }

    @Override
    public <TOUT> IEtlFlowLoadNext<FIN, TOUT> load(ITask<FOUT, TOUT> task) {
        return this.load(Pipeline.start(task));
    }


    @SuppressWarnings("unchecked")
    @Override
    public FOUT run(FIN input) {
        //Declare Java's objects to hold each step's input/output parameter:
        Object in = input;
        Object out = null;

        //For each pipeline in the pipelines list:
        for(IPipeline p : pipelines){

            //Run the task and save the output of it:
            out = p.run(in);


            //The input of the next pipeline is the output of the current one:
            in = out;
        }

        //Return the final result (this is safe because the append method is type-safe)
        return (FOUT) out;
    }
}
