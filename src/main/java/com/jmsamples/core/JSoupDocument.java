package com.jmsamples.core;

import com.jmsamples.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSoupDocument implements DocumentStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(JSoupDocument.class);

    @Override
    public Document getDocument(Page page) {
        try {
            return Jsoup.connect(page.getUrl()).get();
        } catch (Exception e) {
            LOG.error("Error url" + page.getUrl(), e);
        }
        return null;
    }
}
