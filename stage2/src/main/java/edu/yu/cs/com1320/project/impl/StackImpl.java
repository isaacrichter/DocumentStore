package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.HashTableImpl;

public class StackImpl<T> implements Stack<T> {

                private StackItem top;

        public StackImpl(){}

        /**
         * @param element object to add to the Stack
         */
        public void push(T element){
                new StackItem(element);
        }

        /**
         * removes and returns element at the top of the stack
         * @return element at the top of the stack, null if the stack is empty
         */
        public T pop() {
                if (top == null){
                        return null;
                }else{
                        T popped = top.StackedItem;
                        top = top.underMe;
                        return popped;
                }
        }


        /**
         *
         * @return the element at the top of the stack without removing it
         * (return null if stack is empty)
         */
        public T peek(){
                if (top == null){
                        return null;
                }else {
                        return top.StackedItem;
                }
        }

        /**
         *
         * @return how many elements are currently in the stack
         */
        public int size(){
                if (top == null){
                        return 0;
                }else{
                        int itemsInStack = 1;
                        StackItem iterator = top;
                        while (iterator.underMe != null) {
                                itemsInStack++;
                                iterator = iterator.underMe;
                        }
                        return itemsInStack;
                }
        }




        class StackItem{
                 private T StackedItem;
                 private StackItem underMe;

                StackItem(T object){
                        StackedItem = object;
                        this.underMe = top;
                        top = this;
                }
        }
    }

