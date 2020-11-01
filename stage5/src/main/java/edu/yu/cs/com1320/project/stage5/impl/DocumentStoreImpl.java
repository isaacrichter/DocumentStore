package edu.yu.cs.com1320.project.stage5.impl;


import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;


import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.function.Function;




public class DocumentStoreImpl implements DocumentStore {

    protected BTreeImpl<URI,Document> bTreeImpl;
    private TrieImpl<DocumentImpl> trie;
    private StackImpl<Undoable> commandStack;
    //private CommandSet<URI> cmd;
    protected MinHeapImplImpl minHeap;
    private int maxDocs;
    private int maxBytes;
    private  int totalBytes;
    private  int totalDocs;
    File theBaseDir;

    public DocumentStoreImpl() {
        trie = new TrieImpl<DocumentImpl>();

        bTreeImpl = new BTreeImpl<URI, Document>();
        PersistenceManager<URI,Document> pm = new DocumentPersistenceManager();
        bTreeImpl.setPersistenceManager(pm);
        theBaseDir = new File(System.getProperty("user.dir"));//hopefully the base dir doesnt change, as per google
        try {
            bTreeImpl.put(new URI(""), null);
        }catch (Exception ignored){}
        commandStack = new StackImpl<Undoable>();
        minHeap = new MinHeapImplImpl();
        maxDocs = Integer.MAX_VALUE;
        maxBytes = Integer.MAX_VALUE;
        totalBytes = 0;
        totalDocs = 0;
    }

    public DocumentStoreImpl(File baseDir) {
        trie = new TrieImpl<DocumentImpl>();

        bTreeImpl = new BTreeImpl<URI, Document>();
        PersistenceManager<URI,Document> pm = new DocumentPersistenceManager(baseDir);
        theBaseDir = baseDir;
        bTreeImpl.setPersistenceManager(pm);
        try {
            bTreeImpl.put(new URI(""), null);
        }catch (Exception ignored){}
        commandStack = new StackImpl<Undoable>();
        minHeap = new MinHeapImplImpl();
        maxDocs = Integer.MAX_VALUE;
        maxBytes = Integer.MAX_VALUE;
        totalBytes = 0;
        totalDocs = 0;
    }

    class MinHeapImplImpl extends MinHeapImpl<URI> {
        protected  URI[] elements;

        protected int count;
        protected Map<URI,Integer> elementsToArrayIndex; //used to store the index in the elements array

        public MinHeapImplImpl(){
            elements =  new URI[20];
            count = 0;
            elementsToArrayIndex = new HashMap<URI,Integer>(8) ;
        }



        public void reHeapify(URI uri){

            upHeap(getArrayIndex(uri));
            downHeap(getArrayIndex(uri));
            // If the element is in the correct spot in the heap, then do nothing
            // If it was recently changed, move it down until its in the correct spot
            // If a doc was deleted and needs to be moved to the top of the heap, that means its time was changed to be
            // really old and it needs to be upheaped to the top so it can be popped off
        }

        protected  int getArrayIndex(URI uri){
            if(elementsToArrayIndex.get(uri) == null){
                return  0;
            }
            else {
                return elementsToArrayIndex.get(uri);
            }
        }

        protected void doubleArraySize() {
            URI[] tempArray= new URI[2 * (this.elements.length)];
            for (int i = 0 ; i < this.elements.length ; i++){
                tempArray[i] = this.elements[i];
            }
            this.elements = tempArray;
        }

        /**
         * swap the values stored at elements[i] and elements[j]
         */
        //@Override
        protected  void swap(int i, int j)
        {
            URI temp = this.elements[i];
            this.elements[i] = this.elements[j];
            this.elements[j] = temp;

            elementsToArrayIndex.put(this.elements[i],i);
            elementsToArrayIndex.put(this.elements[j],j);

        }

        /**
         *while the key at index k is less than its
         *parent's key, swap its contents with its parent’s
         */
        //@Override
        protected  void upHeap(int k)
        {
            while (k > 1 && this.isGreater(k / 2, k))
            {
                this.swap(k, k / 2);
                k = k / 2;
            }
        }
        private BTreeImpl getBtree(){
            return bTreeImpl;
        }

        protected DocumentImpl minHeapGetDoc(URI uri){
            bTreeImpl.setPersistenceManager(null);


            DocumentImpl document = (DocumentImpl)bTreeImpl.get(uri);

            DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(theBaseDir);
            bTreeImpl.setPersistenceManager(persistenceManager);
            if (document == null) {
                return null;
            } else {
                return document;
            }
        }




