package com.jmsamples.task;


import com.jmsamples.core.JSoupDocument;
import com.jmsamples.core.WebCrawler;
import com.jmsamples.model.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public Collection<Page> execute(String url, int maxDepth) {

        long startTime = System.currentTimeMillis();
        WebCrawler crawler = new WebCrawler(new JSoupDocument());
        long endTime = System.currentTimeMillis();

        HashSet<Page> initialPage = new HashSet<>();
        initialPage.add(new Page(url, 0));
        Collection<Page> pageCol = crawler.visit(initialPage, maxDepth);
        LOG.debug("pageCol is: " + pageCol);

        LOG.debug("Time taken: " + (endTime - startTime) + " milliseconds");

        return pageCol;

    }

    @Async
    public void executeAsync(String url, int depth) {
        execute(url, depth);
    }
}
