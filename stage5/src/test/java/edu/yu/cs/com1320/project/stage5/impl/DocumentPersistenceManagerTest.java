package edu.yu.cs.com1320.project.stage5.impl;

import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import edu.yu.cs.com1320.project.stage5.Document;

import static org.junit.Assert.*;

public class DocumentPersistenceManagerTest {

    @Test
    public void simpleSerializeDeserializeTest() throws URISyntaxException, IOException {
        String initialString1 = "this is the document text string";
        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");
        DocumentPersistenceManager dpm = new DocumentPersistenceManager(file);

        dpm.serialize(uri1, doc);
        Document docfinal = dpm.deserialize(uri1);
        assertEquals(doc.getDocumentAsTxt(), docfinal.getDocumentAsTxt());
        assertEquals(doc.getKey(), docfinal.getKey());
        assertEquals(doc.getWordMap(), docfinal.getWordMap());
        assertNotEquals(doc.getLastUseTime(), docfinal.getLastUseTime());
    }

    @Test
    public void DeserializeNullTest() throws URISyntaxException, IOException {
        URI uri1 = new URI("yeah/man");
        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");
        DocumentPersistenceManager dpm = new DocumentPersistenceManager(file);
        assertNull(dpm.deserialize(uri1));

    }

    @Test
    public void simpleSerializeDeserializeTestEmptyContructor() throws URISyntaxException, IOException {
        String initialString1 = "this is the document text string";
        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        DocumentPersistenceManager dpm = new DocumentPersistenceManager();



        dpm.serialize(uri1, doc);
        Document docfinal = dpm.deserialize(uri1);
        assertEquals(doc.getDocumentAsTxt(), docfinal.getDocumentAsTxt());
        assertEquals(doc.getKey(), docfinal.getKey());
        assertEquals(doc.getWordMap(), docfinal.getWordMap());
        assertNotEquals(doc.getLastUseTime(), docfinal.getLastUseTime());
        File testFile = new File("C:\\Users\\yitzy\\Documents\\ForTestingStage5\\raw.githubusercontent.com");

        assertFalse(testFile.exists());
    }
/*
    @Test
    public void deserializeDeleteEmptyDirsTest() throws URISyntaxException, IOException {
        String initialString1 = "this is the document text string";
        String initialString2 = "this is the document text string";
        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");
        URI uri2 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        DocumentImpl doc2 = new DocumentImpl(uri2, initialString2, (initialString2.hashCode() & 0x7fffffff));
        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");
        DocumentPersistenceManager dpm = new DocumentPersistenceManager(file);

        dpm.serialize(uri1, doc);
        dpm.serialize(uri2, doc2);

        Document docfinal = dpm.deserialize(uri1);
       assertEquals(doc.getDocumentAsTxt(), docfinal.getDocumentAsTxt());
       assertEquals(doc.getKey(), docfinal.getKey());
       assertEquals(doc.getWordMap(), docfinal.getWordMap());
       assertNotEquals(doc.getLastUseTime(), docfinal.getLastUseTime());
       File testFile = new File("C:\\Users\\yitzy\\Documents\\ForTestingStage5\\raw.githubusercontent.com\\Yeshiva-University-CS\\DataStructuresProjectSpring2020\\master\\project");
       assertTrue(testFile.exists());
        dpm.deserialize(uri2);
        assertFalse(testFile.exists());

    }

 */


}