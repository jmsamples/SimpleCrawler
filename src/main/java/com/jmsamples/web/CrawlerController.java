package com.jmsamples.web;

import com.jmsamples.model.Page;
import com.jmsamples.task.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@RestController
public class CrawlerController {
    private static final Logger LOG = LoggerFactory.getLogger(CrawlerController.class);
    private static final String CONST_DEFAULT_MAX_DEPTH="3";
    private final TaskService taskService;

    @Autowired
    public CrawlerController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/crawl-sync")
    public Collection<Page> executeCrawlTaskSync(@RequestParam("url") String url,
                                                 @RequestParam(value="depth", required = false, defaultValue=CONST_DEFAULT_MAX_DEPTH) Integer maxDepth) {
        LOG.info("crawl-sync initiated");
        return taskService.execute(url, maxDepth);
    }


    @RequestMapping(value = "/crawl-deferred")
    public DeferredResult<Collection<Page>> executeCrawlDeferredTask(@RequestParam("url") String url,
                                                                     @RequestParam(value="depth", required = false, defaultValue=CONST_DEFAULT_MAX_DEPTH) Integer maxDepth) {
        LOG.info("crawl-deferred initiated, param: url: " + url);

        //Set long timeout
        DeferredResult<Collection<Page>> deferredResult = new DeferredResult<>(0L);
        CompletableFuture.supplyAsync(() -> {
            return taskService.execute(url, maxDepth);
        }).whenCompleteAsync((result, throwable) -> deferredResult.setResult(result));

        LOG.info("running in an alternate thread");

        return deferredResult;
    }

}
