package com.interview.access.service;

import com.findwise.IndexEntry;
import com.findwise.SearchEngine;
import com.interview.access.controller.InvalidTermException;
import com.interview.access.controller.dto.DocumentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    SearchEngine searchEngine;

    public SearchService(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }


    public void addDocument(DocumentDTO document) {
        searchEngine.indexDocument(document.getId(), document.getContent());
    }

    public List<IndexEntry> search(String term) throws InvalidTermException {
        term = term.trim().toLowerCase();
        if(checkTerm(term)) throw new InvalidTermException();
        return searchEngine.search(term);
    }

    private boolean checkTerm(String term) {
       return term.split(" ").length != 1;
    }
}
