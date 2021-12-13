package com.interview;

import com.findwise.IndexEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.util.Assert.*;

@SpringBootTest
class SimpleEngineTest {
    @Autowired
    SimpleEngine se;


    @Test
    void should_parse_document_and_contain_1_index_entry() {
        //given
        String documentId = "Unique Document 1";
        String content = "the brown fox jumped over the brown dog”";

        //when
        se.indexDocument(documentId, content);

        //then
        se.index.getEntries().forEach(
                (s, indexEntries) -> {
                    notEmpty(indexEntries, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
                    isTrue(1 == indexEntries.size(), "[Assertion failed] - for one document size is always 1");
                }

        );
    }

    @Test
    void should_not_update_index_and_throws_KeyAlteradyExistsException() {
        //given
        String documentId = "Unique Document 1";
        String content = "the brown wolf jumped over the brown dog";
        String newContent = "the brown fox jumped over the brown dog";

        //when
        se.indexDocument(documentId, content);

        //then
        se.index.getEntries().forEach(
                (s, indexEntries) -> {
                    notEmpty(indexEntries, "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
                    isTrue(1 == indexEntries.size(), "[Assertion failed] - for one document size is always 1");
                }
        );
        assertThrows(KeyAlreadyExistsException.class ,() -> se.indexDocument(documentId, newContent));
        notNull(se.index.getEntries().get("wolf"), "[Asserion failed] key wolf exists in Document 1");
        isNull(se.index.getEntries().get("wolf123"), "[Asserion failed] key wolf123 not exists in documents");
    }

    @Test
    void should_not_find_document_in_index_and_return_true() {
        //given
        String existingDoc = "Document 1";
        String notExistingDoc = "Unique Document 1";
        //when
        boolean exists = se.checkIfDocumentIdExists(existingDoc);
        boolean notExists = se.checkIfDocumentIdExists(notExistingDoc);
        //then
        isTrue(exists, "wskazany dokument został wcześniej dodany do indexu!");
        isTrue(!notExists, "wskazany dokument nie został wcześniej dodany do indexu");
    }

    @Test
    void should_return_IndexEntry(){
        //given
        String existingKey = "key1";
        String notExistingKey = "not existing key";

        //when
        List<IndexEntry> notEmptyList = se.search(existingKey);
        List<IndexEntry> emptyList = se.search(notExistingKey);

        //then
        notEmpty(notEmptyList, "Key already initialized, shouldn't be null");
        isTrue(emptyList.size()==0, "Key not exists in index, should be 0");
    }






    @BeforeEach
    void beforeAll() {
        se.index.setEntries(prepareSimpleIndex());
    }

    public ConcurrentMap<String, Set<IndexEntry>> prepareSimpleIndex() {

        ConcurrentMap<String, Set<IndexEntry>> index = new ConcurrentHashMap<>();
        String key1 = "key1";
        String key2 = "key2";
        String key3 = "key3";
        String docId1 = "Document 1";
        String docId2 = "Document 2";
        String docId3 = "Document 3";
        SimpleIndexEntry simpleIndexEntry1 = new SimpleIndexEntry(docId1);
        SimpleIndexEntry simpleIndexEntry2 = new SimpleIndexEntry(docId2);
        SimpleIndexEntry simpleIndexEntry3 = new SimpleIndexEntry(docId3);
        index.put(key1, new HashSet<>(List.of(simpleIndexEntry1)));
        index.put(key2, new HashSet<>(List.of(simpleIndexEntry2)));
        index.put(key3, new HashSet<>(List.of(simpleIndexEntry3)));
        return index;
    }

    @AfterEach
    void tearDown() {
        se.index.getEntries().clear();
    }
}