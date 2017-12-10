package com.matangolan.pipelines.utils;

import java.util.Iterator;

/**
 * Created by Matan on 31-Jul-16.

 */
public class BatchIterator implements Iterator<Integer> {

    private Integer start;
    private Integer end;
    private Integer batchSize;

    private Integer current = 0;

    private Boolean isFirst = true;

    public BatchIterator(Integer start, Integer end, Integer batchSize) {
        this.start = start;
        this.end = end;
        this.batchSize = batchSize;
    }

    @Override
    public boolean hasNext() {

        if(this.current>=this.end)
            return false;

        return true;
    }

    @Override
    public Integer next() {

        if(this.hasNext()){

            if(this.isFirst){
                isFirst = false;
                this.current = this.start;
                return this.start;
            }

            Integer temp = Math.min(this.current + this.batchSize, this.end);
            this.current = temp;
            return temp;
        }

        return null;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public Integer getBatchSize() {
        return batchSize;
    }
}
