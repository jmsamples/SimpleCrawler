package com.jmsamples;

import com.jmsamples.core.DocumentStrategy;
import com.jmsamples.core.WebCrawler;
import com.jmsamples.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static junit.framework.TestCase.assertEquals;

public class WebCrawlerTest {

    private static final Logger LOG = LoggerFactory.getLogger(WebCrawlerTest.class);

    @Test
    public void testCrawl() throws Exception {

        long startTime = System.currentTimeMillis();
        DocumentStrategy mockDocument  = (page) -> {
            try {
                String simpleHTML
                        = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                        .getResource("static/" + page.getUrl()).toURI())));
                Document document = Jsoup.parse(simpleHTML);
                return document;
            } catch (Exception e) {
                LOG.error("Error url" + page.getUrl(), e);
            }
            return null;

        };

        WebCrawler crawler = new WebCrawler(mockDocument);
        long endTime = System.currentTimeMillis();

        HashSet<Page> initialDocs = new HashSet<>();
        initialDocs.add(new Page("index.html", 0));
        int MAX_DEPTH = 3;
        Collection<Page> rootPage = crawler.visit(initialDocs, MAX_DEPTH);

        assertEquals(1, rootPage.size());

        for (Page root : rootPage) {
            HashSet<Page> pageLevel1 = root.getNodes();
            assertEquals(6, pageLevel1.size());
            LOG.debug("pages is: " + pageLevel1);
            for (Page pageLevel2 : pageLevel1) {
                HashSet<Page> pageLevel3 = pageLevel2.getNodes();
                assertEquals(2, pageLevel3.size());
                LOG.debug("pages is: " + pageLevel3);

            }

        }

        LOG.debug("json is: " + rootPage);

    }
}