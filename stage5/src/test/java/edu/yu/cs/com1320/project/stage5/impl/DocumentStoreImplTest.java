package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;

import java.io.*;
import java.net.URI;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

public class DocumentStoreImplTest {




    @Test
    public void trieSearch() throws URISyntaxException {
        //System.out.println("document store impl test");


        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "1 1 1 2 2 3";
        String initialString2 = "2 2 2 3 3 4";
        String initialString3 = "3 3 3 4 4 5";
        String initialString4 = "4 4 4 5 5 6";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");
        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

/*
        List<String> results = DocStore.search("4");
        List<String> textDocArray = new ArrayList<>();
        textDocArray.add(initialString4);
        textDocArray.add(initialString3);
        textDocArray.add(initialString2);
        assertEquals(results, textDocArray);
        //expected, actual


        //assertEquals(DocStore.getDocumentAsTxt(uri3), initialString3);

        //assertEquals(DocStore.getDocumentAsTxt(uri4), initialString4);

 */
    }


    @Test
    public void SearchDocsByPrefixTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "1 1 1s";
        String initialString2 = "111 1n";
        String initialString3 = "4 4 4 5 5 6";
        String initialString4 = "1inamillion";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");
        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        List<String> results = DocStore.searchByPrefix("1");
        List<String> textDocArray = new ArrayList<>();
        textDocArray.add(initialString1);
        textDocArray.add(initialString2);
        textDocArray.add(initialString4);
        assertEquals(results, textDocArray);
        //expected, actual

    }
    @Test
    public void searchByPrefixSortedEmptyTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        assertEquals(DocStore.searchByPrefix("yo"),new ArrayList<String>());
    }

    @Test
    public void searchEmptyTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        assertEquals(DocStore.search("yo"),new ArrayList<String>());
    }

    @Test
    public void deleteTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        assertEquals(DocStore.search("yo"),new ArrayList<String>());
    }


    @Test
    public void debuggingTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "crazy";

        URI uri1 = new URI("https://www.string1.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);

        assertEquals(DocStore.getDocumentAsDocument(uri1).wordCount("crazy"),1);

    }

    @Test
    public void trieKeywordDelete() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "crazy has also";
        String initialString2 ="it testin crazy doesnt blah have it here";
        String initialString3 = "doesnt crazy also have blah it";
        String initialString4 =  "crazy holes how his in golf";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        Set<URI> uriSet = DocStore.deleteAll("also");
        Set<URI> uriSetExpected = new HashSet<URI>();
        uriSetExpected.add(uri1);
        uriSetExpected.add(uri3);
        assertEquals(uriSet, uriSetExpected);

        List<String> actual = new ArrayList<>();
        actual.add(DocStore.getDocumentAsTxt(uri4));
        actual.add(DocStore.getDocumentAsTxt(uri2));

        assertEquals(DocStore.searchByPrefix("h"), actual);

        assertEquals(DocStore.getDocumentAsTxt(uri1), null);
    }




    @Test
    public void deleteAllWithPrefixTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);



        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);

        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);


        Set<URI> uriSet = new HashSet<>();
        uriSet.add(uri3);
        uriSet.add(uri4);

        assertEquals(DocStore.deleteAllWithPrefix("pRe.di"), uriSet);



        List<String> actual = new ArrayList<>();
        actual.add(DocStore.getDocumentAsTxt(uri2));
        actual.add(DocStore.getDocumentAsTxt(uri1));
        assertEquals(DocStore.searchByPrefix("PRe"),actual);

    }




    @Test
    public void undoBigStack() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));

        String initialString1 = "Hello World!1";
        String initialString2 = "Hello World!2";
        String initialString3 = "Hello World!3";
        String initialString4 = "Hello World!4";
        String initialString5 = "Hello World!5";
        String initialString6 = "Hello World!6";
        String initialString7 = "Hello World!7";
        String initialString8 = "Hello World!8";
        String initialString9 = "Hello World!9";
        String initialString10 = "Hello World!10";
        String initialString11 = "Hello World!11";
        String initialString12 = "Hello World!12";
        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");
        URI uri5 = new URI("https://www.string5.com/");
        URI uri6 = new URI("https://www.string6.com/");
        URI uri7 = new URI("https://www.string7.com/");
        URI uri8 = new URI("https://www.string8.com/");
        URI uri9 = new URI("https://www.string9.com/");
        URI uri10 = new URI("https://www.string10.com/");
        URI uri11 = new URI("https://www.string11.com/");
        URI uri12 = new URI("https://www.string12.com/");
        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream5 = new ByteArrayInputStream(initialString5.getBytes());
        int result5 = DocStore.putDocument(targetStream5, uri5, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream6 = new ByteArrayInputStream(initialString6.getBytes());
        int result6 = DocStore.putDocument(targetStream6, uri6, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream7 = new ByteArrayInputStream(initialString7.getBytes());
        int result7 = DocStore.putDocument(targetStream7, uri7, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream8 = new ByteArrayInputStream(initialString8.getBytes());
        int result8 = DocStore.putDocument(targetStream8, uri8, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());
        int result9 = DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream10 = new ByteArrayInputStream(initialString10.getBytes());
        int result10 = DocStore.putDocument(targetStream10, uri10, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream11 = new ByteArrayInputStream(initialString11.getBytes());
        int result11 = DocStore.putDocument(targetStream11, uri11, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream12 = new ByteArrayInputStream(initialString12.getBytes());
        int result12 = DocStore.putDocument(targetStream12, uri12, DocumentStore.DocumentFormat.TXT);



    }

    @Test (expected = IllegalStateException.class)
    public void undoEmptyStackTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));

        DocStore.undo();
    }

    @Test
    public void simpleUndoTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        //int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        //DocStore.putDocument(null, uri2, DocumentStore.DocumentFormat.TXT);

        //assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),initialString2);
        //assertEquals(DocStore.getDocumentAsTxt(uri2),null);
        //assertEquals(DocStore.getDocumentAsTxt(uri3),"this is so predictable");
        //assertEquals(DocStore.getDocumentAsTxt(uri4),"i hate predicate logic");

    }


    @Test
    public void undoDeleteAllTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        DocStore.deleteAll("I");

        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),null);
        assertEquals(DocStore.getDocumentAsTxt(uri3),"this is so predictable");
        assertEquals(DocStore.getDocumentAsTxt(uri4),null);

        DocStore.undo();

        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),"i shall give you a present mr president");
        assertEquals(DocStore.getDocumentAsTxt(uri3),"this is so predictable");
        assertEquals(DocStore.getDocumentAsTxt(uri4),"i hate predicate logic");
    }


    @Test
    public void DeleteAllWithPrefixTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 = "i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        System.out.println(DocStore.minHeap.elements[1]);

        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        System.out.println(DocStore.minHeap.elements[2]);

        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        System.out.println(DocStore.minHeap.elements[3]);

        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);
        System.out.println(DocStore.minHeap.elements[4]);

        //System.out.println(DocStore.searchByPrefix("predi"));

        DocStore.deleteAllWithPrefix("predi");
