package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class DocumentImplTest {

    // also i added a line while testing that saves the pdDoc in the code as a pdf,
    // and it opened it and whatever i set the string to was there
    @Test
    public void constuctingTxtTest() throws URISyntaxException {
        String string = "Hello World" ;
        URI uri = new URI("https://www.google.com/");
        DocumentImpl doc1 = new DocumentImpl(uri ,string, string.hashCode());
        assertEquals(doc1.getDocumentAsTxt() ,string);
        assertEquals(doc1.getDocumentTextHashCode(), string.hashCode());
    }

}