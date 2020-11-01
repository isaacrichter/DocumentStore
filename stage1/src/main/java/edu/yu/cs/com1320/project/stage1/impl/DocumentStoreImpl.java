package edu.yu.cs.com1320.project.stage1.impl;

import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;
import java.io.ByteArrayOutputStream;


public class DocumentStoreImpl implements DocumentStore{

    private HashTableImpl<URI,DocumentImpl> hashTableImpl;

    public DocumentStoreImpl()
    {
        hashTableImpl = new HashTableImpl<URI, DocumentImpl>();
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return the hashcode of the String version of the document
     */
    public int putDocument(InputStream input, URI uri, DocumentStore.DocumentFormat format) {
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
            public byte[] getDocumentAsPdf (URI uri){
                DocumentImpl document = (DocumentImpl) hashTableImpl.get(uri);
                if(document == null){return null;}
                else{return document.getDocumentAsPdf();}
            }

            /**
             * @param uri the unique identifier of the document to get
             * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
             */
            public String getDocumentAsTxt (URI uri){
                DocumentImpl document = (DocumentImpl) hashTableImpl.get(uri);
                if(document == null){return null;}
                else{return document.getDocumentAsTxt();}
            }

            /**
             * @param uri the unique identifier of the document to delete
             * @return true if the document is deleted, false if no document exists with that URI
             */
            public boolean deleteDocument (URI uri){
                DocumentImpl docPreDelete = hashTableImpl.get(uri);
                DocumentImpl docPostDelete = hashTableImpl.put(uri, null);
                if (docPostDelete == null){return false;}
                else if(docPostDelete.equals(docPreDelete)){return true;}
                else {return false;}
            }



    }