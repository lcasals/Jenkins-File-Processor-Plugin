package io.jenkins.plugins.fileProcessor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PdfFile {


    private File file;
    private PDDocument doc;
    //private PDDocumentInformation pdd;

    public PdfFile(String fileName, String directory) throws IOException {


        this.file = new File(directory+fileName);
        this.doc = PDDocument.load(file);
        PDDocumentInformation pdd = this.doc.getDocumentInformation();
        this.fileName = fileName;
        this.author = pdd.getAuthor();
        this.title = pdd.getTitle();
        this.subject = pdd.getSubject();
        this.pageCount = doc.getNumberOfPages();
        this.fileSize = file.length();

        //Laura stuff//
        this.fileMonth = (pdd.getCreationDate()).get(Calendar.MONTH);
        this.fileDay = (pdd.getCreationDate()).get(Calendar.DATE);
        this.fileYear = (pdd.getCreationDate()).get(Calendar.YEAR);
        this.fileHour = (pdd.getCreationDate()).get(Calendar.HOUR);
        this.fileMinute = (pdd.getCreationDate()).get(Calendar.MINUTE);
        this.fileSecond = (pdd.getCreationDate()).get(Calendar.SECOND);


        int count = 0;
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setEndPage(20);
        String text = stripper.getText(doc);
        String[] texts = text.split(" ");
        for (String txt : texts) {
            if(txt.trim().length() != 0)
            {
                count = count + 1;
            }
        }
        this.wordCount = count;


        //end of Laura stuff//

        doc.close();

    }

    //add dateCreation Laura
    private int fileMonth;
    private int fileDay;
    private int fileYear;
    private int fileHour;
    private int fileMinute;
    private int fileSecond;
    //end of Laura section
    private int wordCount;
    private GregorianCalendar lastModificationDate;
    private String creator;
    private String subject;
    private String title;
    private int pageCount;
    private String author;
    private long fileSize;
    private GregorianCalendar creationDate;
    private String fileName;
    //Stores each links response code
    private HashMap<String, Integer> linksInFile = new HashMap<String, Integer>();
    //Stores emails and if they're valid
    private HashMap<String, Integer> emailsInFile = new HashMap<String, Integer>();
    private HashMap<String, String> fileValidation = new HashMap<String, String>(){{
        put("emails","null");
        put("links", "null");
        put("grammar", "null");
    }};

    //Get and set methods
    //laura section//
    public int getFileMonth(){
        return this.fileMonth;
    }
    public int getFileDay(){
        return this.fileDay;
    }
    public int getFileYear(){
        return this.fileYear;
    }
    public int getFileHour(){
        return this.fileHour;
    }
    public int getFileMinute(){
        return this.fileMinute;
    }
    public int getFileSecond(){
        return this.fileSecond;
    }
    //end Laura section//
    public void setWordCount(Integer count){
        this.wordCount = count;
    }

    public Integer getWordCount(){
        return this.wordCount;
    }
    public void setPageCount(int count){
        this.pageCount = count;
    }
    public int getPageCount(){
        return this.pageCount;
    }
    public void setAuthor(String name){
        this.author = name;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setFileSize(int size){
        this.fileSize = size;
    }
    public long getFileSize(){
        return this.fileSize;
    }
    public GregorianCalendar getDateOfCreation(){
        return this.creationDate;
    }
    //returns whether there is a flag in emails, links, or grammar.
    public String getErrorFlag(String validate){
        return this.fileValidation.get(validate);
    }
    public void setErrorFlag(String validate, String error){
        this.fileValidation.put(validate, error);
    }

    //returns the response code for a validating a specific email
    public Integer getEmailResponse(String email){
        return this.emailsInFile.get(email);
    }
    public void setEmailResponse(String email, Integer code){
        this.emailsInFile.put(email, code);
    }

    public String getFileName() {
        return this.fileName = fileName;
    }
    public void setLinksResponseInFile(String link, Integer code){
        this.linksInFile.put(link, code);
    }
    public Integer getLinkResponseCode(String link){
        return this.linksInFile.get(link);
    }

    public void OutPutJson(){

    }
}