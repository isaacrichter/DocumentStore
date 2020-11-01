package edu.yu.cs.com1320.project.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class HashTableImplTest {

    @Test
    public void initializerTest(){
        HashTableImpl<String,String> HashTable = new HashTableImpl<String, String>();
        //assertEquals(5 ,HashTable.hashTableSize);
    }

    @Test
    public void basicPutnGetTest(){
        HashTableImpl<String,String> HashTable = new HashTableImpl<String, String>();
        HashTable.put("Yitzy","Richter");
        HashTable.put("Mayer","Adelman");
        HashTable.put("Shai","Vadnai");
        HashTable.put("Aaron","Brooks");
        HashTable.put("Ezra","Wildes");
        HashTable.put("Yair","Caplan");
        HashTable.put("Tani","Motechin");
        HashTable.put("Zach","Fish");
        HashTable.put("Daniel","Zolty");
        HashTable.put("Dovid","Gelbtuch");
        HashTable.put("Elie","Shapiro");
        HashTable.put("Jonathan","Haller");
        HashTable.put("Aryeh","Steinfeld");
        HashTable.put("Yonatan","Berner");
        assertEquals("Richter",HashTable.get("Yitzy"));
        assertEquals("Fish",HashTable.get("Zach"));
        assertEquals("Shapiro",HashTable.get("Elie"));
        assertEquals("Berner",HashTable.get("Yonatan"));
        System.out.println("stage 2 test");
        //System.out.println(HashTable.hashTableSize);
        //System.out.println(HashTable.itemsInTableCount);

    }


    @Test
    public void basicReplaceTest() {
        HashTableImpl<String, String> HashTable = new HashTableImpl<String, String>();
        HashTable.put("Richter", "Yitzy");
        HashTable.put("Richter", "Isaac");
        assertEquals("Isaac", HashTable.get("Richter"));
    }

    @Test
    public void basicAttemptDeleteTest() {
        // attempt to delete something not in HashTable
        HashTableImpl<String, String> HashTable = new HashTableImpl<String, String>();
        HashTable.put("Richter", null);
        assertNull(HashTable.get("Richter"));
    }


    @Test
    public void basicDeleteTest() {
        //delete something from hashtable, test if returns deleted value,
        // and that nothing with the deleted key is remains
        HashTableImpl<String, String> HashTable = new HashTableImpl<String, String>();
        HashTable.put("Richter", "Yitzy");
        String whatWasDeleted = HashTable.put("Richter", null);
        assertEquals("Yitzy", whatWasDeleted);
        assertNull(HashTable.get("Richter"));
    }


    @Test
    public void intermediateDeleteTest() {
        //delete something from hashtable in overloaded slot (using multiples of five to do this),
        // test that the others in the list in that array spot are still there
        HashTableImpl<Integer, String> HashTable = new HashTableImpl<Integer, String>();
        HashTable.put(0, "zero");
        HashTable.put(5, "five");
        HashTable.put(10, "ten");
        HashTable.put(15, "fifteen");
        HashTable.put(20, "twenty");
        // delete early ones, test to see if they were deleted,
        // and get the ones that follow to test they weren't dropped
        HashTable.put(5, null);
        HashTable.put(15, null);
        assertEquals("zero", HashTable.get(0));
        assertEquals("ten", HashTable.get(10));
        assertEquals("twenty", HashTable.get(20));


    }

    @Test
    public void addingDoubleOfSamePair(){
        HashTableImpl<Integer, String> HashTable = new HashTableImpl<Integer, String>();
        HashTable.put(0, "zero");
        String returnVal = HashTable.put(0,"zero");
        //System.out.println(returnVal);


    }

    @Test
    public void MassivePutAndGetTest() {
        //delete something from hashtable in overloaded slot (using multiples of five to do this),
        // test that the others in the list in that array spot are still there
        HashTableImpl<Integer, String> HashTable = new HashTableImpl<Integer, String>();
        HashTable.put(0, "zero");
        //String zeroReplaceString1 = HashTable.put(0, "0");
        HashTable.put(1, "one");
        HashTable.put(2, "two");
        HashTable.put(3, "three");
        HashTable.put(4, "four");
        HashTable.put(5, "twenty");
        HashTable.put(6, "zero");
        HashTable.put(7, "five");
        HashTable.put(8, "ten");
        HashTable.put(9, "fifteen");
        HashTable.put(10, "ten");
        String fiveReplacesString = HashTable.put(5, "5");
        HashTable.put(15, "fifteen");
        HashTable.put(16, "sixteen");
        HashTable.put(20, "twenty");
        String zeroReplaceString2 = HashTable.put(0, "ZERO");
        HashTable.put(23, "twenty three");
        HashTable.put(37, "thirty seven");
        HashTable.put(37, "thirty seven");
        String thirtySevenSelfReplaced = HashTable.put(37, "thirty seven");
        HashTable.put(87, "eighty seven");
        HashTable.put(90, "ninety");
        HashTable.put(100, "one hundred");
        String OneHundredReplaceString = HashTable.put(100, "100");
        // delete early ones, test to see if they were deleted,
        // and get the ones that follow to test they weren't dropped
        HashTable.put(5, null);
        HashTable.put(15, null);
        HashTable.put(20, null);
        HashTable.put(90, null);
        assertNull(HashTable.get(15));
        assertEquals("thirty seven", thirtySevenSelfReplaced);
        //assertEquals("zero", zeroReplaceString1);
        //assertEquals("0", zeroReplaceString2);
        //assertEquals("ZERO", HashTable.get(0));
        assertEquals("ten", HashTable.get(10));
        assertEquals("100", HashTable.get(100));
    }

}