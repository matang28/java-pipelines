package com.matangolan.pipelines.core;

/**
 * Created by Matan on 20-Nov-16.
 * .
 */
public interface ITaskStateListener {

    void onStarted();
    void onCompleted();

}
