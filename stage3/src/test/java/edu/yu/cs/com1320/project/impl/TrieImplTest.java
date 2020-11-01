package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import org.junit.Test;

import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.*;

public class TrieImplTest {

    @Test
    public void getAllSortedTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        trie.put("one","1");
        trie.put("one","11");
        trie.put("one","111");
        trie.put("one","1111");

        trie.put("two","2");
        trie.put("three","3");

        Comparator<String> stringComparator =
                (String a, String b) -> b.length() - a.length();
        ArrayList<String> results= new ArrayList<>();
        results.add("1111");
        results.add("111");
        results.add("11");
        results.add("1");

        assertEquals(trie.getAllSorted("one",  stringComparator),results);
    }

    @Test
    public void getAllWithPrefixSortedTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        trie.put("one","1");
        trie.put("onesuffix","11");
        trie.put("oNe_sufffiX","111");
        trie.put("onee","1111");

        trie.put("two","2");
        trie.put("three","3");

        Comparator<String> stringComparator =
                (String a, String b) -> b.length() - a.length();
        ArrayList<String> results= new ArrayList<>();
        results.add("1111");
        results.add("111");
        results.add("11");
        results.add("1");

        assertEquals(trie.getAllWithPrefixSorted("one",  stringComparator),results);
    }

    @Test
    public void getAllWithPrefixSortedEmptyTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        Comparator<String> comparator =
                (String string1, String string2) -> string2.length() - string1.length();
        assertEquals(trie.getAllWithPrefixSorted("yo", comparator),new ArrayList<String>());
    }

    @Test
    public void getAllSortedEmptyTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        Comparator<String> comparator =
                (String string1, String string2) -> string2.length() - string1.length();
        assertEquals(trie.getAllSorted("yo", comparator),new ArrayList<String>());
    }

    @Test
    public void microtargetedDeleteMethodTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        trie.put("she","1she");
        trie.put("sells","1sells");
        trie.put("sea","1sea");
        trie.put("shells","1shells");

        Comparator<String> comparator =
                (String string1, String string2) -> string2.length() - string1.length();
        ArrayList<String> expected =  new ArrayList<String>();
        expected.add("1she");
        assertEquals(trie.getAllSorted("she", comparator),expected);
        trie.delete("she", "1she");
        expected.clear();
        assertEquals(trie.getAllSorted("she", comparator), expected);

    }

    @Test
    public void deleteAllMethodTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        trie.put("she","1she");
        trie.put("sells","1sells");
        trie.put("sea","1sea    ");
        trie.put("sea","2sea   ");
        trie.put("sea","3sea  ");
        trie.put("sea","4sea ");

        trie.put("shells","1shells");

        Comparator<String> comparator =
                (String string1, String string2) -> string2.length() - string1.length();
        ArrayList<String> expected =  new ArrayList<String>();
        expected.add("1sea    ");
        expected.add("2sea   ");
        expected.add("3sea  ");
        expected.add("4sea ");

        assertEquals(trie.getAllSorted("sea", comparator),expected);
        trie.deleteAll("she");
        expected.clear();
        assertEquals(trie.getAllSorted("she", comparator), expected);

    }

    @Test
    public void deleteAllWithPrefixMethodTest() throws URISyntaxException {
        TrieImpl<String> trie = new TrieImpl<String>();
        trie.put("she","1she");
        trie.put("slls","1sells");
        trie.put("sel","1sea    ");
        trie.put("seer","2sea   ");
        trie.put("seb","3sea  ");
        trie.put("s''E","4sea ");
        trie.put("shells","1shells");

        Comparator<String> comparator =
                (String string1, String string2) -> string2.length() - string1.length();
        ArrayList<String> actual =  new ArrayList<String>();
        actual.add("1sea    ");
        actual.add("2sea   ");
        actual.add("3sea  ");
        actual.add("4sea ");

        assertEquals(trie.getAllWithPrefixSorted("se", comparator),actual);

        trie.deleteAllWithPrefix("se");
        actual.clear();
        assertEquals(trie.getAllWithPrefixSorted("se", comparator), actual);
        actual.add("1shells");
        actual.add("1sells");
        actual.add("1she");
        assertEquals(trie.getAllWithPrefixSorted("s,", comparator), actual);


    }
}