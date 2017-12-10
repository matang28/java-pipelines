package com.matangolan.pipelines.flow;


import com.matangolan.pipelines.DummyTasksUtils;
import com.matangolan.pipelines.pipeline.Pipeline;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public class EtlFlowTest {


    @Test
    public void test_etl_flow_simple_with_tasks() throws Exception{

        //Arrange:
        Integer input = 1;


        //Act:
        Integer result = EtlFlow
                .extract(
                new DummyTasksUtils.AddOneTask()

                ).transform(
                new DummyTasksUtils.AddOneTask()

                ).load(
                new DummyTasksUtils.AddOneTask()

                ).run(input);

        //Assert:
        assertNotNull(result);
        assertEquals(input+1+1+1, (int)result);

    }

    @Test
    public void test_etl_flow_simple_with_pipelines() throws Exception{

        //Arrange:
        Integer input = 10;
        String expectedResult = String.valueOf(
                ((((input+1+1+1) * 10) * 10) * 10) + 1
        );

        //Act:
        String result = EtlFlow
                .extract(
                        Pipeline.start(
                                new DummyTasksUtils.AddOneTask())
                        .append(new DummyTasksUtils.AddOneTask())
                        .append(new DummyTasksUtils.AddOneTask())

                ).transform(
                        Pipeline.start(
                                new DummyTasksUtils.MultipleByTenTask())
                                .append(new DummyTasksUtils.MultipleByTenTask())
                                .append(new DummyTasksUtils.MultipleByTenTask())

                ).load(
                        Pipeline.start(
                                new DummyTasksUtils.AddOneTask())
                        .append(new DummyTasksUtils.IntegerToStringTask())

                ).run(input);

        //Assert:
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

}