package com.jmsamples.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;

@AllArgsConstructor
@ToString
public class Page implements Comparable<Page> {
    @Getter
    private String url;
    private String title;
    @Getter
    transient private int depth;
    private HashSet<Page> nodes = new HashSet<>();

    public Page(String url, String title, int depth) {
        this.url = url;
        this.title = title;
        this.depth = depth;
    }

    public Page(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url);
    }

    public void addNode(Page node) {
        nodes.add(node);
    }

    public HashSet<Page> getNodes() {
        return nodes;
    }

    @Override
    public int compareTo(Page o) {
        return this.getUrl().compareTo(o.getUrl());
    }
}