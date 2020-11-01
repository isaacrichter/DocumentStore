package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentPersistenceManager;
import org.junit.Test;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class BTreeImplTest {

    @Test
    public void basicPutGetTest() {
        BTreeImpl<Integer, String> bTree = new BTreeImpl<Integer, String>();
        bTree.put(0, "sentinel");

        String[] nums = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
        for (int i = 1; i < 10; i++) {
            bTree.put(i, nums[i]);
        }

        for (int i = 1; i < 10; i++) {
            assertEquals(bTree.get(i), nums[i]);
        }

        for (int i = 1; i < 10; i++) {
            bTree.put(i, null);
        }
        for (int i = 1; i < 10; i++) {
            assertNull(bTree.get(i));
        }
    }

    @Test
    public void backwardsPutGetTest() {
        BTreeImpl<Integer, String> bTree = new BTreeImpl<Integer, String>();
        bTree.put(0, "sentinel");

        //String[] nums = {"nine", "eight", "seven", "six", "five", "four", "three", "two", "one","zero" };
        String[] nums = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve", "thirteen",
                "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen", "twenty", "twenty one"};

        for (int i = 21; i > 0; i--) {
            bTree.put(i, nums[i]);
        }
        for (int i = 21; i > 0; i--) {
            assertEquals(bTree.get(i), nums[i]);
        }

        for (int i = 1; i < 22; i++) {
            bTree.put(i, null);
        }
        for (int i = 1; i < 22; i++) {
            assertNull(bTree.get(i));
        }

    }


    @Test
    public void basicPersistenceManagerTest() throws Exception {
        BTreeImpl<URI, Document> bTree = new BTreeImpl<URI, Document>();
        bTree.put(new URI(""), null); // put sentinal

        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");

        DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(file);
        bTree.setPersistenceManager(persistenceManager);

        String initialString1 = "this is the document text string";
        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        bTree.put(uri1, doc);
        bTree.moveToDisk(uri1);

        //check that val is not stored as an obj
        bTree.setPersistenceManager(null);
        assertNull(bTree.get(uri1));

        //check val is stored as json
        bTree.setPersistenceManager(persistenceManager);
        assertEquals(bTree.get(uri1).getDocumentAsTxt(), doc.getDocumentAsTxt());
        assertFalse(new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5\\raw.githubusercontent.com").exists());
    }

    @Test
    public void MoveEntryToDiskAndDeleteTest() throws Exception {
        BTreeImpl<URI, Document> bTree = new BTreeImpl<URI, Document>();
        bTree.put(new URI(""), null); // put sentinal

        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");

        DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(file);
        bTree.setPersistenceManager(persistenceManager);

        String initialString1 = "this is the document text string";
        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        bTree.put(uri1, doc);
        bTree.moveToDisk(uri1);

        //check that val is not stored as an obj


        bTree.setPersistenceManager(null);
        Document docReturned = bTree.get(uri1);

        //System.out.println(docReturned);

        bTree.setPersistenceManager(persistenceManager);
        Document docReturned2 = bTree.get(uri1);

        assertEquals(docReturned2.getDocumentAsTxt(), doc.getDocumentAsTxt());

        bTree.setPersistenceManager(null); // checking that the get call from before moved it to memory
        assertEquals(docReturned2.getDocumentAsTxt(), doc.getDocumentAsTxt());


        //assertNull(bTree.get(uri1));// should it b null???



    }

    @Test
    public void replaceFileonDiskTest() throws Exception {

        BTreeImpl<URI, Document> bTree = new BTreeImpl<URI, Document>();
        bTree.put(new URI(""), null); // put sentinal

        File file = new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5");

        DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(file);
        bTree.setPersistenceManager(persistenceManager);

        String initialString1 = "this is the document text string";
        String initialString2 = "this is the second document text string";

        URI uri1 = new URI("https://raw.githubusercontent.com/Yeshiva-University-CS/DataStructuresProjectSpring2020/master/project/stage4/src/test/java/edu/yu/cs/com1320/project/stage4/impl/Doc");

        DocumentImpl doc = new DocumentImpl(uri1, initialString1, (initialString1.hashCode() & 0x7fffffff));
        bTree.put(uri1, doc);
        bTree.moveToDisk(uri1);
        DocumentImpl doc2 = new DocumentImpl(uri1, initialString2, (initialString2.hashCode() & 0x7fffffff));
        Document returnedDoc = bTree.put(uri1,doc2);
        //System.out.println(bTree.get(uri1).getDocumentAsTxt());


        //check that putting i a replacement replaces the old even when it is on disk
        assertEquals(returnedDoc.getDocumentAsTxt(), initialString1);
        //check that created dirs were deleted
        assertFalse(new File("\\C:\\Users\\yitzy\\Documents\\ForTestingStage5\\raw.githubusercontent.com").exists());

        //checks that its the second version is in memory
        assertNull(bTree.persistenceManager.deserialize(uri1));
        assertEquals(bTree.get(uri1).getDocumentAsTxt(), initialString2);

    }




}