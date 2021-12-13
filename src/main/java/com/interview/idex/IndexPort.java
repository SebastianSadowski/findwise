package com.interview.idex;

import com.findwise.IndexEntry;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface IndexPort {

    ConcurrentMap<String, Set<IndexEntry>> getEntries();

    void setEntries(ConcurrentMap<String, Set<IndexEntry>> entries);
}
