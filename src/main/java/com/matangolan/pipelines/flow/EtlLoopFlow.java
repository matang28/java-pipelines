package com.matangolan.pipelines.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matan on 08-Feb-17.
 * .
 */
public class EtlLoopFlow<IN, OUT> implements IEtlLoopFlowNext<IN,OUT>, IEtlLoopFlowInputNext<OUT> {

    protected IEtlFlowLoadNext<IN,OUT> loopFlow;
    protected List<IN> inputList;

    public EtlLoopFlow(){}

    public static <IN,OUT> IEtlLoopFlowNext<IN,OUT> doLoopOn(IEtlFlowLoadNext<IN, OUT> etlFlow){

        EtlLoopFlow<IN, OUT> result = new EtlLoopFlow<>();

        result.loopFlow = etlFlow;

        return result;
    }

    @Override
    public IEtlLoopFlowInputNext<OUT> forInput(List<IN> inputList) {
        this.inputList = inputList;

        return this;
    }

    @Override
    public List<OUT> run() {

        List<OUT> result = new ArrayList<>();

        for (IN input : this.inputList){
            result.add(this.loopFlow.run(input));
        }

        return result;
    }
}