/*
        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),"i shall give you a present mr president");
        assertEquals(DocStore.getDocumentAsTxt(uri3),null);
        assertEquals(DocStore.getDocumentAsTxt(uri4),null);

        DocStore.undo();

        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),"i shall give you a present mr president");
        assertEquals(DocStore.getDocumentAsTxt(uri3),"this is so predictable");
        assertEquals(DocStore.getDocumentAsTxt(uri4),"i hate predicate logic");

 */
    }



    @Test
    public void DebugHeap() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));

        String initialString4 =  "i hate predicate logic";
        URI uri4 = new URI("https://www.string4.com/");
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());

        DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        DocStore.deleteAllWithPrefix("predic");



    }


    @Test
    public void UndoDeleteAllWithPrefix() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        int result1 = DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        int result2 = DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        int result3 = DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);



        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());
        int result4 = DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);



        DocStore.deleteAllWithPrefix("predic");

        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),"i shall give you a present mr president");
        assertEquals(DocStore.getDocumentAsTxt(uri3),null);
        assertEquals(DocStore.getDocumentAsTxt(uri4),null);

        DocStore.undo(uri3);

        assertEquals(DocStore.getDocumentAsTxt(uri1),"this one's go the prefix");
        assertEquals(DocStore.getDocumentAsTxt(uri2),"i shall give you a present mr president");
        assertEquals(DocStore.getDocumentAsTxt(uri3),"this is so predictable");
        assertEquals(DocStore.getDocumentAsTxt(uri4),null);

    }
