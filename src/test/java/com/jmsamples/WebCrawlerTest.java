package com.jmsamples;

import com.jmsamples.core.DocumentStrategy;
import com.jmsamples.core.WebCrawler;
import com.jmsamples.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
        final int MAX_DEPTH = 3;
        Collection<Page> rootPage = crawler.visit(initialDocs, MAX_DEPTH);

        //Test root node
        List<Page> rootList = new ArrayList<Page>(rootPage);
        assertEquals(1, rootList.size());

        //Test level 1 nodes
        List<Page> level1 = new ArrayList<Page>(rootList.get(0).getNodes());
        Collections.sort(level1);

        Page link10 = level1.get(0);
        Page link11 = level1.get(1);
        Page link12 = level1.get(2);
        Page link13 = level1.get(3);
        Page link14 = level1.get(4);
        Page link15 = level1.get(5);

        assertEquals("index.html", link10.getUrl());
        assertEquals("page1a.html", link11.getUrl());
        assertEquals("page1b.html", link12.getUrl());
        assertEquals("page1c.html", link13.getUrl());
        assertEquals("page1d.html", link14.getUrl());
        assertEquals("page1e.html", link15.getUrl());

        //Test level 2 nodes
        List<Page> level2 = new ArrayList<Page>(link11.getNodes());
        Collections.sort(level2);

        Page link20 = level2.get(0);
        Page link21 = level2.get(1);

        assertEquals("index.html", link20.getUrl());
        assertEquals("page2a.html", link21.getUrl());

        //Test level 3 nodes
        List<Page> level3 = new ArrayList<Page>(link21.getNodes());
        Collections.sort(level3);

        Page link30 = level3.get(0);
        Page link31 = level3.get(1);
        Page link32 = level3.get(2);
        Page link33 = level3.get(3);

        assertEquals("index.html", link30.getUrl());
        assertEquals("page3a.html", link31.getUrl());
        assertEquals("page3b.html", link32.getUrl());
        assertEquals("page3c.html", link33.getUrl());


        LOG.debug("json is: " + rootPage);

    }
}