package com.matangolan.pipelines.tasks;


import com.matangolan.pipelines.core.ITask;
import com.matangolan.pipelines.utils.ListUtils;

import java.util.List;

/**
 * Created by Matan on 22-Nov-16.
 * .
 */
public class PartitionListTask<T> implements ITask<List<T>, List<List<T>>> {

    private Integer partitionSize = 0;

    public PartitionListTask(Integer partitionSize) {
        this.partitionSize = partitionSize;
    }

    @Override
    public List<List<T>> run(List<T> input) {
        return ListUtils.partitionList(input, partitionSize);
    }
}
