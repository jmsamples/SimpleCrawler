package com.jmsamples.task;

import com.jmsamples.model.Page;

import java.util.Collection;

public interface TaskService {

    Collection<Page> execute(String url, int depth);

    void executeAsync(String url, int depth);

}
