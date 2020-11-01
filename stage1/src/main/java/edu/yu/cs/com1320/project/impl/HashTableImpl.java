package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;


public class HashTableImpl<Key,Value> implements HashTable<Key,Value>{
    private final int hashTableSize = 5;
    private ListContainer<Key,Value>[] hashTable;

    public HashTableImpl(){
        hashTable = new ListContainer[hashTableSize];

    }



    /**
     * @param k the key whose value should be returned
     * @return the value that is stored in the HashTable for k, or null if there is no such key in the table
     */

    public Value get(Key k){
        // calculate the index for where to find the correct ListContainer
        // if the index is empty, return null, since no matching key
        int index = (k.hashCode() & 0x7fffffff) % hashTableSize;
        if (hashTable[index] == null) {
            return null;
        }   // if first key in the array equals the key were trying to find, return its value
        else if (hashTable[index].key.equals(k)) {
            return hashTable[index].value;
        } else {
            // get the first ListContainer object in the array index and call it temp, in order to be able to move
            // through the list. if there is a next objects in the list, compare its key to the one
            // we are trying to find, and if they are equal, return the value.
            ListContainer<Key, Value> temp = hashTable[index];
            while (temp.next != null) {
                if (temp.next.key.equals(k)) { // compare key of next object with that of the newListContainer
                    return temp.next.value;    // if they match, send back old value and replace
                }else{temp = temp.next;}
            }  //if there is no next item in the ListContainer list at this point,
            // no keys have matched so return null as per the API
            return null;

        }
    }

    /**
     * @param k the key at which to store the value
     * @param v the value to store
     * @return if the key was already present in the HashTable, return the previous value stored for the key. If the key was not already present, return null.
     * #also deals with deleting in case where Value is null
     */

    public Value put(Key k, Value v) {
        int index = (k.hashCode() & 0x7fffffff) % hashTableSize;        //calculate the index for the newListContainer
        ListContainer<Key, Value> newListContainer = new ListContainer<Key, Value>(k, v);    //instantiate it
        if (hashTable[index] == null) {    //if the index is empty, place newListContainer there
            if(newListContainer.value == null) {
                { return null; }
            }else {
                hashTable[index] = newListContainer;
                return null; //as per the API
            }
        } else if (hashTable[index].key.equals(newListContainer.key) && newListContainer.value == null) {
                        if (hashTable[index].next != null) {  // if it is the only item in that array slot,
                            ListContainer<Key, Value> temp = hashTable[index];
                            hashTable[index] = null;  // delete by emptying slot
                            return temp.value;//////////////////////////////////////////////////////////////////////////
                        } else {// remove it by setting its .next to the first item of the list
                            ListContainer<Key, Value> temp = hashTable[index].next;
                            ListContainer<Key, Value> temp2 = hashTable[index];
                            hashTable[index] = temp;
                            return temp2.value; // as per Piazza
                        }
        }else if (hashTable[index].key.equals(newListContainer.key)) {// case: key of first item matches
            ListContainer<Key, Value> temp = hashTable[index];
            hashTable[index] = newListContainer; //replace
            if (temp.next != null) {            // makes sure you don't lose the rest of the list!
                newListContainer.next = temp.next;
            }
            return temp.value; //return the replaced

        } else { // iterate through the rest of the list to see if there is a match for the key
            ListContainer<Key, Value> temp = hashTable[index];
            while (temp.next != null) {
                //compare key of next object with that of the newListContainer if they match, send back old value and replace
                if (temp.next.key.equals(newListContainer.key) && newListContainer.value == null) {
                        // if it is the only item left in that array slot, delete by setting prev to null
                    ListContainer<Key, Value> temp2 = temp.next;
                    if (temp.next.next == null) {
                        temp.next = null; return temp2.value;
                    }
                    else { temp.next = temp.next.next;}   // remove it by skipping it in prev.next
                    return temp2.value;  // return the replaced

                }  else if (temp.next.key.equals(newListContainer.key)) {
                    // .next is match, hold it in temp2, set .next to newListContainer
                    ListContainer<Key, Value> temp2 = temp.next;
                    temp.next = newListContainer;
                    if (temp2.next != null) {       // makes sure you don't lose the rest of the list!
                        newListContainer.next = temp2.next;
                    }
                    return temp2.value;
                }
                temp = temp.next;
            }
            //if there is no next item in the ListContainer list and no keys have matched, loop will exit
            temp.next = newListContainer;
            return null;
        }
    }

    class ListContainer<Key2, Value2>{

        private Key2 key;
        private Value2 value;
        private ListContainer<Key2,Value2> next;

        ListContainer(Key2 k, Value2 v){
            key = k;
            value = v;
            next = null;
        }
    }

}