package edu.yu.cs.com1320.project.impl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MinHeapImplTest {

    @Test
    public void heapInsertWithArrayDoubleTest(){
        MinHeapImpl<Integer> minHeap = new MinHeapImpl<Integer>();

        minHeap.insert(1000);
        minHeap.insert(2000);

    }


    //Must set initial size of array to 1,2 or 4 for it to pass
/*
    @Test
    public void doubleArraySize(){
        MinHeapImpl<Integer> minHeap = new MinHeapImpl<Integer>();

        minHeap.insert(1000);
        minHeap.insert(2000);

        Integer[] checker = new Integer[4];
        checker[1] = 1000;
        checker[2] = 2000;
        assertArrayEquals(minHeap.elements, checker);

    }


/*
    @Test
    public void removeMinTest(){
        //also tests that after inserting the order changes properly
        MinHeapImpl<Integer> minHeap = new MinHeapImpl<Integer>();

        minHeap.insert(5000);
        minHeap.insert(4000);
        minHeap.insert(3000);
        minHeap.insert(2000);
        minHeap.insert(1000);
        minHeap.removeMin();
        Integer[] checker = {null,2000,3000,4000,5000,null,null,null};
        assertArrayEquals(minHeap.elements, checker);
    }
*/
    @Test
    public void elementsToArrayIndexTest(){
        //also tests that after inserting the order changes properly
        MinHeapImpl<Integer> minHeap = new MinHeapImpl<Integer>();

        minHeap.insert(5000);
        minHeap.insert(4000);
        minHeap.insert(3000);
        minHeap.insert(2000);
        minHeap.insert(1000);
        minHeap.removeMin();
        minHeap.insert(1000);
        Map<Integer,Integer> checker = new HashMap<>();
        checker.put(1000,1);
        checker.put(2000,2);
        checker.put(4000,3);
        checker.put(5000,4);
        checker.put(7,5);// just to make sure its not working because of a stupid mistake


        assertEquals(minHeap.elementsToArrayIndex.get(1000), checker.get(1000));
        assertEquals(minHeap.elementsToArrayIndex.get(2000), checker.get(2000));
        assertEquals(minHeap.elementsToArrayIndex.get(4000), checker.get(4000));
        assertEquals(minHeap.elementsToArrayIndex.get(5000), checker.get(5000));
        assertEquals(minHeap.elementsToArrayIndex.get(5000), checker.get(5000));
        assertEquals(minHeap.elementsToArrayIndex.get(3000), checker.get((7)));

    }



}