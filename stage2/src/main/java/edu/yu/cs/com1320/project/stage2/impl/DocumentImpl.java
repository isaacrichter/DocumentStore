package edu.yu.cs.com1320.project.stage2.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import edu.yu.cs.com1320.project.stage2.Document;
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




    //constructor method that takes in params of putDocument from document store
    public DocumentImpl(URI uri, String txt, int txtHash){
        this.uri = uri;
        this.txt = txt;
        this.txtHash = (txtHash & 0x7fffffff);
        this.pdfBytes = txtToPdf(txt);

    }

    public DocumentImpl(URI uri, String txt, int txtHash, byte[] pdfBytes){
        this.uri = uri;
        this.txt = txt;
        this.txtHash = (txtHash & 0x7fffffff);
        this.pdfBytes = pdfBytes;
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
                //pdDoc.save("HelloWorld!.pdf");
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
    public URI getKey() {
        return uri;

    }


    public boolean equals(DocumentImpl doc2){
        return this.txtHash == doc2.txtHash;

    }
}