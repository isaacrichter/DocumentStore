package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
///

import edu.yu.cs.com1320.project.impl.HashTableImpl;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore{

    private HashTableImpl<URI,DocumentImpl> hashTableImpl;
    private StackImpl<Command> commandStack;


    public DocumentStoreImpl() {
        hashTableImpl = new HashTableImpl<URI, DocumentImpl>();
        commandStack = new StackImpl<Command>();
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the String version of the previous doc. If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     MAKE STACK OBJECT AND ADD TO THE STACK
     */
    public int putDocument(InputStream input, URI uri, DocumentStore.DocumentFormat format) {
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        } else if (input == null) {
            return putDelete(uri);
        } else {
            byte[] byteArray = inputStreamToByteArray(input); // converts input stream to byteArray equivalent
            if (format == DocumentFormat.TXT) {
                return addTxtDoc(byteArray, uri);
            } else {
                return addPdfDoc(byteArray, uri);
            }
        }
    }

    private String PdfByteArrayToString(byte[] byteArray) {
        try {
            PDDocument document = PDDocument.load(byteArray);  // load the byte array onto a pddocument
            PDFTextStripper stripper = new PDFTextStripper();  //instantiate a textstripper,
            return stripper.getText(document);                // and strip the document into a string, and return
        } catch (IOException e) {e.printStackTrace(); }
        return  null;
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


    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that URI
     */
    public byte[] getDocumentAsPdf(URI uri){
        DocumentImpl document = (DocumentImpl) hashTableImpl.get(uri);
        if(document == null){return null;}
        else{
            return document.getDocumentAsPdf();
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
     */
    public String getDocumentAsTxt(URI uri){
        DocumentImpl document = (DocumentImpl) hashTableImpl.get(uri);
        if(document == null){return null;}
        else{return document.getDocumentAsTxt();}
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
    MAKE STACK OBJECT AND ADD TO THE STACK
     */
    public boolean deleteDocument(URI uri){
        DocumentImpl docPreDelete = hashTableImpl.get(uri);
        DocumentImpl docPostDelete = hashTableImpl.put(uri, null);
        if (docPostDelete == null){return false;}
        else if(docPostDelete.equals(docPreDelete)){
            Function<URI, Boolean> undoCommand= undoDeleteCommand(uri, new ByteArrayInputStream(docPreDelete.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return true;}
        else {return false;}
    }

    private boolean deleteDocumentAddNothingToStack(URI uri){
        DocumentImpl docPreDelete = hashTableImpl.get(uri);
        DocumentImpl docPostDelete = hashTableImpl.put(uri, null);
        if (docPostDelete == null){return false;}
        else if(docPostDelete.equals(docPreDelete)){
            return true;}
        else {return false;}
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


    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException{
        if(commandStack.size() == 0){throw new IllegalStateException();}
        else {
            Command command = commandStack.pop();
            command.undo();
        }
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    public void undo(URI uri) throws IllegalStateException{
        StackImpl<Command> helperStack = new StackImpl<Command>();
        while (!commandStack.peek().getUri().equals(uri)) {
            if (commandStack.peek() == null){throw new IllegalStateException();}
            helperStack.push(commandStack.pop());
        }
        commandStack.pop().undo();

        while (helperStack.size() != 0){
            commandStack.push(helperStack.pop());
        }

        }

    /**  * @return the Document object stored at that URI, or null if there is no such Document  */
    protected Document getDocument(URI uri){
        DocumentImpl document = hashTableImpl.get(uri);
        if(document == null){return null;}
        else{
            return document;
        }
    }

    private Function<URI,Boolean> undoPutCommand(URI uri){
        Function<URI, Boolean> undoPut = (URI uri1) -> {
            return deleteDocumentAddNothingToStack(uri); // maybe its supposed to be uri1??
        };
        return  undoPut;
    }

    private Function<URI,Boolean> undoDeleteCommand(URI uri, InputStream inputStream, DocumentStore.DocumentFormat format){

        Function<URI, Boolean> undoDelete = (URI uri1) -> {
                    int hash = putDocumentAddNothingToStack(inputStream, uri, format);
                    if (hash == 0){return false;}
                    else {return true;}
        };
        return  undoDelete;
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
                DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode());
                ///
                DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
                if(oldDocument == null){return 0;}
                else{ return oldDocument.getDocumentTextHashCode(); }
            } else {
                String txt = PdfByteArrayToString(byteArray);
                txt = txt.trim();
                DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode(),byteArray);
                DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
                if(oldDocument == null){return 0;}
                else{ return oldDocument.getDocumentTextHashCode(); }
            }
        }
    }


    private int addTxtDoc(byte[] byteArray, URI uri){
        String txt = new String(byteArray);  //converts it to a string, next line creates new DocumentImpl to store
        DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode());
        DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
        if(oldDocument == null){
            Function<URI, Boolean> undoCommand = undoPutCommand(uri);
            createAddCommandToStack(uri, undoCommand);
            return 0;
        }
        else{
            Function<URI, Boolean> undoCommand = undoReplaceCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return oldDocument.getDocumentTextHashCode();
        }
    }



    private int addPdfDoc(byte[] byteArray, URI uri){
        String txt = PdfByteArrayToString(byteArray);
        txt = txt.trim();
        DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode(), byteArray);
        DocumentImpl oldDocument = hashTableImpl.put(uri, newDocument);    // add it to the hashtable
        if (oldDocument == null) {
            Function<URI, Boolean> undoCommand = undoPutCommand(uri);
            createAddCommandToStack(uri, undoCommand);
            return 0;
        } else {
            Function<URI, Boolean> undoCommand = undoReplaceCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return oldDocument.getDocumentTextHashCode();
        }
    }

    private void createAddCommandToStack(URI uri, Function<URI, Boolean> undoCommand){
        Command command = new Command(uri, undoCommand);
        commandStack.push(command);
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

}

/*
LOGIC FOR LAMBDAS
A) what needs to be saved for each undo
    possible actions to undo
    1) delete existing document -- put it
    2) put new document -- delete it
    3) replace existing document -- delete nww, and put old
    (doesn't make sense to undo getting a doc or a failed put or delete action)

    info and action to save in th lambda
    1) deleted document -- put it
    2) hash of new document -- delete it
    3) hash of new and old document, delete and then put
*/