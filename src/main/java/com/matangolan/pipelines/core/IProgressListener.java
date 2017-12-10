package com.matangolan.pipelines.core;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public interface IProgressListener {
    void onProgress(Float current, Float overall);
}
