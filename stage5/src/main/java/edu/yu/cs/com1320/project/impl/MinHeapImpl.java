package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable> extends MinHeap<E> {
    protected  E[] elements;

    protected int count;
    protected Map<E,Integer> elementsToArrayIndex; //used to store the index in the elements array

    public MinHeapImpl(){
        elements = (E[])new Comparable[8];
        count = 0;
        elementsToArrayIndex = new HashMap<E,Integer>(8) ;
    }



    public void reHeapify(E element){
        upHeap(getArrayIndex(element));
        downHeap(getArrayIndex(element));
        // If the element is in the correct spot in the heap, then do nothing
        // If it was recently changed, move it down until its in the correct spot
        // If a doc was deleted and needs to be moved to the top of the heap, that means its time was changed to be
        //  oldest and it needs to be upheaped to the top so it can be popped off
    }

    protected  int getArrayIndex(E element){
        if(elementsToArrayIndex.get(element) == null){
            return  0;
        }
        else {
            return elementsToArrayIndex.get(element);
        }
    }

    protected void doubleArraySize() {
        E[] tempArray= (E[])new Comparable[2 * (this.elements.length)];
        for (int i = 0 ; i < this.elements.length ; i++){
            tempArray[i] = this.elements[i];
        }
        this.elements = tempArray;
    }

    /**
     * swap the values stored at elements[i] and elements[j]
     */
    @Override
    protected  void swap(int i, int j)
    {
        E temp = this.elements[i];
        this.elements[i] = this.elements[j];
        this.elements[j] = temp;

        elementsToArrayIndex.put(this.elements[i],i);
        elementsToArrayIndex.put(this.elements[j],j);

    }

    /**
     *while the key at index k is less than its
     *parent's key, swap its contents with its parent’s
     */
    @Override
    protected  void upHeap(int k)
    {


        while (k > 1 && this.isGreater(k / 2, k))
        {
            this.swap(k, k / 2);
            k = k / 2;
        }
    }


    /**
     * is elements[i] > elements[j]?
     */
    protected  boolean isGreater(int i, int j)
    //if first is bigger, returns true
    {

        return this.elements[i].compareTo(this.elements[j]) > 0;

    }


    /**
     * move an element down the heap until it is less than
     * both its children or is at the bottom of the heap
     */
    @Override
    protected  void downHeap(int k)
    {
        while (2 * k <= this.count)
        {
            //identify which of the 2 children are smaller
            int j = 2 * k;
            if (j < this.count && this.isGreater(j, j + 1))
            {
                j++;
            }
            //if the current value is < the smaller child, we're done
            if (!this.isGreater(k, j))
            {
                break;
            }
            //if not, swap and continue testing
            this.swap(k, j);
            k = j;
        }
    }

    @Override
    public void insert(E x)
    {
        if(elementsToArrayIndex.keySet().contains(x)){
            return;
        }
        // double size of array if necessary
        if (this.count >= this.elements.length - 1)
        {
            this.doubleArraySize();
        }
        //add x to the bottom of the heap
        this.elements[++this.count] = x;
        elementsToArrayIndex.put(x,this.count);

        //percolate it up to maintain heap order property
        this.upHeap(this.count);
    }

    @Override
    public E removeMin()
    {
        if (elements[1]==null)
        {
            throw new NoSuchElementException("Heap is empty");
        }
        E min = this.elements[1];
        //swap root with last, decrement count
        this.swap(1, this.count--);
        elementsToArrayIndex.remove(min);
        //move new root down as needed
        this.downHeap(1);
        this.elements[this.count + 1] = null; //null it to prepare for GC
        return min;
    }






}
