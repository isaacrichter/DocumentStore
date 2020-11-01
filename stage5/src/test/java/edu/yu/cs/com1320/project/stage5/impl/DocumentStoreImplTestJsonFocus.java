package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import org.junit.Test;
import static org.junit.Assert.*;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

public class DocumentStoreImplTestJsonFocus {

    @Test
    public void moveToDiskTest() throws URISyntaxException {
    }/*
        DocumentStoreImpl docstore = new DocumentStoreImpl();
        docstore.setMaxDocumentCount(2);
        String initialString1 = "this is doc1";
        String initialString2 = "this is doc2";
        String initialString3 = "this is doc3";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        //InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());


        docstore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docstore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docstore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);

        assertEquals(docstore.getDocument(uri1).getDocumentAsTxt(), initialString1);

        assertEquals(docstore.minHeap.removeMin(),uri2 );
        assertEquals(docstore.minHeap.removeMin(),uri3 );
        try{
            docstore.minHeap.removeMin();

        }catch (Exception E){
            System.out.println("test passed");
        }






    }

@Test
    public void setLimitsTest() throws URISyntaxException {
        DocumentStoreImpl docstore = new DocumentStoreImpl();
        String initialString1 = "this is doc1";
        String initialString2 = "this is doc2";
        String initialString3 = "this is doc3";

        URI uri1 = new URI("https://www.string1.com/");
        URI uri2 = new URI("https://www.string2.com/");
        URI uri3 = new URI("https://www.string3.com/");

        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        //InputStream targetStream2b = new ByteArrayInputStream(initialString2.getBytes());
        InputStream targetStream3 = new ByteArrayInputStream(initialString3.getBytes());


        docstore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        docstore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        docstore.putDocument(targetStream3, uri3, DocumentStore.DocumentFormat.TXT);
        docstore.setMaxDocumentCount(2);

    //assertEquals(docstore.minHeap.removeMin(),uri1 );
    //System.out.println(docstore.minHeap.removeMin());
    //System.out.println(docstore.minHeap.removeMin());

        assertEquals(docstore.minHeap.removeMin(),uri2 );
        assertEquals(docstore.minHeap.removeMin(),uri3 );
        try{
            docstore.minHeap.removeMin();

        }catch (Exception E){
            System.out.println("test passed");
        }
        assertNull(docstore.getDocumentAsDocument(uri1));
        assertEquals(docstore.getDocument(uri1).getDocumentAsTxt(), initialString1);






    }

     */
}