/*
//FIELDS MINHEAP IN DOCSTOREIMPL AND ELEMENTS IN MINHEAPIMPL NEED TO BE PUBLIC FOR THIS TEST
    @Test
    public void heapDeleteAndUndoUriTest() throws URISyntaxException {
                DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());

        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);


        Document[] checker = {null, DocStore.getDocumentAsDocumentNoReheap(uri1), DocStore.getDocumentAsDocumentNoReheap(uri2), DocStore.getDocumentAsDocumentNoReheap(uri3), DocStore.getDocumentAsDocumentNoReheap(uri4), null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker);

        DocStore.deleteDocument(uri3);

        Document[] checker2 = {null, DocStore.getDocumentAsDocumentNoReheap(uri1), DocStore.getDocumentAsDocumentNoReheap(uri2), DocStore.getDocumentAsDocumentNoReheap(uri4), null, null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker2);

        DocStore.undo(uri3);

        Document[] checker3 = {null, DocStore.getDocumentAsDocumentNoReheap(uri1), DocStore.getDocumentAsDocumentNoReheap(uri2), DocStore.getDocumentAsDocumentNoReheap(uri4), DocStore.getDocumentAsDocumentNoReheap(uri3), null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker3);
    }



 */
/*

    //FIELDS MINHEAP IN DOCSTOREIMPL AND ELEMENTS IN MINHEAPIMPL NEED TO BE PUBLIC FOR THIS TEST
    @Test
    public void heapGetTest() throws URISyntaxException {
                DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        InputStream targetStream3b = new ByteArrayInputStream(initialString3.getBytes());
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());

        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);


        Document[] checker = {null, DocStore.getDocumentAsDocumentNoReheap(uri1), DocStore.getDocumentAsDocumentNoReheap(uri2), DocStore.getDocumentAsDocumentNoReheap(uri3), DocStore.getDocumentAsDocumentNoReheap(uri4), null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker);

        DocStore.getDocumentAsTxt(uri1);

        Document[] checker2 = {null, DocStore.getDocumentAsDocumentNoReheap(uri2), DocStore.getDocumentAsDocumentNoReheap(uri4), DocStore.getDocumentAsDocumentNoReheap(uri3), DocStore.getDocumentAsDocumentNoReheap(uri1), null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker2);


    }



    //FIELDS MINHEAP IN DOCSTOREIMPL AND ELEMENTS IN MINHEAPIMPL NEED TO BE PUBLIC FOR THIS TEST
    @Test
    public void heapPutPreexistingTest() throws URISyntaxException {
                DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 ="i shall give you a present mr president";
        String initialString3 = "this is so predictable";
        String initialString4 =  "i hate predicate logic";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");
        URI uri4 = new URI("https://www.string4.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());

        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());
        InputStream targetStream4 = new ByteArrayInputStream(initialString4.getBytes());

        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream4, uri4, DocumentStore.DocumentFormat.TXT);

        DocStore.putDocument(targetStream2b, uri2, DocumentStore.DocumentFormat.TXT);

        Document[] checker2 = {null, DocStore.getDocumentAsDocumentNoReheap(uri1), DocStore.getDocumentAsDocumentNoReheap(uri4), DocStore.getDocumentAsDocumentNoReheap(uri3), DocStore.getDocumentAsDocumentNoReheap(uri2), null, null, null};
        assertArrayEquals(DocStore.minHeap.elements, checker2);


    }

 */

    @Test
    public void setDocLimitAndUndo() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 = "i shall give you a present mr president";
        String initialString3 = "this is so predictable";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());


        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);

        DocStore.setMaxDocumentCount(2); // can put before and after the putdoc and it still works :)
