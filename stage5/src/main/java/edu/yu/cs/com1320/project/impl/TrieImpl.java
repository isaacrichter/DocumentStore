package edu.yu.cs.com1320.project.impl;


import edu.yu.cs.com1320.project.Trie;

import java.util.*;
public class TrieImpl<Value>  implements  Trie<Value>{
    private static final int alphabetSize = 256; // extended ASCII
    private TrieImpl.Node<Value> root; // root of trie

    public TrieImpl(){
        root = new TrieImpl.Node<Value>();
    }


    public static class Node<Value>
    {
        protected List<Value> val = new ArrayList<Value>();
        protected TrieImpl.Node<Value>[] links = new TrieImpl.Node[TrieImpl.alphabetSize];
        private Node<Value> parent;
    }
    /**
     * add the given value at the given key
     * @param key
     * @param val
     */
    public void put(String key, Value val){
        String treatedKey = depunctuateAndUppercase(key);

        //deleteAll the value from this key
        if (val == null)
        {
            this.deleteAll(treatedKey);
        }
        else
        {
            this.root = put(this.root, treatedKey, val, 0, null);
        }
    }

    private TrieImpl.Node<Value> put(TrieImpl.Node<Value> x, String key, Value val, int d,TrieImpl.Node<Value> parent) {
        //create a new node
        if (x == null)
        {
            x = new TrieImpl.Node<Value>();
        }
        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (d == key.length())
        {

            x.val.add(val);
            x.parent = parent;
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        x.links[c] = this.put(x.links[c], key, val, d + 1, x);
        return x;
    }

    /**
     * get all exact matches for the given key, sorted in descending order.
     * Search is CASE INSENSITIVE.
     * @param key
     * @param comparator used to sort  values
     * @return a List of matching Values, in descending order
     */
    public List<Value> getAllSorted(String key, Comparator<Value> comparator){
        String treatedKey = depunctuateAndUppercase(key);
        TrieImpl.Node<Value> x = this.get(this.root, treatedKey, 0);

        if (x == null)
        {
            return new ArrayList<Value>();
        }
        Collections.sort(x.val, comparator);
        return x.val;
    }


    private TrieImpl.Node<Value> get(TrieImpl.Node<Value> x, String key, int d)
    {
        //link was null - return null, indicating a miss
        if (x == null)
        {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length())
        {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.links[c], key, d + 1);
    }

    /**
     * get all matches which contain a String with the given prefix, sorted in descending order.
     * For example, if the key is "Too", you would return any value that contains "Tool", "Too", "Tooth", "Toodle", etc.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @param comparator used to sort values
     * @return a List of all matching Values containing the given prefix, in descending order
     */
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator){
        // 1) get all the docs that have words with that prefix
        // 2) sort using the comparator that is a lambda from the document store.
        // 3) return
        String treatedPrefix = depunctuateAndUppercase(prefix);
        TrieImpl.Node<Value> x = this.get(this.root, treatedPrefix, 0);
        if(x==null){return new ArrayList<Value>();}
        List<Value> valueList = new ArrayList<Value>();
        HashSet<Value> noDoubleSet = new HashSet<Value>(); // to test for duplicates before adding to valueList
        valueList = traverseAndAddValues(x, valueList, noDoubleSet);
        //method that counts number of times the prefix shows up
        Collections.sort(valueList, comparator);// awesomely awesome comparator passed in
        return valueList;
    }


    private List<Value> traverseAndAddValues(TrieImpl.Node<Value> x, List<Value> valueList, HashSet<Value> noDoubleSet){
        //noDoubleSet is used to test for duplicates before adding to valueList
        if(x != null) {
            if (hasChildren(x)) {
                for (TrieImpl.Node<Value> node : x.links) {
                    if (node != null) {
                        traverseAndAddValues(node, valueList, noDoubleSet);
                    }
                }
            }

            if (x.val != null) {
                for (Value singleVal : x.val) {
                    if (noDoubleSet.add(singleVal))
                        valueList.add(singleVal);
                }
            }
        }
        return valueList;
    }

    private  String depunctuateAndUppercase(String word) {
        String depunctuated = word.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();          // convert to uppercase
        return uppercased;
    }
    /**
     * Delete the subtree rooted at the last character of the prefix.
     * Search is CASE INSENSITIVE.
     * @param prefix
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAllWithPrefix(String prefix){
        String treatedKey = depunctuateAndUppercase(prefix);
        TrieImpl.Node<Value> x = this.get(this.root, treatedKey, 0);
        Set<Value> deletedSet = new HashSet<Value>();
        if (x!=null){

            Set<Value> completedDeletedSet = traverseDelete(x,deletedSet);

            TrieImpl.Node<Value> par ;
            par =
                    x.parent;
            trimTrieFromEmptyNode(par);


            return completedDeletedSet;
        }
        else{return  null;}
    }

    private Set<Value> traverseDelete(Node<Value> x, Set<Value> deletedSet) {
        if(x.links !=null ) {

            for (TrieImpl.Node<Value> y : x.links) { // im asking for npe's here...
                if (y != null) {
                    traverseDelete(y, deletedSet);
                }
            }
        }
        if (x.parent != null) {
            for (int i = 0; i < 256; i++) {
                if (x.parent.links[i] == x) {
                    for (Value vals : x.val) {
                        deletedSet.add(vals);
                    }

                    x.parent.links[i] = null;
                    return deletedSet;
                }

            }
        }
        return deletedSet;
    }

    /**
     * Delete all values from the node of the given key (do not remove the values from other nodes in the Trie)
     * @param key
     * @return a Set of all Values that were deleted.
     */
    public Set<Value> deleteAll (String key){
        String treatedKey = depunctuateAndUppercase(key);

        TrieImpl.Node<Value> x = this.get(this.root, treatedKey, 0);

        Set<Value> set = new HashSet<Value>();
        for (Value val : x.val) {
            set.add(val);
        }
        x.val.clear();
        trimTrieFromEmptyNode(x);

        // if the node is clear and does dot have any descendants, trim the trie until non-null node
        // getParent() method may be helpful

        return set;
    }


    private void trimTrieFromEmptyNode(Node<Value> x) {
        if (x == null){
            return;
        }
        TrieImpl.Node<Value> currentNode = x;
        if (hasChildren(currentNode)) {
            return;
        } else
            while (x.val == null || x.val == new ArrayList<Value>()) {
                for (TrieImpl.Node<Value> potentialChild : x.parent.links) {
                    if (potentialChild == x) {
                        potentialChild = null;
                        trimTrieFromEmptyNode(x.parent);
                    }
                }
            }
    }

    private boolean hasChildren(TrieImpl.Node<Value> x){
        for (TrieImpl.Node<Value> y:
                x.links) {
            if (y != null){
                return true;
            }
        }
        return false;
    }

    /**
     * Remove the given value from the node of the given key (do not remove the value from other nodes in the Trie)
     * @param key
     * @param val
     * @return the value which was deleted. If the key did not contain the given value, return null.
     */
    public Value delete(String key, Value val){
        String treatedKey = depunctuateAndUppercase(key);

        TrieImpl.Node<Value> x = this.get(this.root, treatedKey, 0);

        if (x == null) {
            return null;//@@@
        }

        if (x.val.remove(val)){
            trimTrieFromEmptyNode(x);
            return val;// NOT SURE IF THIS IS THE CORRECT RETURN VAL. WORK THROUGH METHOD WHEN TESTING
        }
        return null;
    }
}
