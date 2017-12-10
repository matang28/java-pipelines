package com.matangolan.pipelines.flow;

import java.util.List;

/**
 * Created by Matan on 08-Feb-17.
 * .
 */
public interface IEtlLoopFlowNext<IN, OUT> {
    IEtlLoopFlowInputNext<OUT> forInput(List<IN> inputList);
}
