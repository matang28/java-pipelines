package com.matangolan.pipelines.pipeline;

import com.matangolan.pipelines.DummyTasksUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public class PipelineTest {

    @Test
    public void test_PipelineFlow_single_item() throws Exception{

        //Arrange:
        Integer input = 1;
        DummyTasksUtils.AddOneTask task1 = new DummyTasksUtils.AddOneTask();

        //Act:
        Integer result = Pipeline.start(task1).run(input);

        //Assert:
        assertNotNull(result);
        assertEquals(input + 1, (int)result);
    }

    @Test
    public void test_PipelineFlow_multiple_items() throws Exception{

        //Arrange:
        Integer input = 10;
        DummyTasksUtils.AddOneTask task1 = new DummyTasksUtils.AddOneTask();
        DummyTasksUtils.MultipleByTenTask task2 = new DummyTasksUtils.MultipleByTenTask();
        DummyTasksUtils.IntegerToStringTask task3 = new DummyTasksUtils.IntegerToStringTask();

        //Act:
        String result = Pipeline.start(task1).append(task2).append(task3).run(input);

        //Assert:
        assertNotNull(result);
        assertEquals(String.valueOf((input + 1)*10), result);
    }

    @Test
    public void test_PipelineFlow_item_throw_exception() throws Exception{

        Boolean isThrown = false;

        //Arrange:
        Integer input = 10;
        DummyTasksUtils.AddOneTask task1 = new DummyTasksUtils.AddOneTask();
        DummyTasksUtils.MultipleByTenTask task2 = new DummyTasksUtils.MultipleByTenTask();
        DummyTasksUtils.ExceptionThrowingTask task3 = new DummyTasksUtils.ExceptionThrowingTask();

        //Act:
        try{
            Integer result = Pipeline.start(task1).append(task2).append(task3).run(input);
        }
        catch (Exception e){
            if(e instanceof NullPointerException)
                isThrown = true;
        }

        //Assert:
        assertTrue(isThrown);
    }

}