/*
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri1),null);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), initialString3);

        DocStore.undo(uri3); //also works if i do ()

        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri1),null);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), null);

        //DocStore.minHeap.removeMin();
        //DocStore.minHeap.removeMin();// heap only has one element in it :)


 */

    }



    @Test
    public void setDocLimitAndUndo2() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 = "i shall give you a present mr president";
        String initialString3 = "this is so predictable";

        int a1 = initialString1.getBytes().length;
        int b1 = txtToPdfee(initialString1).length;
        int totalBytes1 = a1 + b1; //860

        int a2 = initialString2.getBytes().length;
        int b2 = txtToPdfee(initialString2).length;
        int totalBytes2 = a2 + b2; // 885

        int a3 = initialString3.getBytes().length;
        int b3 = txtToPdfee(initialString3).length;
        int totalBytes3 = a3 + b3; // 854

        //total is 2599



        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());

        DocStore.setMaxDocumentBytes(2300);


        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);

        //assertEquals(DocStore.getDocumentAsDocumentJsonNoReheap(uri1),initialString1);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), initialString3);


        DocStore.undo();
/*
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri1),null);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), null);

         */
    }


    private byte[] txtToPdfee(String txt){
        try{
            if (txt == null){return null;} // avoids null pointer exception and still allows the doc to construct
            else {
                // create document, add page, and then saves it. still need to actually turn it into pdf
                PDDocument pdDoc = new PDDocument();
                PDFont font = PDType1Font.TIMES_ROMAN;
                PDPage page = new PDPage();
                pdDoc.addPage(page);
                PDPageContentStream streamToDoc = new PDPageContentStream(pdDoc, page);
                streamToDoc.beginText();
                streamToDoc.setFont(font, 12);
                streamToDoc.showText(txt);
                streamToDoc.endText();
                streamToDoc.close();
                //pdDoc.save("HelloWorld!.pdf");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                pdDoc.save(baos); //saves file to output stream
                pdDoc.close(); //closes the doc
                return baos.toByteArray();
            }
        } catch (Exception e) {e.printStackTrace(); return null;}
    }

    @Test
    public void emptyCommandsStackTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream target1 = null;
        DocStore.putDocument(null,uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.undo();
        DocStore.deleteDocument(uri3);
        DocStore.undo(uri3);

        //any aditional undos make it fail :)
    }


    @Test
    public void deleteAllWithPrefixUndoOverLimit() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl((new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5")));


        String initialString1 = "this one's go the prefix";
        String initialString2 = "i shall give you a present mr president";
        String initialString3 = "this is so predictable";

        int a1 = initialString1.getBytes().length;
        int b1 = txtToPdfee(initialString1).length;
        int totalBytes1 = a1 + b1; //860

        int a2 = initialString2.getBytes().length;
        int b2 = txtToPdfee(initialString2).length;
        int totalBytes2 = a2 + b2; // 885

        int a3 = initialString3.getBytes().length;
        int b3 = txtToPdfee(initialString3).length;
        int totalBytes3 = a3 + b3; // 885

        //total is 2599



        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());

        DocStore.setMaxDocumentBytes(2500);

        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri1, DocumentStore.DocumentFormat.TXT);

        DocStore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);

        assertEquals(DocStore.getDocumentAsTxtNoReheap(uri1),initialString2);
        //assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        //assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), initialString3);

        //DocStore.undo();

        //assertEquals(DocStore.getDocumentAsTxtNoReheap(uri1),null);
        //assertEquals(DocStore.getDocumentAsTxtNoReheap(uri2), initialString2);
        //assertEquals(DocStore.getDocumentAsTxtNoReheap(uri3), null);
    }








}