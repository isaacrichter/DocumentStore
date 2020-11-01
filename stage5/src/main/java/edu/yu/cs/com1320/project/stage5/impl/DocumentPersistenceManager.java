package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
import org.apache.pdfbox.io.IOUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;


/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    protected File baseDir;

    public DocumentPersistenceManager(File baseDir){
        if (baseDir == null){
            this.baseDir = new File(System.getProperty("user.dir"));
        }else{
            this.baseDir = baseDir;
        }

    }
    public DocumentPersistenceManager(){
        this.baseDir = new File(System.getProperty("user.dir"));
    }


    class DocumentSerializer implements JsonSerializer<Document> {
        public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
            //create json obj
            //get and add each needed element/attribute
            //return obj
            JsonObject object = new JsonObject();
            String txt = document.getDocumentAsTxt().replaceAll(" ", "_");
            object.addProperty("txt", txt);
            int txtHash = document.getDocumentTextHashCode();
            object.addProperty("txtHash", txtHash);
            String uri = document.getKey().toString();
            object.addProperty("uri", uri);
            return object;
        }
    }


    class DocumentDeserialiser implements JsonDeserializer<Document> {
        @Override
        public Document deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String docTxt = json.getAsJsonObject().get("txt").getAsString();
                String txt = docTxt.replace("_", " ");

                int txtHash = json.getAsJsonObject().get("txtHash").getAsInt();

                URI uri = new URI(json.getAsJsonObject().get("uri").getAsString());

                Document document = new DocumentImpl(uri, txt, txtHash);
                return document;
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    //@Override
    public void serialize(URI uri, Document val) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentSerializer()).create();
        Type type = new TypeToken<Document>() {}.getType();
        placeFile(uri, gson.toJson(val, type));
    }

             private void placeFile(URI totalUri, String jsonString) throws IOException {
                 String treatedURI = uriFormatter(totalUri);
                 String[] uriPieces = uriSeparator(treatedURI);

                 int dirToMake = uriPieces.length - 1; //because the last one is the name of the actual file
                 int index = 0;
                 while(dirToMake > index){
                     StringBuffer endOfFilePath = new StringBuffer();
                     for (int i= 0; i <= index ; i++){
                         endOfFilePath.append(File.separator).append(uriPieces[i]);
                     }
                     //create the new directory
                     boolean created;
                     created = new File(baseDir.toString()+ endOfFilePath.toString()).mkdir();
                     index++;
                 }
                 //create the file!
                 File jsonFile = new File( baseDir + File.separator+ treatedURI +  ".json");
                 //jsonFile.createNewFile();

                 // creates a FileWriter Object
                 FileWriter writer = new FileWriter(jsonFile);

                 // Writes the content to the file
                 writer.write(jsonString);
                 writer.flush();
                 writer.close();



             }

            private String[] uriSeparator(String newuri){
                String[] uriPieces;
                if(("\\").equals(File.separator)){
                    uriPieces = newuri.split("\\\\"); // because it is in a regex, "\\\\" just means "\"
                } else {
                    uriPieces = newuri.split(File.separator);
                }
                return uriPieces;
            }

            private String uriFormatter(URI uri){

                String uriPath = uri.getSchemeSpecificPart();

                String newUri = uriPath
                        .replace("\\\\", "/")
                        .replace("/", File.separator);
                return newUri;

            }

    public Document deserialize(URI uri) throws IOException {
        //use URI to read json off disk
        String file = baseDir + File.separator + uriFormatter(uri) + ".json";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
        }catch (java.io.FileNotFoundException e){
            return null;
        }
        String currentLine = reader.readLine();
        reader.close();
        Gson gson = new GsonBuilder().registerTypeAdapter(Document.class, new DocumentDeserialiser()).create();
        Type docType = new TypeToken<Document>() {}.getType();
        Document doc = gson.fromJson(currentLine, docType);

        //delete file and empty dirs
        File fileToDelete = new File(file);
        fileToDelete.delete();

        //and empty dirs if there are any
        String treatedURI = uriFormatter(uri);
        String[] uriPieces = uriSeparator(treatedURI);
        //subtract the length of the uri piece (want dir, not file just deleted) (no - 1 to account for the File.Separator char) using substring
        StringBuffer dirToDeleteLocation = new StringBuffer(this.baseDir + File.separator + treatedURI.substring(0, (treatedURI.length()) - (uriPieces[uriPieces.length -1].length())));
        recursiveDeleteDir(dirToDeleteLocation, uriPieces, 1);
        return doc;

    }


        private Document deserializer(JsonElement serializedDoc){
            Type documentType = new TypeToken<Document>() {}.getType();
            Gson gson = new Gson();
            Document doc = gson.fromJson(serializedDoc, documentType);


        return  null;
        }
    private void recursiveDeleteDir(StringBuffer dirToDeleteLocation ,String[] uriPieces, int i){
        File dirToDelete = new File(String.valueOf(dirToDeleteLocation));


        if(( !dirToDelete.toString().equals(baseDir.toString()))
                && dirToDelete.list().length==0) {
            //delete, recursive call, else return
            dirToDelete.delete();
            i = i+1;

            StringBuffer sb = new StringBuffer((dirToDeleteLocation.substring(0, ((dirToDeleteLocation.length()-1) - uriPieces[uriPieces.length -(i)].length()))));

            recursiveDeleteDir(sb,uriPieces, i);
        }else{
            return;
        }
        }

}