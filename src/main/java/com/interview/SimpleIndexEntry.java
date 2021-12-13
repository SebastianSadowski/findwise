package com.interview;

import com.findwise.IndexEntry;

import java.util.Objects;

public class SimpleIndexEntry implements IndexEntry {

    String id;
    Double score;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public void setScore(double score) {
        this.score = score;
    }

    public SimpleIndexEntry(String id) {
        this.id = id;
        score=1d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleIndexEntry that = (SimpleIndexEntry) o;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "SimpleIndexEntry{" +
                "id='" + id + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
