package com.interview;

import com.findwise.IndexEntry;
import com.findwise.SearchEngine;
import com.interview.idex.IndexPort;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class SimpleEngine implements SearchEngine {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();

    IndexPort index;

    public SimpleEngine(IndexPort index) {
        this.index = index;
    }

    @Override
    public void indexDocument(String id, String content) {
        List<String> wordsInDoc = parseContent(content);
        wordsInDoc = validWords(wordsInDoc);

        try {
            putParsedDocumentToIndex(id, wordsInDoc);
        } catch (KeyAlreadyExistsException e) {
            //should be better handled
            System.out.println("Catch me if you can!");
        }
    }

    @Override
    public List<IndexEntry> search(String term) {
        return searchInIndex(term);
    }

    private void addParsedDataToIndex(IndexEntry documentEntry, String word) {
        var indexEntries = index.getEntries().get(word);
        if (indexEntries != null) {
            if (indexEntries.contains(documentEntry))
                indexEntries
                        .forEach(en -> {
                            if (en.equals(documentEntry)) {
                                System.out.println(" entry with documentId already exists, increment score");
                                indexEntries.remove(en);
                                en.setScore(en.getScore() + 1);
                                indexEntries.add(en);
                            }
                        });
            else {
                indexEntries.add(documentEntry);
            }
            index.getEntries().put(word, indexEntries);
        } else
            index.getEntries().put(word, new HashSet<>(new ArrayList<>(List.of(documentEntry))));
    }

    private List<String> parseContent(String content) {
        var words = content.split(" ");
        Arrays.sort(words);
        return Arrays.stream(words).map(String::toLowerCase).collect(Collectors.toList());
    }

    private List<String> validWords(List<String> words) {
        return words.stream()
                .map(word -> word.replaceAll("[,.]", "")) //remove punctuation in separated words
                .filter(word -> !word.isBlank())
                .collect(Collectors.toList());
    }

    private void putParsedDocumentToIndex(final String docId, List<String> words) throws KeyAlreadyExistsException {
        var documentEntry = new SimpleIndexEntry(docId);
        if (checkIfDocumentIdExists(docId))
            throw new KeyAlreadyExistsException(String.format("Document with id %s already exists in index", docId));
        synchronized (this) {
            words.forEach(wo -> addParsedDataToIndex(documentEntry, wo));
        }
    }

    boolean checkIfDocumentIdExists(final String docId) {
        return index.getEntries().entrySet()
                .stream().anyMatch(indexRow -> indexRow.getValue().contains(new SimpleIndexEntry(docId)));
    }

    private List<IndexEntry> searchInIndex(String term) {
        Set<IndexEntry> result = index.getEntries().get(term);
        return result != null ? new ArrayList<>(result) : new ArrayList<>();
    }
}
