package edu.yu.cs.com1320.project.stage4.impl;


import java.io.ByteArrayOutputStream;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class DocumentImpl implements Document{


    private URI uri;
    private int txtHash;
    private String txt;
    private byte[] pdfBytes;
    protected Map<String, Integer> wordMap;
    private long timeLastUsed;
    //private hashtable to hold number of times words appear


    //constructor method that takes in params of putDocument from document store
    public DocumentImpl(URI uri, String txt, int txtHash){
        this.uri = uri;
        this.txt = txt;
        this.txtHash = (txtHash & 0x7fffffff);
        this.pdfBytes = txtToPdf(txt);
        this.wordMap = mapMyWords(txt);
        //addWordMapToTrie(trie);
        this.timeLastUsed = System.nanoTime();

        // this.hashmap put into trie
    }


    public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes){
        this.uri = uri;
        this.txt = txt;
        this.txtHash = (txtHash & 0x7fffffff);
        this.pdfBytes = pdfBytes;
        this.wordMap = mapMyWords(txt);
        // this.hashmap put into trie
    }

    private Map<String, Integer> mapMyWords(String text) {
        String[] wordArray = textToReadyToMapWordArray(text);
        this.wordMap =  new HashMap<String, Integer>();
        int hits = 0;

        for (String word: wordArray) {
            // if the hashmap has this word in it already, get the number of hits, add one, and re-put
            if (wordMap.containsKey(word)) {
                hits = wordMap.get(word);
                wordMap.put(word, hits + 1);
                //
                // else its the first hit so put it with hits (the value) == 1
            } else {
                wordMap.put(word, 1);
            }
        }
        return wordMap;
    }

    private void addWordMapToTrie(TrieImpl<DocumentImpl> trie){

        for (String key : wordMap.keySet()) {
            trie.put(key,this);
        }
    }

    private String[] textToReadyToMapWordArray(String text){
        String depunctuated = text.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();                            // convert to uppercase
        String[] wordArray = uppercased.split(" ");                         // convert to word array
        return  wordArray;
    }




    private byte[] txtToPdf(String txt){
        try{
            if (txt == null){return null;} // avoids null pointer exception and still allows the doc to construct
            else {
                // create document, add page, and then saves it. still need to actually turn it into pdf
                PDDocument pdDoc = new PDDocument();
                PDFont font = PDType1Font.TIMES_ROMAN;
                PDPage page = new PDPage();
                pdDoc.addPage(page);
                PDPageContentStream streamToDoc = new PDPageContentStream(pdDoc, page);
                streamToDoc.beginText();
                streamToDoc.setFont(font, 12);
                streamToDoc.showText(txt);
                streamToDoc.endText();
                streamToDoc.close();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                pdDoc.save(baos); //saves file to output stream
                pdDoc.close(); //closes the doc
                return baos.toByteArray();
            }
        } catch (Exception e) {e.printStackTrace(); return null;}
    }


    /**
     * @return the document as a PDF
     */
    public byte[] getDocumentAsPdf(){
        return pdfBytes;
    }

    /**
     * @return the document as a Plain String
     */
    public String getDocumentAsTxt(){
        return txt;
    }

    /**
     * @return hash code of the plain text version of the document
     */
    public int getDocumentTextHashCode(){
        return txt.hashCode();
    }

    /**
     * @return URI which uniquely identifies this document
     */
    public URI getKey(){
        return uri;
    }

    /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document
     */
    public int wordCount(String word){
        String treatedWord = depunctuateAndUppercase(word);
        if(wordMap.get(treatedWord) == null){
            return 0;
        }


        return wordMap.get(treatedWord);
    }


    private  String depunctuateAndUppercase(String word) {
        String depunctuated = word.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();          // convert to uppercase
        return uppercased;
    }

    protected Map<String, Integer> getWordMap(){
        return this.wordMap;
    }

    public long getLastUseTime(){
        return this.timeLastUsed;
    }
    public void setLastUseTime(long timeInMilliseconds){
        this.timeLastUsed = timeInMilliseconds;
    }

    public int compareTo(Document document){
        //Not sure if im returning the right things
        //if this doc was used less recently return 1
        if(this.getLastUseTime() > document.getLastUseTime()){
            return 1;
            //if this doc was used more recently return -1
        } else if (document.getLastUseTime() > this.getLastUseTime()){
            return -1;
        }else{return 0;}
    }


}

