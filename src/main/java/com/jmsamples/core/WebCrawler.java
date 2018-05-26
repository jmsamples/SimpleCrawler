    package com.jmsamples.core;

    import com.jmsamples.model.Page;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    import java.util.Collection;
    import java.util.HashSet;
    import java.util.Queue;
    import java.util.Set;
    import java.util.concurrent.LinkedBlockingQueue;

    /**
     * Crawl a site beginning at the supplied URL to a specified depth.
     * @author JM
     */
    public final class WebCrawler {

        private static final Logger LOG = LoggerFactory.getLogger(WebCrawler.class);

        private DocumentStrategy documentStrategy;

        public WebCrawler(DocumentStrategy documentStrategy) {
            this.documentStrategy = documentStrategy;
        }

        /**
         * Method used to crawl the root page, extract links and then crawl child pages
         * @param rootPage initial page. Begin the crawl from here
         * @param maxDepth maximum depth for which to crawl.
         */
        public Collection<Page> visit(Collection<Page> rootPage, int maxDepth) {
            //Keep a queue of links that need to be visited
            Queue<Page> pendingQueue = new LinkedBlockingQueue(rootPage);
            //Keep track of all links that have been visited
            Set<String> visited = new HashSet<>();
            while (!pendingQueue.isEmpty()) {
                Page parentPage = pendingQueue.poll();
                if (!visited.contains(parentPage.getUrl()) && parentPage.getDepth() < maxDepth) {
                    visited.add(parentPage.getUrl());
                    populateChildLinks(parentPage);
                    for(Page childLink : parentPage.getNodes()) {
                        pendingQueue.add(childLink);
                        LOG.debug("Parent " + parentPage.getUrl() +" >> Depth: " + childLink.getDepth() + " [" + childLink + "]");
                    }
                } else {
                    LOG.debug("Skipping " + parentPage.getUrl());
                }
            }
            return rootPage;
        }

        private void populateChildLinks(Page page) {
            if (!page.getUrl().isEmpty()) {
                Document document = documentStrategy.getDocument(page);
                Elements childLinks = document.select("a[href]");
                for (Element link : childLinks) {
                    String childUrl = link.attr("abs:href");
                    if (childUrl.isEmpty()) {
                        childUrl = link.attr("href");
                    }
                    String title = link.text();
                    //remove trailing slash (reduce duplicate links)
                    childUrl = childUrl.replaceAll("/$", "");
                    Page newPage = new Page(childUrl, title, page.getDepth() + 1);
                    page.addNode(newPage);
                }
            }
        }


        public static void main(String[] args) {
            long startTime = System.currentTimeMillis();
            WebCrawler crawler = new  WebCrawler (new JSoupDocument());
            long endTime = System.currentTimeMillis();

            HashSet<Page> initialDocs = new HashSet<>();
            initialDocs.add(new Page("http://localhost:8000/index.html", 0));
            int MAX_DEPTH = 3;
            Collection<Page> json = crawler.visit(initialDocs, MAX_DEPTH);
            LOG.debug("json is: " + json);

            LOG.debug("That took " + (endTime - startTime) + " milliseconds");
        }



    }
