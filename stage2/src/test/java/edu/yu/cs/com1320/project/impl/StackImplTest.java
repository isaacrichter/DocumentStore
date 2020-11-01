package edu.yu.cs.com1320.project.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class StackImplTest {

    @Test
    public void pushNPeekTest(){
        StackImpl<Integer> theStack = new StackImpl<Integer>();
        theStack.push(1);
        assertEquals(theStack.peek(), (Integer)1);
        theStack.push(2);
        assertEquals(theStack.peek(), (Integer)2);
        theStack.push(3);
        assertEquals(theStack.peek(), (Integer)3);
    }

    @Test
    public void popTest(){
        StackImpl<Integer> theStack = new StackImpl<Integer>();
        theStack.push(1);
        theStack.push(2);
        assertEquals(theStack.pop(), (Integer)2);
        assertEquals(theStack.peek(), (Integer)1);
        theStack.push(3);
        assertEquals(theStack.pop(), (Integer)3);
        assertEquals(theStack.pop(), (Integer)1);
        assertNull(theStack.peek());
        assertNull(theStack.pop());


    }


    @Test
    public void sizeTest(){
        StackImpl<Integer> theStack = new StackImpl<Integer>();
        theStack.push(1);
        theStack.push(2);
        theStack.push(3);
        theStack.push(4);
        assertEquals(theStack.size(), 4);
        theStack.pop();
        theStack.pop();
        assertEquals(theStack.size(), 2);
    }

    @Test
    public void nullTest() {
        StackImpl<Integer> theStack = new StackImpl<Integer>();
        assertNull(theStack.peek());
        assertNull(theStack.pop());
        assertNull(theStack.pop());
        assertNull(theStack.pop());
        assertNull(theStack.pop());
        assertNull(theStack.peek());
        assertNull(theStack.peek());
        assertNull(theStack.peek());
    }

}