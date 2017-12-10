package com.matangolan.pipelines.flow;

import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.tasks.EmptyTask;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Matan on 08-Feb-17.
 * .
 */
public class EtlLoopFlowTest {

    @Test
    public void test_loop_flow_add_one() throws Exception{

        //Arrange:
        Integer inputSize = 10000;
        List<Integer> loopList = new ArrayList<>();
        for(int i=0;i<inputSize;i++){
            loopList.add(i);
        }

        //Act:
        List<Integer> result = EtlLoopFlow.doLoopOn(
                EtlFlow
                        .extract(new EmptyTask<Integer>())
                        .transform(new AddOneTask())
                        .load(new EmptyTask<>())
        )
                .forInput(loopList)
                .run();

        //Assert:
        assertNotNull(result);
        assertEquals((int)inputSize, result.size());
        AddOneTask task = new AddOneTask();

        for(int i=0;i<inputSize;i++){
            assertEquals(task.run(loopList.get(i)), result.get(i));
        }
    }


}

class AddOneTask implements ITask<Integer, Integer> {

    @Override
    public Integer run(Integer input) {
        return input+1;
    }
}