        //@Override
        protected boolean isGreater(int i, int j)
        //if first is bigger, returns true
        {
            //It should not be possible for a json to be called by getDocumentAsDocument, which is fine
            //because this should only comapare docs in minheap, namely not jsons

            //how to get the document without issue
            BTreeImpl btree = getBtree();

            return ((minHeapGetDoc(this.elements[i])).getLastUseTime()) > ((minHeapGetDoc(this.elements[j]))
                    .getLastUseTime());
        }

        /**
         * move an element down the heap until it is less than
         * both its children or is at the bottom of the heap
         */
        //@Override
        protected  void downHeap(int k)
        {
            if (elements[2]==null){return;}
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

        //@Override
        public void insert(URI x) {



            if (elementsToArrayIndex.keySet().contains(x)) {
                return;
            }
            // double size of array if necessary
            if (this.count >= this.elements.length - 1) {
                this.doubleArraySize();
            }
            //add x to the bottom of the heap
            this.elements[++this.count] = x;
            elementsToArrayIndex.put(x, this.count);


            //percolate it up to maintain heap order property

            this.upHeap(this.count);
        }

        //@Override
        public URI removeMin()
        {
            if (elements[1]==null)
            {
                throw new NoSuchElementException("Heap is empty");
            }
            URI min = this.elements[1];
            //swap root with last, decrement count
            this.swap(1, this.count--);
            elementsToArrayIndex.remove(min);
            //move new root down as needed
            this.downHeap(1);
            this.elements[this.count + 1] = null; //null it to prepare for GC
            return min;
        }
    }





