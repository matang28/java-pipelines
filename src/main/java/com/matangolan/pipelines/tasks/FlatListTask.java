package com.matangolan.pipelines.tasks;

import com.matangolan.pipelines.core.ITask;

import java.util.LinkedList;
import java.util.List;

/**
 * This task takes a list that contains lists of the same type and flatten it to one big list.
 */
public class FlatListTask<T> implements ITask<List<List<T>>, List<T>> {
    @Override
    public List<T> run(List<List<T>> input) {

        LinkedList<T> linkedList = new LinkedList<>();

        for(List<T> item  : input){
            linkedList.addAll(item);
        }

        input = null;

        return linkedList;
    }
}
