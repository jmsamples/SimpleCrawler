package com.jmsamples.core;

import com.jmsamples.model.Page;
import org.jsoup.nodes.Document;

public interface DocumentStrategy {
    public Document getDocument(Page page);
}