    //                 ****************** ESSENTIAL PUT METHODS START HERE ***********************                   //


    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the String version of the previous doc.
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     */
    public int putDocument(InputStream input, URI uri, DocumentStore.DocumentFormat format) {
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
        obliterateSpecificDocFromMinHeap((getDocumentAsDocument(uri)));

        Document oldDocument = bTreeImpl.put(uri, null);
        if (oldDocument == null) {
            Function<URI, Boolean> undoCommand = undoDeleteCommand(uri,null, DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return 0;
        } else {
            Function<URI, Boolean> undoCommand = undoDeleteCommand(uri, new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);

            obliterateSpecificDocFromTrie((DocumentImpl) oldDocument);

            subtractDocFromLimit((DocumentImpl) oldDocument); // only subtract if was json ;l;

            return oldDocument.getDocumentTextHashCode();
        }
    }


    private byte[] inputStreamToByteArray(InputStream input) {
        if (input == null) {
            return null;
        }
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private int addTxtDoc(byte[] byteArray, URI uri) {
        String txt = new String(byteArray);
        return addDocGen(txt,uri);
    }

    private int addPdfDoc(byte[] byteArray, URI uri) {
        String txt1 = PdfByteArrayToString(byteArray);
        String txt = txt1.trim();
        return addDocGen(txt,uri);
    }


    private int addDocGen(String txt, URI uri) {

        boolean sameDoc = false;

        boolean wasJSon = false;
        if (getDocumentAsDocument(uri) != getDocumentAsDocumentJsonNoReheap(uri)) {
            wasJSon = true;
        }
        DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());


        if (txt.equals(getDocumentAsTxtNoReheap(uri))) {
            obliterateSpecificDocFromMinHeap(getDocumentAsDocument(uri)); //the get doc as doc will return null
            //if the doc is stored in not json, which
            //mean only on heap things will be returned:)
            obliterateSpecificDocFromTrie(getDocumentAsDocument(uri));
            sameDoc = true;
            if (!wasJSon) {//if it was a json, the limits now go up because the new doc is being placed in memory as an obj
                totalBytes -= getDocsTotalBytes(newDocument);   //subtracting so adding 5 lines later wont throw anything off
                totalDocs--;                                    //subtracting so adding 5 lines later wont throw anything off
            }
        }

        Document oldDocument = bTreeImpl.get(uri);    // add it to the btree
        if (oldDocument != null) {
        }
        bTreeImpl.put(uri, newDocument);
        minHeap.insert(newDocument.getKey());
        totalBytes += getDocsTotalBytes(newDocument);
        totalDocs++;

        if (oldDocument == null) {
            minHeap.insert(newDocument.getKey());
            return onlyAddDoc(newDocument);//;l;l;l;l
        } else {
            if(!wasJSon) {
                totalBytes -= getDocsTotalBytes((DocumentImpl) oldDocument); // was commented out with this...: Still stored in stack!
                //if it wasnt a json then it doesn't count against memory
            }

            replaceDoc(newDocument, (DocumentImpl) oldDocument,sameDoc);
            return oldDocument.getDocumentTextHashCode();
        }
    }


    private int replaceDoc(DocumentImpl newDocument, DocumentImpl oldDocument, Boolean sameDoc){
        int bytesDown = 0;
        int docsDown = 0;
        List<DocumentImpl> docsToRemove = new ArrayList<>();
        while ( !(maxBytes >= (totalBytes - bytesDown - getDocsTotalBytes(oldDocument))) || !(maxDocs >= totalDocs - docsDown)) {

            DocumentImpl doc = getDocumentAsDocument(minHeap.removeMin());
            docsToRemove.add(doc);
            bytesDown += getDocsTotalBytes(doc) ;
            docsDown++;

        }

        if (!sameDoc) {
            if(!docsToRemove.isEmpty()){
                CommandSet<URI> commandSet = new CommandSet<URI>();
                for (DocumentImpl doc : docsToRemove) {
                    deleteDocumentAddNothingToStack(doc.getKey());
                    obliterateSpecificDocFromTrie(doc);
                    removeFromStack(doc.getKey());
                    Document docPostDelete = bTreeImpl.put(doc.getKey(), null);
                }
                Function<URI, Boolean> undoCommand = undoDeleteCommand(oldDocument.getKey(), new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
                GenericCommand<URI> stackCommand = new GenericCommand<URI>(oldDocument.getKey(), undoCommand);
                commandSet.addCommand(stackCommand);

                Function<URI, Boolean> undotheCommand = undoPutCommand(newDocument.getKey());
                GenericCommand<URI> undoThePutCommand = new GenericCommand<URI>(newDocument.getKey(), undotheCommand);
                commandSet.addCommand(undoThePutCommand);
                commandStack.push(commandSet);

            }else {
                obliterateSpecificDocFromTrie(oldDocument);
                obliterateSpecificDocFromMinHeap(oldDocument);
                Function<URI, Boolean> undoCommand = undoReplaceCommand(oldDocument.getKey(), new ByteArrayInputStream(oldDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
                createAddCommandToStack(oldDocument.getKey(), undoCommand);
            }
        }
        Function<URI, Boolean> undoCommand = undoReplaceCommand(newDocument.getKey(),new ByteArrayInputStream(newDocument.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
        createAddCommandToStack(newDocument.getKey(), undoCommand);
        return oldDocument.hashCode();
    }

    private int onlyAddDoc(DocumentImpl freshDoc) {
        int bytesDown = 0;
        int docsDown = 0;
        List<DocumentImpl> docsToRemove = new ArrayList<>();

        while (!(maxBytes >= (this.totalBytes - bytesDown))  || !(maxDocs >= totalDocs - docsDown)) {
            DocumentImpl doc = getDocumentAsDocument(minHeap.removeMin()); // removes min from heap,
            // and puts in docs to remove
            docsToRemove.add(doc);
            bytesDown += getDocsTotalBytes(doc);
            docsDown++;
        }

        if (!docsToRemove.isEmpty()) {
            for (DocumentImpl doc : docsToRemove) {
                totalBytes -= getDocsTotalBytes(doc);
                totalDocs--;
                try{
                    bTreeImpl.moveToDisk(doc.getKey());
                }catch (Exception ignored){};
            }
            Function<URI, Boolean> undoCommand = undoPutCommand(freshDoc.getKey());
            GenericCommand<URI> undoThePutCommand = new GenericCommand<URI>(freshDoc.getKey(), undoCommand);
            commandStack.push(undoThePutCommand);
            return 0;

        }else{

            Function<URI, Boolean> undoCommand = undoPutCommand(freshDoc.getKey());
            createAddCommandToStack(freshDoc.getKey(), undoCommand);
            return 0;
        }
    }


    private String PdfByteArrayToString(byte[] byteArray) {
        try {
            PDDocument document = PDDocument.load(byteArray);  // load the byte array onto a pddocument
            PDFTextStripper stripper = new PDFTextStripper();  //instantiate a textstripper,
            return stripper.getText(document);                // and strip the document into a string, and return
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


//                  ****************** ESSENTIAL PUT METHODS END HERE ***********************                    //

//                  ****************** ESSENTIAL GET METHODS START HERE ***********************                  //

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as a PDF, or null if no document exists with that URI
     */
    public byte[] getDocumentAsPdf(URI uri) {
        boolean wasJSon = false;
        if (getDocumentAsDocument(uri) != getDocumentAsDocumentJsonNoReheap(uri)){
            wasJSon = true;
        }
        //+++++++++++++++++++++++++
        Document document = bTreeImpl.get(uri);
        if (document == null) {
            return null;
        } else {
            if(wasJSon){
                this.totalBytes+=getDocsTotalBytes((DocumentImpl) document);
                manageMemory();
                //the memory counts go up, and if memory is exceeded, things get moved to disk//ooooooooooo
            }
            document.setLastUseTime(System.nanoTime());
            minHeap.reHeapify(uri);
            return document.getDocumentAsPdf();
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document as TXT, i.e. a String, or null if no document exists with that URI
     */
    public String getDocumentAsTxt(URI uri) {
        boolean wasJSon = false;
        if (getDocumentAsDocument(uri) != getDocumentAsDocumentJsonNoReheap(uri)){
            wasJSon = true;
        }

        Document document = bTreeImpl.get(uri);

        if (document == null) {
            return null;
        } else {
            if(wasJSon){
                this.totalBytes+=getDocsTotalBytes((DocumentImpl) document);
                manageMemory();
                //the memory counts go up, and if memory is exceeded, things get moved to disk//oooooo
            }
            document.setLastUseTime(System.nanoTime());
            minHeap.reHeapify(uri);
            return document.getDocumentAsTxt();
        }
    }


//                  ****************** ESSENTIAL GET METHODS END HERE ***********************                    //

//                  *********** ESSENTIAL NON-SEARCH DELETE METHODS START HERE ***************                  //


    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    public boolean deleteDocument(URI uri) {
        obliterateSpecificDocFromMinHeap(getDocumentAsDocument(uri));

        Document docPreDelete = bTreeImpl.get(uri);
        Document docPostDelete = bTreeImpl.put(uri, null);
//-------------------------------------------------------------------------------------------------------------------------
        if (docPostDelete == null) {
            Function<URI, Boolean> undoCommand = undoDeleteCommand(uri,null, DocumentFormat.TXT);
            createAddCommandToStack(uri, undoCommand);
            return false;
        } else if (docPostDelete.equals(docPreDelete)) {
            obliterateSpecificDocFromTrie((DocumentImpl) docPostDelete);
            subtractDocFromLimit((DocumentImpl)docPostDelete);

            Function<URI, Boolean> undoCommand = undoDeleteCommand(docPreDelete.getKey(), new ByteArrayInputStream(docPreDelete.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            GenericCommand<URI> stackCommand = new GenericCommand<URI>(docPreDelete.getKey(), undoCommand);
            commandStack.push(stackCommand);
            return true;
        } else {
            return false;
        }
    }


//                  ****************** ESSENTIAL NON-SEARCH DELETE METHODS END HERE ***********************                    //

//                  ******************** ESSENTIAL UNDO METHODS START HERE *************************


    /**
     * undo the last put or delete command
     *
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    public void undo() throws IllegalStateException {
        if (commandStack.size() == 0) {
            throw new IllegalStateException();
        } else {
            Undoable commandOrCommandSet = commandStack.pop();
            commandOrCommandSet.undo();
        }
    }


    private void removeFromStack(URI uri){

        StackImpl<Undoable> helperStack = new StackImpl<Undoable>();
        Undoable stackObj = commandStack.peek();
        while (commandStack.peek() != null) {
            if (stackObj instanceof CommandSet) {
                Iterator<GenericCommand<URI>> iterator = ((CommandSet) stackObj).iterator();

                while (iterator.hasNext()) {
                    GenericCommand<URI> genericCommand = iterator.next();
                    if (genericCommand.getTarget() == uri) {
                        ((CommandSet) stackObj).undo(uri);
                    }
                }
                if (((CommandSet<URI>) stackObj).size() == 0) {
                    commandStack.pop();
                }

            } else if (stackObj instanceof GenericCommand) {
                if (((GenericCommand) stackObj).getTarget() == uri) {
                    commandStack.pop().undo();
                }
            } else {
            }
            helperStack.push(commandStack.pop());
        }
        deleteDocumentAddNothingToStack(uri);// make sure the last undo didnt result in a put
        //return stack to original state sans the undone command
        while (helperStack.size() != 0) {
            commandStack.push(helperStack.pop());

        }
    }


    /**
     * undo the last put or delete that was done with the given URI as its key
     *
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
                Iterator<GenericCommand<URI>> iterator = ((CommandSet) stackObj).iterator();
                CommandSet<URI> cmd = new CommandSet<>() ;
                while (iterator.hasNext()) {
                    GenericCommand<URI> genericCommand = iterator.next();
                    if (genericCommand.getTarget() == uri) {
                        cmd = (CommandSet<URI>) stackObj;
                        bool = true;
                    }
                }
                undoIt(uri, cmd);
                if (((CommandSet<URI>) stackObj).size() == 0) {
                    commandStack.pop();
                }

            } else if (stackObj instanceof GenericCommand) {
                if (((GenericCommand) stackObj).getTarget() == uri) {
                    commandStack.pop().undo();
                    bool = true;
                }
            } else {
            }
            helperStack.push(commandStack.pop());
            // bool remains false, go thru next item in the stack

        }
        //return stack to original state sans the undone command
        while (helperStack.size() != 0) {
            commandStack.push(helperStack.pop());

        }
    }


    private Function<URI, Boolean> undoDeleteCommand(URI uri, InputStream inputStream, DocumentStore.DocumentFormat format) {

        Function<URI, Boolean> undoDelete = (URI uri1) -> {
            int hash = putDocumentAddNothingToStack(inputStream, uri, format);
            if (hash == 0) {
                return false;
            } else {
                return true;
            }
        };
        return undoDelete;
    }

    private void createAddCommandToStack(URI uri, Function<URI, Boolean> undoCommand) {
        GenericCommand genericCommand = new GenericCommand(uri, undoCommand);
        commandStack.push(genericCommand);
    }

    private int putDocumentAddNothingToStack(InputStream input, URI uri, DocumentStore.DocumentFormat format) {
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        } else if (input == null) {
            return 0;
        } else {
            byte[] byteArray = inputStreamToByteArray(input); // converts input stream to byteArray equivalent

            if (format == DocumentFormat.TXT) {
                String txt = new String(byteArray);  //converts it to a string, next line creates new DocumentImpl to store
                DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode());
                Document oldDocument = bTreeImpl.put(uri, newDocument);    // add it to the hashtable
                minHeap.insert(newDocument.getKey());
                if (oldDocument == null) {
                    return 0;
                } else {
                    return oldDocument.getDocumentTextHashCode();
                }
            } else {
                String txt = PdfByteArrayToString(byteArray);
                txt = txt.trim();
                DocumentImpl newDocument = createDoc(uri, txt, txt.hashCode(), byteArray);
                DocumentImpl oldDocument = (DocumentImpl)bTreeImpl.put(uri, newDocument);    // add it to the hashtable
                minHeap.insert(newDocument.getKey());

                if (oldDocument == null) {
                    return 0;
                } else {
                    return oldDocument.getDocumentTextHashCode();
                }
            }
        }
    }


    private Function<URI, Boolean> undoReplaceCommand(URI uri, InputStream inputStream, DocumentStore.DocumentFormat format) {

        Function<URI, Boolean> undoReplace = (URI uri1) -> {
            boolean bool1 = deleteDocumentAddNothingToStack(uri);
            int hash = putDocumentAddNothingToStack(inputStream, uri, format); // maybe its supposed to be uri1??
            if (hash != 0 && (bool1)) {
                return true;
            } else {
                return false;
            }
        };
        return undoReplace;
    }


    private Function<URI, Boolean> undoPutCommand(URI uri) {
        Function<URI, Boolean> undoPut = (URI uri1) -> {
            return deleteDocumentAddNothingToStack(uri); // maybe its supposed to be uri1??
        };
        return undoPut;
    }

    private boolean deleteDocumentAddNothingToStack(URI uri) {
        obliterateSpecificDocFromMinHeap(getDocumentAsDocument(uri));

        DocumentImpl docPreDelete = (DocumentImpl)bTreeImpl.get(uri);
        DocumentImpl docPostDelete = (DocumentImpl)bTreeImpl.put(uri, null);
        if (docPostDelete == null) {
            return false;
        } else if (docPostDelete.equals(docPreDelete)) {
            obliterateSpecificDocFromTrie(docPostDelete);
            if(commandStackContains(docPostDelete)){
                subtractDocFromLimit(docPostDelete);}
            return true;
        } else {
            return false;
        }
    }

    private boolean commandStackContains(DocumentImpl doc){
        StackImpl<Undoable> helperStack = new StackImpl<Undoable>();
        Undoable stackObj = commandStack.peek();
        boolean contains = false;
        while (commandStack.peek() != null) {
            if (stackObj instanceof CommandSet) {
                Iterator<GenericCommand<URI>> iterator = ((CommandSet) stackObj).iterator();

                while (iterator.hasNext()) {
                    GenericCommand<URI> genericCommand = iterator.next();
                    if (genericCommand.getTarget() == doc.getKey()) {
                        contains = true;
                    }
                }
            } else if (stackObj instanceof GenericCommand) {
                if (((GenericCommand) stackObj).getTarget() == doc.getKey()) {
                    contains = true;
                }
            } else {
            }
            helperStack.push(commandStack.pop());
            // bool remains false, go thru next item in the stack
        }

        //return stack to original state
        while (helperStack.size() != 0) {
            commandStack.push(helperStack.pop());
        }
        return contains;
    }


    private void undoIt(URI uri, CommandSet<URI> cmd) {
        cmd.undo(uri);
    }


//                  ****************** ESSENTIAL UNDO METHODS END HERE ***********************                    //

//                  **************** ESSENTIAL SEARCH METHODS START HERE *********************


    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE INSENSITIVE.
     *
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> search(String keyword) {
        List<DocumentImpl> sortedDocs = searchDocs(keyword);
        updateTimeOnDocListAndReHeapify(sortedDocs);
        return docListToText(sortedDocs);
    }

    /**
     * same logic as search, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFs(String keyword) {
        List<DocumentImpl> sortedDocs = searchDocs(keyword);
        updateTimeOnDocListAndReHeapify(sortedDocs);
        return docListToPdf(sortedDocs);
    }

    private List<DocumentImpl> searchDocs(String keyword) {
        Comparator<DocumentImpl> documentComparator =
                (DocumentImpl doc1, DocumentImpl doc2) ->
                        doc2.wordCount(keyword) - doc1.wordCount(keyword);
        List<DocumentImpl> sortedDocs = trie.getAllSorted(keyword, documentComparator);
        return sortedDocs;
    }


    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE INSENSITIVE.
     *
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    public List<String> searchByPrefix(String keywordPrefix) {
        List<DocumentImpl> sortedDocs = searchDocsByPrefix(keywordPrefix);
        updateTimeOnDocListAndReHeapify(sortedDocs);
        return docListToText(sortedDocs);
    }

    /**
     * same logic as searchByPrefix, but returns the docs as PDFs instead of as Strings
     */
    public List<byte[]> searchPDFsByPrefix(String keywordPrefix) {
        List<DocumentImpl> sortedDocs = searchDocsByPrefix(keywordPrefix);
        updateTimeOnDocListAndReHeapify(sortedDocs);
        return docListToPdf(sortedDocs);
    }

    private List<DocumentImpl> searchDocsByPrefix(String keywordPrefix) {



        String depunctuated = keywordPrefix.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();                            // convert to uppercase
        Comparator<DocumentImpl> documentComparatorPrefixAppearances =
                (DocumentImpl doc1, DocumentImpl doc2) -> prefixCount(doc2, uppercased) - prefixCount(doc1, uppercased);
        List<DocumentImpl> sortedDocs = trie.getAllWithPrefixSorted(keywordPrefix, documentComparatorPrefixAppearances);
        //remove duplicates, go thru each doc and count prefix, then sort

        return sortedDocs;
    }

    private int prefixCount(DocumentImpl doc, String prefix) {
        //the underlying logic: split the doc anytime the prefix shows up with a space before it
        // (means its the start of a word, add spaces to start of the doc) and ull end up with one more piece
        // than times the prefix showed up at the start of a word
        String treatedString = depunctuateAndUppercase(doc.getDocumentAsTxt());
        String[] docTextWithWithSpaceAtStart = (" " + treatedString).split(" " + prefix);
        return docTextWithWithSpaceAtStart.length - 1;
    }


    private String depunctuateAndUppercase(String word) {
        String depunctuated = word.replaceAll("\\p{Punct}", ""); // remove punctuation
        String uppercased = depunctuated.toUpperCase();          // convert to uppercase
        return uppercased;
    }


    private List<byte[]> docListToPdf(List<DocumentImpl> sortedDocs) {
        List<byte[]> sortedPdfDocs = new ArrayList<>();
        for (DocumentImpl doc : sortedDocs) {
            sortedPdfDocs.add(doc.getDocumentAsPdf());
        }
        return sortedPdfDocs;
    }


    private List<String> docListToText(List<DocumentImpl> sortedDocs) {
        List<String> sortedTextDocs = new ArrayList<>();
        for (DocumentImpl doc : sortedDocs) {
            sortedTextDocs.add(doc.getDocumentAsTxt());
        }
        return sortedTextDocs;
    }


//                  ******************** ESSENTIAL SEARCH METHODS END HERE *************************                    //

//                  **************** ESSENTIAL SEARCH-DELETE METHODS START HERE *********************


    /**
     * Completely remove any trace of any document which contains the given keyword
     *
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */


    /*

     */
    public Set<URI> deleteAll(String keyword) {
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
            obliterateSpecificDocFromTrie(getDocumentAsDocumentReheap(uri)); // tried doing it in one foreach but it caused an error
            //JSON figure out how this will work...
            obliterateSpecificDocFromMinHeap(getDocumentAsDocumentReheap(uri));
            subtractDocFromLimit(getDocumentAsDocumentJsonNoReheap(uri)); // Still exists in the commans stack!

            bTreeImpl.put(uri, null);
        }

        commandStack.push(commandSet);
        return deletedDocsUris;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE INSENSITIVE.
     *
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        Set<DocumentImpl> deleteDocs = trie.deleteAllWithPrefix(keywordPrefix);
        if(deleteDocs==null){
            return null;
        }
        Set<URI> deletedDocsUris = new HashSet<>();
        CommandSet<URI> commandSet = new CommandSet<URI>();
        for (DocumentImpl doc : deleteDocs) {
            deletedDocsUris.add(doc.getKey());

            deleteDocument(doc.getKey());//this method calls obliterateFromTrie and heap AND SUBTRACTS FROM STORAGE LIMIT
            Function<URI, Boolean> undoCommand = undoDeleteCommand(doc.getKey(), new ByteArrayInputStream(doc.getDocumentAsTxt().getBytes()), DocumentFormat.TXT);
            GenericCommand<URI> stackCommand = new GenericCommand<URI>(doc.getKey(), undoCommand);
            commandSet.addCommand(stackCommand);
            DocumentImpl docPostDelete = (DocumentImpl)bTreeImpl.put(doc.getKey(), null);
        }
        commandStack.push(commandSet);
        return deletedDocsUris;
    }
    protected DocumentImpl getDocumentAsDocument(URI uri) {
        //we get rid of the pm before getting so that only docs not on disk are returned
        bTreeImpl.setPersistenceManager(null);
        DocumentImpl document = (DocumentImpl)bTreeImpl.get(uri);

        DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(this.theBaseDir);
        bTreeImpl.setPersistenceManager(persistenceManager);
        if (document == null) {
            return null;
        } else {
            return document;
        }
    }

//                  ******************** ESSENTIAL SEARCH-DELETE METHODS END HERE *************************                    //

//                  ******************* ESSENTIAL TRIE AND HEAP  METHODS START HERE ***********************


    private void obliterateSpecificDocFromTrie(DocumentImpl doc) {
        for (String word : doc.getWordMap().keySet()) {
            trie.delete(word, doc);
        }
    }

    private void obliterateSpecificDocFromMinHeap(DocumentImpl doc) {
        if (doc == null) {
            return;
        }
        minHeap.insert(doc.getKey()); // this way if a doc that was already obliterated has this method called on it again
        // it wont cause a null pointer exception!

        doc.setLastUseTime(Long.MIN_VALUE);
        minHeap.reHeapify(doc.getKey());
        minHeap.removeMin(); //downheaping the new min is done in removeMin()

    }

    private void updateTimeOnDocListAndReHeapify(List<DocumentImpl> sortedDocs) {

        long time = System.nanoTime();

        for (DocumentImpl doc : sortedDocs
        ) {
            doc.setLastUseTime(time);
            this.minHeap.reHeapify(doc.getKey());

            boolean wasJSon = false;
            if (getDocumentAsDocument(doc.getKey()) != getDocumentAsDocumentJsonNoReheap(doc.getKey())){
                wasJSon = true;
                if(wasJSon){
                    this.totalBytes+=getDocsTotalBytes(doc);
                    manageMemory();
                }
            }
        }
    }


    protected String getDocumentAsTxtNoReheap(URI uri) {
        //we get rid of the pm before getting so that only docs not on disk are returned
        bTreeImpl.setPersistenceManager(null);
        DocumentImpl document = (DocumentImpl)bTreeImpl.get(uri);
        DocumentPersistenceManager persistenceManager = new DocumentPersistenceManager(this.theBaseDir);
        bTreeImpl.setPersistenceManager(persistenceManager);
        if (document == null) {
            return null;
        } else {
            return document.getDocumentAsTxt();
        }
    }
    /**
     * set maximum number of documents that may be stored
     *
     * @param limit
     */
    public void setMaxDocumentCount(int limit){
        this.maxDocs = limit;
        while(totalDocs > this.maxDocs){
            DocumentImpl doc = getDocumentAsDocument(minHeap.removeMin()); //JSON to fix this get document
            //as document, and then call reheap
            //obliterateSpecificDocFromTrie(doc);// JSON this should not be done
            //removeFromStack(doc.getKey());

            try {
                bTreeImpl.moveToDisk(doc.getKey());
            }catch (Exception e){}

            totalDocs--;
            totalBytes-=getDocsTotalBytes(doc);

        }
    }

    private void manageMemory(){
        while(totalDocs > this.maxDocs || totalBytes >= this.maxBytes){
            DocumentImpl doc = getDocumentAsDocument(minHeap.removeMin());
            try {
                bTreeImpl.moveToDisk(doc.getKey());
            }catch (Exception e){}
            totalDocs--;
            totalBytes-=getDocsTotalBytes(doc);
        }
    }

    /**
     * set maximum number of bytes of memory that may be used by all the compressed documents in memory combined
     *
     * @param limit
     */
    public void setMaxDocumentBytes(int limit) {
        this.maxBytes = limit;
        while(totalBytes >= this.maxBytes){
            DocumentImpl doc = getDocumentAsDocument(minHeap.removeMin());
            try {
                bTreeImpl.moveToDisk(doc.getKey());

            }catch (Exception e){}
            totalDocs--;
            totalBytes-=getDocsTotalBytes(doc);


        }
    }

    private int getDocsTotalBytes(DocumentImpl document){
        return (document.getDocumentAsTxt().getBytes().length + document.getDocumentAsPdf().length);
    }

    private void subtractDocFromLimit(DocumentImpl document){
        totalDocs -= 1;
        totalBytes -= getDocsTotalBytes(document);
    }

    private void addDocToLimit(DocumentImpl document){
        totalDocs += 1;
        totalBytes += getDocsTotalBytes(document);
    }

    private boolean storageIsOpen(DocumentImpl document){
        return  (maxBytes >= (totalBytes + getDocsTotalBytes(document)) && maxDocs >= (totalDocs + 1));
    }



//                  ******************** ESSENTIAL TRIE AND HEAP METHODS END HERE *************************                    //

//                  *************************** TESTING METHODS START HERE ********************************


    protected DocumentImpl getDocumentAsDocumentReheap(URI uri) {
        DocumentImpl document = (DocumentImpl) bTreeImpl.get(uri);
        if (document == null) {
            return null;
        } else {
            document.setLastUseTime(System.nanoTime());
            minHeap.reHeapify(document.getKey());
            return document;

        }
    }

    protected DocumentImpl getDocumentAsDocumentJsonNoReheap(URI uri) {
        DocumentImpl document = (DocumentImpl) bTreeImpl.get(uri);
        if (document == null) {
            return null;
        } else {
            return document;
        }
    }

    protected DocumentImpl getDocument(URI uri){
        DocumentImpl document = getDocumentAsDocument(uri);
        if (document == null) {
            return null;
        } else {
            return document;
        }
    }

    private DocumentImpl createDoc(URI uri, String txt, int txtHash){
        DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode());
        addWordMapToTrie(newDocument);
        return newDocument;
    }

    private DocumentImpl createDoc(URI uri, String txt, int txtHash, byte[] pdfBytes){
        DocumentImpl newDocument = new DocumentImpl(uri, txt, txt.hashCode(), pdfBytes);
        addWordMapToTrie(newDocument);
        return newDocument;
    }


    private void addWordMapToTrie(DocumentImpl doc) {
        for (String key : doc.wordMap.keySet()) {
            this.trie.put(key, doc);
        }
    }
}