package com.interview.access.controller;

import com.findwise.IndexEntry;
import com.interview.access.controller.dto.DocumentDTO;
import com.interview.access.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public void addDocument(@RequestBody DocumentDTO document) {
        searchService.addDocument(document);
    }

    @GetMapping("/{term}")
    public ResponseEntity<List<IndexEntry>> getIndexForTerm(@PathVariable String term) {
        System.out.println(term);
        try {
            List<IndexEntry> search = searchService.search(term);
            return ResponseEntity.ok().body(search);
        } catch (InvalidTermException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}
