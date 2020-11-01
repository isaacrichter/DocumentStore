package edu.yu.cs.com1320.project.stage3.impl;


import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI,DocumentImpl> hashTableImpl;
    private TrieImpl<DocumentImpl> trie;
    private StackImpl<Undoable> commandStack;
    private CommandSet<URI> cmd;
    //need to add command stack when that is ready

    public DocumentStoreImpl() {
        trie = new TrieImpl<DocumentImpl>();
        hashTableImpl = new HashTableImpl<URI, DocumentImpl>();
        commandStack = new StackImpl<Undoable>();
    }

    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the String version of the previous doc.
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */
    public int putDocument(InputStream input, URI uri, edu.yu.cs.com1320.project.stage3.DocumentStore.DocumentFormat format){
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        } else if (input == null) {
            return putDelete(uri);
        } else {
            byte[] byteArray = inputStreamToByteArray(input); // converts input stream to byteArray equivalent
            if (format == DocumentStore.DocumentFormat.TXT) {
                return addTxtDoc(byteArray, uri);
            } else {
                return addPdfDoc(byteArray, uri);
            }
        }
    }

    //delete document with this uri if one exists
    private int putDelete(URI uri) {
        DocumentImpl oldDocument = hashTableImpl.put(uri, null);
        if (oldDocument == null) {
            return 0;
        } else {
            Function<URI, Boolean> undoCommand = undoDeleteCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return oldDocument.getDocumentTextHashCode();
        }
    }

        private Function<URI,Boolean> undoDeleteCommand(URI uri, InputStream inputStream, DocumentStore.DocumentFormat format){

            Function<URI, Boolean> undoDelete = (URI uri1) -> {
                int hash = putDocumentAddNothingToStack(inputStream, uri, format);
                if (hash == 0){return false;}
                else {return true;}
            };
            return  undoDelete;
        }

    private void createAddCommandToStack(URI uri, Function<URI, Boolean> undoCommand){
        GenericCommand genericCommand = new GenericCommand(uri, undoCommand);
        commandStack.push(genericCommand);
    }

    private int putDocumentAddNothingToStack(InputStream input, URI uri, DocumentStore.DocumentFormat format){
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        } else if(input == null){
            DocumentImpl newDocument = new DocumentImpl(uri, null, 0);
            DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);
            if(oldDocument == null){return 0;}
            else{ return oldDocument.getDocumentTextHashCode(); }
        } else {
            byte[] byteArray = inputStreamToByteArray(input); // converts input stream to byteArray equivalent

            if (format == DocumentFormat.TXT) {
                String txt = new String(byteArray);  //converts it to a string, next line creates new DocumentImpl to store
                DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());
                DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
                if(oldDocument == null){return 0;}
                else{ return oldDocument.getDocumentTextHashCode(); }
            } else {
                String txt = PdfByteArrayToString(byteArray);
                txt = txt.trim();
                DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());
                DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
                if(oldDocument == null){return 0;}
                else{ return oldDocument.getDocumentTextHashCode(); }
            }
        }
    }

    private int addTxtDoc(byte[] byteArray, URI uri){
        String txt = new String(byteArray);  //converts it to a string, next line creates new DocumentImpl to store
        DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());
        DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
        if(oldDocument == null){

            Function<URI, Boolean> undoCommand = undoPutCommand(uri);
            createAddCommandToStack(uri, undoCommand);
            return 0;

        }
        else{

            Function<URI, Boolean> undoCommand = undoReplaceCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            //OLD COMMAND CODE

            return oldDocument.getDocumentTextHashCode();
        }
    }

    private Function<URI,Boolean> undoReplaceCommand(URI uri, InputStream inputStream, DocumentStore.DocumentFormat format){

        Function<URI, Boolean> undoReplace = (URI uri1) -> {
            boolean bool1 = deleteDocumentAddNothingToStack(uri);
            int hash = putDocumentAddNothingToStack(inputStream, uri, format); // maybe its supposed to be uri1??
            if (hash != 0 && (bool1)){return true;}
            else {return false;}
        };
        return  undoReplace;
    }


    private Function<URI,Boolean> undoPutCommand(URI uri){
        Function<URI, Boolean> undoPut = (URI uri1) -> {
            return deleteDocumentAddNothingToStack(uri); // maybe its supposed to be uri1??
        };
        return  undoPut;
    }

    private boolean deleteDocumentAddNothingToStack(URI uri){
        DocumentImpl docPreDelete = hashTableImpl.get(uri);
        DocumentImpl docPostDelete = hashTableImpl.put(uri, null);
        if (docPostDelete == null){return false;}
        else if(docPostDelete.equals(docPreDelete)){
            return true;}
        else {return false;}
    }

    private int addPdfDoc(byte[] byteArray, URI uri){
        String txt = PdfByteArrayToString(byteArray);
        txt = txt.trim();
        DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());
        DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
        if (oldDocument == null){
            Function<URI, Boolean> undoCommand = undoPutCommand(uri);
            createAddCommandToStack(uri, undoCommand);
            return 0;
        } else {

            Function<URI, Boolean> undoCommand = undoReplaceCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return oldDocument.getDocumentTextHashCode();
        }
    }

    private byte[] inputStreamToByteArray(InputStream input){
        if(input == null){return null;}
        try {
            //instantiate a ByteArrayOutputStream object
            ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
            // .read() reads the next byte of data from the input stream, and stores it as an int
            int reads = input.read();
            // .read() = -1 if there is no next. while there is a next, write it to the ByteArrayOutputStream
            while (reads != -1) {
                baOutputStream.write(reads);
                reads = input.read(); //moves to next byte of the output stream
            }
            //gets the byte array
            byte[] byteArray = baOutputStream.toByteArray();
            return byteArray;
        }catch (IOException e){ e.printStackTrace(); return null;}
    }

    private String PdfByteArrayToString(byte[] byteArray) {
        try {
            PDDocument document = PDDocument.load(byteArray);  // load the byte array onto a pddocument
            PDFTextStripper stripper = new PDFTextStripper();  //instantiate a textstripper,
            return stripper.getText(document);                // and strip the document into a string, and return
        } catch (IOException e) {e.printStackTrace(); }
        return  null;
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that URI
     */
    public byte[] getDocumentAsPdf(URI uri){
        DocumentImpl document = hashTableImpl.get(uri);
        if(document == null){return null;}
        else{
            return document.getDocumentAsPdf();
        }
    }

    protected DocumentImpl getDocumentAsDocument(URI uri){
        DocumentImpl document = hashTableImpl.get(uri);
        if(document == null){return null;}
        else{
            return document;
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
     */
    public String getDocumentAsTxt(URI uri){
        DocumentImpl document = hashTableImpl.get(uri);
        if(document == null){return null;}
        else{return document.getDocumentAsTxt();}
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri){
        DocumentImpl docPreDelete = hashTableImpl.get(uri);
        DocumentImpl docPostDelete = hashTableImpl.put(uri, null);
        if (docPostDelete == null){return false;}
        else if(docPostDelete.equals(docPreDelete)){
            obliterateSpecificDocFromTrie(docPostDelete);
            return true;
        }

        else {return false;}
    }


    private void obliterateSpecificDocFromTrie(DocumentImpl doc){
        for (String word: doc.getWordMap().keySet()) {
            trie.delete(word,doc);
        }
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException{
        if(commandStack.size() == 0){throw new IllegalStateException();}
        else {
            Undoable commandOrCommandSet = commandStack.pop();
            commandOrCommandSet.undo();
        }
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    public void undo(URI uri) throws IllegalStateException {

        StackImpl<Undoable> helperStack = new StackImpl<Undoable>();
        boolean bool = false;
        while (!bool) {
            Undoable stackObj = commandStack.peek();
            if (stackObj == null) {
                throw new IllegalStateException();
            } else if (stackObj instanceof CommandSet) {
                Iterator<GenericCommand<URI>> iterator = ((CommandSet)stackObj).iterator();
                while (iterator.hasNext()){
                    GenericCommand<URI> genericCommand = iterator.next();
                    if (genericCommand.getTarget() == uri) {
                        this.cmd = (CommandSet<URI>) stackObj;
                        bool = true;
                    }



            }
                undoIt(uri, this.cmd);
                if (((CommandSet<URI>) stackObj).size() == 0){
                    commandStack.pop();
                }
            } else if (stackObj instanceof GenericCommand) {
                if (((GenericCommand) stackObj).getTarget() == uri) {
                    commandStack.pop().undo();
                    bool = true;

                }
            } else {}
                helperStack.push(commandStack.pop());
        }
        //return stack to original state sans the undone command
        while (helperStack.size() != 0) {
            commandStack.push(helperStack.pop());

        }
    }

    private void undoIt(URI uri, CommandSet<URI> cmd){
        cmd.undo(uri);
    }
    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> search(String keyword){
        List<DocumentImpl> sortedDocs = searchDocs(keyword);

        return docListToText(sortedDocs);
    }

    /**
     * same logic as search, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFs(String keyword){
        List<DocumentImpl> sortedDocs = searchDocs(keyword);
        return docListToPdf(sortedDocs);
    }

    private List<DocumentImpl> searchDocs(String keyword){
        Comparator<DocumentImpl> documentComparator =
                (DocumentImpl doc1, DocumentImpl doc2) ->
                        doc2.wordCount(keyword) - doc1.wordCount(keyword);
        List<DocumentImpl> sortedDocs = trie.getAllSorted(keyword, documentComparator);
        return sortedDocs;
    }

    private List<byte[]> docListToPdf(List<DocumentImpl> sortedDocs){
        List<byte[]> sortedPdfDocs = new ArrayList<>();
        for (DocumentImpl doc: sortedDocs) {
            sortedPdfDocs.add(doc.getDocumentAsPdf());
        }
        return sortedPdfDocs;
    }

    private List<String> docListToText(List<DocumentImpl> sortedDocs){
        List<String> sortedTextDocs = new ArrayList<>();
        for (DocumentImpl doc: sortedDocs) {
                sortedTextDocs.add(doc.getDocumentAsTxt());
        }
        return sortedTextDocs;
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> searchByPrefix(String keywordPrefix){
        List<DocumentImpl> sortedDocs = searchDocsByPrefix(keywordPrefix);
        return docListToText(sortedDocs);
    }

    /**
     * same logic as searchByPrefix, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFsByPrefix(String keywordPrefix){
        List<DocumentImpl> sortedDocs = searchDocsByPrefix(keywordPrefix);
        return docListToPdf(sortedDocs);
    }

    private List<DocumentImpl> searchDocsByPrefix(String keywordPrefix) {
        String depunctuated = keywordPrefix.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();                            // convert to uppercase
        Comparator<DocumentImpl> documentComparatorPrefixAppearances =
                (DocumentImpl doc1, DocumentImpl doc2) -> prefixCount(doc2, uppercased) - prefixCount(doc1,uppercased);
        List<DocumentImpl> sortedDocs = trie.getAllWithPrefixSorted(keywordPrefix, documentComparatorPrefixAppearances);
        //remove duplicates, go thru each doc and count prefix, then sort

        return sortedDocs;
    }

    private int prefixCount(DocumentImpl doc, String prefix){
        //the underlying logic: split the doc anytime the prefix shows up with a space before it
        // (means its the start of a word, add spaces to start of the doc) and ull end up with one more piece
        // than times the prefix showed up at the start of a word
        String treatedString = depunctuateAndUppercase(doc.getDocumentAsTxt());
        String[] docTextWithWithSpaceAtStart = (" " + treatedString).split(" " + prefix);
        return docTextWithWithSpaceAtStart.length - 1;
    }

    private  String depunctuateAndUppercase(String word) {
        String depunctuated = word.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();          // convert to uppercase
        return uppercased;
    }


    /**
     * Completely remove any trace of any document which contains the given keyword
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAll(String keyword){
        String treatedKeyword = depunctuateAndUppercase(keyword);
        List<DocumentImpl> docsToDelete = searchDocs(treatedKeyword);
        Set<URI> deletedDocsUris = new HashSet<>();
        for (DocumentImpl doc : docsToDelete) {
            deletedDocsUris.add(doc.getKey());
        }
        CommandSet<URI> commandSet = new CommandSet<URI>();

        for (DocumentImpl doc : docsToDelete) {
            Function<URI, Boolean> undoCommand = undoDeleteCommand(doc.getKey(), new ByteArrayInputStream(doc.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            GenericCommand<URI> stackCommand = new GenericCommand<URI>(doc.getKey(), undoCommand);
            commandSet.addCommand(stackCommand);
        }
        for (URI uri : deletedDocsUris) {
            obliterateSpecificDocFromTrie(getDocumentAsDocument(uri)); // tried doing it in one foreach but it caused an error
            hashTableImpl.put(uri, null);
        }

        commandStack.push(commandSet);
        return deletedDocsUris;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix){
       Set<DocumentImpl> deleteDocs = trie.deleteAllWithPrefix(keywordPrefix);
        Set<URI> deletedDocsUris = new HashSet<>();
        CommandSet<URI> commandSet = new CommandSet<URI>();
        for (DocumentImpl doc : deleteDocs) {
            deletedDocsUris.add(doc.getKey());
            obliterateSpecificDocFromTrie(doc);
            deleteDocument(doc.getKey());//this method calls obliterateFromTrie
            Function<URI, Boolean> undoCommand = undoDeleteCommand(doc.getKey(), new ByteArrayInputStream(doc.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            GenericCommand<URI> stackCommand = new GenericCommand<URI>(doc.getKey(), undoCommand);
            commandSet.addCommand(stackCommand);
            DocumentImpl docPostDelete = hashTableImpl.put(doc.getKey(), null);
        }
        commandStack.push(commandSet);
        return deletedDocsUris;
    }

    private DocumentImpl createDoc(URI uri, String txt, int txtHash){
        DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode());
        addWordMapToTrie(newDocument);


        return newDocument;


    }


    private void addWordMapToTrie(DocumentImpl doc) {
        for (String key : doc.wordMap.keySet()) {
            this.trie.put(key, doc);
        }

    }
}