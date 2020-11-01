package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MinHeapImplImplTest {

 /*   @Test
    public void MinHeapImplImplTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString1 = "11111111";
        URI uri1 = new URI("https://www.string1.com/");
        DocumentImpl doc1 = new DocumentImpl( uri1,initialString1, initialString1.hashCode());
        DocStore.bTreeImpl.put(uri1,doc1);
        DocStore.minHeap.insert(uri1);




        String initialString2 = "222222222222222";
        URI uri2 = new URI("https://www.string2.com/");
        DocumentImpl doc2 = new DocumentImpl( uri2,initialString2, initialString2.hashCode());
        DocStore.bTreeImpl.put(uri2,doc2);
        DocStore.minHeap.insert(uri2);
        DocStore.bTreeImpl.get(uri2).setLastUseTime(Long.MIN_VALUE + 10);
        DocStore.minHeap.reHeapify(uri2);

        DocStore.bTreeImpl.get(uri1).setLastUseTime(Long.MIN_VALUE);
        DocStore.minHeap.reHeapify(uri1);




        assertEquals(DocStore.minHeap.removeMin(), uri1);

        String initialString3 = "333333333333";
        URI uri3 = new URI("https://www.string3.com/");
        DocumentImpl doc3 = new DocumentImpl( uri3,initialString3, initialString3.hashCode());
        DocStore.bTreeImpl.put(uri3,doc3);

        DocStore.minHeap.insert(uri3);

        assertEquals(DocStore.minHeap.removeMin(), uri2);

        //doc1.setLastUseTime(System.nanoTime());
        //DocStore.minHeap.reHeapify(uri1);







    }

    @Test
    public void MinHeapImplImplTest2() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString1 = "11111111";
        URI uri1 = new URI("https://www.string1.com/");
        DocumentImpl doc1 = new DocumentImpl( uri1,initialString1, initialString1.hashCode());
        DocStore.bTreeImpl.put(uri1,doc1);
        DocStore.minHeap.insert(uri1);




        String initialString2 = "222222222222222";
        URI uri2 = new URI("https://www.string2.com/");
        DocumentImpl doc2 = new DocumentImpl( uri2,initialString2, initialString2.hashCode());
        DocStore.bTreeImpl.put(uri2,doc2);
        DocStore.minHeap.insert(uri2);



        String initialString3 = "333333333333";
        URI uri3 = new URI("https://www.string3.com/");
        DocumentImpl doc3 = new DocumentImpl( uri3,initialString3, initialString3.hashCode());
        DocStore.bTreeImpl.put(uri3,doc3);
        DocStore.minHeap.insert(uri3);

        DocStore.bTreeImpl.get(uri2).setLastUseTime(Long.MIN_VALUE);
        DocStore.minHeap.reHeapify(uri2);


        assertEquals(DocStore.minHeap.removeMin(), uri2);
        assertEquals(DocStore.minHeap.removeMin(), uri1);
        assertEquals(DocStore.minHeap.removeMin(), uri3);


        //doc1.setLastUseTime(System.nanoTime());
        //DocStore.minHeap.reHeapify(uri1);







    }
/*
    @Test
    public void manyitems() throws URISyntaxException {
        System.out.println("document store impl test");


        DocumentStoreImpl DocStore = new DocumentStoreImpl();

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

        List<String> results = DocStore.search("4");
        List<String> textDocArray = new ArrayList<>();
        textDocArray.add(initialString4);
        textDocArray.add(initialString3);
        textDocArray.add(initialString2);
        assertEquals(results, textDocArray);
        //expected, actual


        //assertEquals(DocStore.getDocumentAsTxt(uri3), initialString3);

        //assertEquals(DocStore.getDocumentAsTxt(uri4), initialString4);
    }



    @Test
    public void MinHeapImplImplTest3() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString1 = "11111111";
        URI uri1 = new URI("https://www.string1.com/");
        DocumentImpl doc1 = new DocumentImpl( uri1,initialString1, initialString1.hashCode());
        DocStore.bTreeImpl.put(uri1,doc1);
        DocStore.minHeap.insert(uri1);


        String initialString2 = "222222222222222";
        URI uri2 = new URI("https://www.string2.com/");
        DocumentImpl doc2 = new DocumentImpl( uri2,initialString2, initialString2.hashCode());
        DocStore.bTreeImpl.put(uri2,doc2);
        DocStore.minHeap.insert(uri2);

        String initialString3 = "333333333333";
        URI uri3 = new URI("https://www.string3.com/");
        DocumentImpl doc3 = new DocumentImpl( uri3,initialString3, initialString3.hashCode());
        DocStore.bTreeImpl.put(uri3,doc3);
        DocStore.minHeap.insert(uri3);

        String initialString4 = "4444444";
        URI uri4 = new URI("https://www.string4.com/");
        DocumentImpl doc4 = new DocumentImpl( uri4,initialString4, initialString4.hashCode());
        DocStore.bTreeImpl.put(uri4,doc4);
        DocStore.minHeap.insert(uri4);


        String initialString5 = "5555555";
        URI uri5 = new URI("https://www.string5.com/");
        DocumentImpl doc5 = new DocumentImpl( uri5,initialString5, initialString5.hashCode());
        DocStore.bTreeImpl.put(uri5,doc5);
        DocStore.minHeap.insert(uri5);




    }


    */



}