package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.stage2.DocumentStore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DocumentStoreImplTest {
    @Test
    public void putUndoTopItemTest() throws URISyntaxException {
        System.out.println("stage 2");
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = new URI("https://www.google.com/");
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        DocStore.undo();
        assertEquals(DocStore.deleteDocument(uri), false);

    }

    @Test
    public void deleteUndoTopItemTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = new URI("https://www.google.com/");
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        DocStore.deleteDocument(uri);
        assertEquals(DocStore.deleteDocument(uri), false);
        DocStore.undo();
        assertEquals(DocStore.getDocumentAsTxt(uri), "Hello World!");

    }


    @Test
    public void replaceUndoTopItemTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        String initialString1 = "Hello World!";
        String initialString2 = "Hello Chworld!";
        InputStream targetStream1 = new ByteArrayInputStream(initialString1.getBytes());
        InputStream targetStream2 = new ByteArrayInputStream(initialString2.getBytes());
        URI uri1 = new URI("https://www.google.com/");
        URI uri2 = new URI("https://www.theyeshivaworld.com/");
        DocStore.putDocument(targetStream1, uri1, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream2, uri2, DocumentStore.DocumentFormat.TXT);
        DocStore.undo();
        assertEquals(DocStore.getDocumentAsTxt(uri1), "Hello World!");
        assertEquals(DocStore.getDocumentAsTxt(uri2), null);

    }

    @Test
    public void puttingGettingMultipleDocs3() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString9 = "Hello World!9";
        URI uri9 = new URI("https://www.string9.com/");
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());

        DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(null, uri9, DocumentStore.DocumentFormat.TXT);

        DocStore.undo(uri9);
        assertEquals(DocStore.getDocumentAsTxt(uri9), initialString9);
    }


    @Test
    public void putNullDocumentTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        //String string = "Hello World" ;
        URI uri = new URI("https://www.google.com/");
        InputStream inputStream = null;
        int result = DocStore.putDocument(null, uri, DocumentStore.DocumentFormat.PDF);
        assertEquals(0, result);
    }

    @Test
    public void putTxtDocumentTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        /////////////////
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        /////////////////
        //String string = "Hello World" ;
        URI uri = new URI("https://www.google.com/");
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        assertEquals(0, 0);
    }

    @Test
    public void putGetTxtDocumentTest() throws URISyntaxException, IOException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        /////////////////
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        /////////////////
        //String string = "Hello World" ;
        URI uri = new URI("https://www.google.com/");
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        assertEquals(0, 0);
        String txt = DocStore.getDocumentAsTxt(uri);
        assertEquals("Hello World!", txt);
    }

    @Test
    public void putGetPdfDocumentNotNullTest() throws URISyntaxException, IOException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = new URI("https://www.google.com/");
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        byte[] pdfByteResult = DocStore.getDocumentAsPdf(uri);
        assertNotNull(pdfByteResult);
        assertEquals(0, result); // confirms that correct value is returned
    }

    @Test
    public void deleteMethodTest() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        String initialString = "Hello World!";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        URI uri = new URI("https://www.google.com/");
        URI uri1 = new URI("https://www.github.com/");
        boolean isDeleted1 = DocStore.deleteDocument(uri1);
        assertFalse(isDeleted1);
        int result = DocStore.putDocument(targetStream, uri, DocumentStore.DocumentFormat.TXT);
        boolean isDeleted2 = DocStore.deleteDocument(uri);
        assertTrue(isDeleted2);

    }


    @Test
    public void testPutDocumentWithNullArguments() throws URISyntaxException {
        DocumentStore store = new DocumentStoreImpl();
        String string = "hello world";
        URI uri1 = new URI("uri");
        try {
            store.putDocument(new ByteArrayInputStream(string.getBytes()), null, DocumentStore.DocumentFormat.TXT);
            fail("null URI should've thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            store.putDocument(new ByteArrayInputStream(string.getBytes()), uri1, null);
            fail("null format should've thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }


/*
    @Test
    public void testGetPdfDocAsTxt() throws URISyntaxException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = new URI("uri");

        int returned = store.putDocument(new ByteArrayInputStream(pdfData1),uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == pdfTxt1.hashCode());
        assertEquals("failed to return correct text",pdfTxt1,store.getDocumentAsTxt(uri1));
    }
*/

    /*
    @Test
    public void pdfToTxtFromThisDesktop() throws URISyntaxException, IOException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        URI uri1 = new URI("uri");
        File myPdf = new File("C:\\Users\\yitzy\\Downloads\\oneTimeUseStuff\\Spring-2020-Project-Requirements.pdf");
        InputStream targetStream = new FileInputStream(myPdf);
        int result1 = DocStore.putDocument(targetStream, uri1, DocumentStore.DocumentFormat.PDF);
        System.out.print(DocStore.getDocumentAsTxt(uri1));
        //It Works!!!!!!!!!!!!!!!!!!

    }

     */


    // requires inputStreamToByteArray to be private
    /*
    @Test
    public void pdfToTxtFromThisDesktop() throws URISyntaxException, IOException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
        File myPdf = new File("C:\\Users\\yitzy\\Downloads\\oneTimeUseStuff\\stage1TestPDF.pdf");
        InputStream input = new FileInputStream(myPdf);
        byte[] byteArray = DocStore.inputStreamToByteArray(input);
        URI uri1 = new URI("uri");
        InputStream targetStream = new FileInputStream(myPdf);
        int result1 = DocStore.putDocument(targetStream, uri1, DocumentStore.DocumentFormat.PDF);
        System.out.print(DocStore.getDocumentAsTxt(uri1));
        assertEquals("failed to return correct text","This is some PDF text for doc1, hat tip to Adobe.", DocStore.getDocumentAsTxt(uri1));
        //It Works!!!!!!!!!!!!!!!!!! stage1TestPDF.pdf

    }
    */


    @Test
    public void puttingGettingMultipleDocs() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
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

        assertEquals(DocStore.getDocumentAsTxt(uri11), initialString11);
        assertEquals(DocStore.getDocumentAsTxt(uri4), initialString4);
        assertEquals(result9, 0);
        assertEquals(result7, 0);

        int deletedDocHashcode = DocStore.putDocument(null, uri9, DocumentStore.DocumentFormat.TXT);
        assertNull(DocStore.getDocumentAsPdf(uri9));
        assertEquals(deletedDocHashcode, initialString9.hashCode());

    }

    @Test
    public void putUndo() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString9 = "Hello World!9";
        URI uri9 = new URI("https://www.string9.com/");
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());

        DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        DocStore.undo(uri9);
        String doc = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc, null);
    }


    @Test
    public void deleteDeleteMethodUndo() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString9 = "Hello World!9";
        URI uri9 = new URI("https://www.string9.com/");
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());

        DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        DocStore.deleteDocument(uri9);
        String doc = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc, null);
        DocStore.undo(uri9);
        String doc2 = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc2, initialString9);


    }

    @Test
    public void deletePutMethodUndo() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString9 = "Hello World!9";
        URI uri9 = new URI("https://www.string9.com/");
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());

        DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(null, uri9, DocumentStore.DocumentFormat.TXT);
        String doc = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc, null);
        DocStore.undo(uri9);
        String doc2 = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc2, initialString9);


    }

    @Test
    public void replaceUndo() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();

        String initialString9 = "Hello World!9";
        URI uri9 = new URI("https://www.string9.com/");
        InputStream targetStream9 = new ByteArrayInputStream(initialString9.getBytes());

        String initialString8 = "Hello World!9";
        InputStream targetStream8 = new ByteArrayInputStream(initialString8.getBytes());

        DocStore.putDocument(targetStream9, uri9, DocumentStore.DocumentFormat.TXT);
        DocStore.putDocument(targetStream8, uri9, DocumentStore.DocumentFormat.TXT);
        String doc = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc, initialString8);
        DocStore.undo();
        String doc2 = DocStore.getDocumentAsTxt(uri9);
        assertEquals(doc2, initialString9);


    }

    @Test
    public void undoBigStack() throws URISyntaxException {
        DocumentStoreImpl DocStore = new DocumentStoreImpl();
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
        /*DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri11);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri10);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri9);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri8);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri7);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri6);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri5);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri4);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri3);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri2);
        DocStore.undo();
        assertEquals(DocStore.commandStack.peek().getUri(), uri1);
        DocStore.undo();


        DocStore.undo(uri1);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri2);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri3);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri4);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri5);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri6);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri7);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri8);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri9);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri10);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        DocStore.undo(uri11);
        assertEquals(DocStore.commandStack.peek().getUri(), uri12);
        assertEquals(DocStore.getDocumentAsTxt(uri1),null);
        assertEquals(DocStore.getDocumentAsTxt(uri2),null);
        assertEquals(DocStore.getDocumentAsTxt(uri3),null);
        assertEquals(DocStore.getDocumentAsTxt(uri4),null);
        assertEquals(DocStore.getDocumentAsTxt(uri5),null);
        assertEquals(DocStore.getDocumentAsTxt(uri6),null);
        assertEquals(DocStore.getDocumentAsTxt(uri7),null);
        assertEquals(DocStore.getDocumentAsTxt(uri8),null);
        assertEquals(DocStore.getDocumentAsTxt(uri9),null);
        assertEquals(DocStore.getDocumentAsTxt(uri10),null);
        assertEquals(DocStore.getDocumentAsTxt(uri11),null);

         */


    }
}