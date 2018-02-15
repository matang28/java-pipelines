package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.DummyTasksUtils;
import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.pipeline.Pipeline;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public class ParallelTaskTest {

    public class DummyClass extends ParallelTask<Integer, Integer> {

        public DummyClass(int parallelism) {
            super(parallelism);
        }

        @Override
        protected ITask<Integer, Integer> getTask() {
            return new DummyTasksUtils.AddOneTask();
        }
    }

    public class DummyClass2 extends ParallelTask<Integer, Integer> {

        public DummyClass2(int parallelism) {
            super(parallelism);
        }

        @Override
        protected ITask<Integer, Integer> getTask() {
            return new DummyTasksUtils.MultipleByTenTask();
        }
    }


    @Test
    public void test_single_threaded() throws Exception {

        //Arrange:
        Integer items = 100;
        Integer parallelism = 1;
        List<Integer> inputList = createList(items);
        DummyClass dummyClass = new DummyClass(parallelism);

        //Act:
        List<Integer> result = dummyClass.run(inputList);

        //Assert:
        assertNotNull(result);
        assertEquals((int)items, result.size());
        inputList.forEach(item-> assertTrue(result.contains(item+1)));

    }

    @Test
    public void test_multi_threaded() throws Exception {

        //Arrange:
        Integer items = 100;
        Integer parallelism = 4;
        List<Integer> inputList = createList(items);
        DummyClass dummyClass = new DummyClass(parallelism);

        //Act:
        List<Integer> result = dummyClass.run(inputList);

        //Assert:
        assertNotNull(result);
        assertEquals((int)items, result.size());
        inputList.forEach(item-> assertTrue(result.contains(item+1)));

    }

    @Test
    public void test_multi_threaded_progress() throws Exception {

        //Arrange:
        Integer items = 100;
        Integer parallelism = 4;
        List<Integer> inputList = createList(items);
        DummyClass dummyClass = new DummyClass(parallelism);

        BlockingQueue<Float> progress = new LinkedBlockingQueue<>();
        dummyClass.setProgressListener((current, overall) -> {
            try {
                progress.put(current);
            }
            catch (InterruptedException e) {
                fail();
            }
        });

        //Act:
        List<Integer> result = dummyClass.run(inputList);
        List<Float> progressResult = new ArrayList<>();
        progress.drainTo(progressResult);

        //Assert:
        assertNotNull(result);
        assertEquals((int)items, result.size());
        inputList.forEach(item-> assertTrue(result.contains(item+1)));

        assertNotNull(progressResult);
        assertEquals((int)items, progressResult.size());

        //Assert sorted:
        Float last = -1000f;
        for(Float i : progressResult){
            assertTrue(i>last);
            last = i;
        }
    }

    @Test
    public void test_multi_threaded_with_pipeline_same_parallelism() throws Exception{

        //Arrange:
        Integer items = 100;
        Integer parallelism = 4;
        List<Integer> inputList = createList(items);
        DummyClass dummyClass = new DummyClass(parallelism);
        DummyClass2 dummyClass2 = new DummyClass2(parallelism);

        //Act:
        List<Integer> result = Pipeline.start(dummyClass).append(dummyClass2).run(inputList);

        //Assert:
        assertNotNull(result);
        assertEquals((int)items, result.size());
        inputList.forEach(item-> assertTrue(result.contains((item+1)*10)));

    }

    @Test
    public void test_multi_threaded_with_pipeline_diff_parallelism() throws Exception{

        //Arrange:
        Integer items = 100;
        Integer parallelism1 = 1;
        Integer parallelism2 = 3;
        List<Integer> inputList = createList(items);
        DummyClass dummyClass = new DummyClass(parallelism1);
        DummyClass2 dummyClass2 = new DummyClass2(parallelism2);

        //Act:
        List<Integer> result = Pipeline.start(dummyClass).append(dummyClass2).run(inputList);

        //Assert:
        assertNotNull(result);
        assertEquals((int)items, result.size());
        inputList.forEach(item-> assertTrue(result.contains((item+1)*10)));

    }

    @Test
    public void test_multi_threaded_with_zero_input() throws Exception{

        //Arrange:
        Integer parallelism = 10;
        List<Integer> inputList = new ArrayList<>();
        DummyClass dummyClass = new DummyClass(parallelism);

        //Act:
        List<Integer> result = Pipeline.start(dummyClass).run(inputList);

        //Assert:
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    private static List<Integer> createList(int items){
        List<Integer> list = new ArrayList<>();

        for (int i=0;i<items;i++){
            list.add(i);
        }

        return list;
    }

}