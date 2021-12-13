package com.interview.idex;


import com.findwise.IndexEntry;
import com.interview.idex.IndexPort;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryIndexAdapter implements IndexPort {



    private ConcurrentMap<String, Set<IndexEntry>> entries = new ConcurrentHashMap<>();


    public ConcurrentMap<String, Set<IndexEntry>> getEntries() {
        return entries;
    }

    public void setEntries(ConcurrentMap<String, Set<IndexEntry>> entries) {
        this.entries = entries;
    }
}
