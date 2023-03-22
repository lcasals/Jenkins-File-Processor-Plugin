package io.jenkins.plugins.docDataValidator;

import com.google.gson.Gson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PdfFile {


    private File file;
    private PDDocument doc;
    private PDDocumentInformation pdd;

    public PdfFile(String fileName, String directory) throws IOException {


        this.file = new File(directory+fileName);
        this.doc = PDDocument.load(file);
        this.pdd = this.doc.getDocumentInformation();
        this.fileName = fileName;
        this.author = pdd.getAuthor();
        this.title = pdd.getTitle();
        this.subject = pdd.getSubject();
        this.creator = pdd.getCreator();
        this.pageCount = doc.getNumberOfPages();
        this.fileSize = file.length();
        setWordCount();

        //Laura stuff//
        this.fileMonth = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.MONTH);
        this.fileDay = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.DATE);
        this.fileYear = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.YEAR);
        this.fileHour = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.HOUR);
        this.fileMinute = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.MINUTE);
        this.fileSecond = ((GregorianCalendar) pdd.getCreationDate()).get(Calendar.SECOND);

        //Goes through each PDF file and skips all spaces to get appropriate word count
        this.creationDate = getFileMonth()+"/"+getFileDay()+
                "/"+getFileYear()+" " +getFileHour()+":"+getFileMinute()+":"
                +getFileSecond();

        doc.close();

    }

    //defining variables
    private int fileMonth;
    private int fileDay;
    private int fileYear;
    private int fileHour;
    private int fileMinute;
    private int fileSecond;
    private int wordCount;
    private String allData;
    private GregorianCalendar lastModificationDate;
    private String creator;
    private String subject;
    private String title;
    private int pageCount;
    private String author;
    private long fileSize;
    private String creationDate;
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
    public void setWordCount() throws IOException {
        int count = 0;
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setEndPage(20);
        String text = stripper.getText(this.doc);
        String[] texts = text.split(" ");
        for (String txt : texts) {
            if (txt.trim().length() != 0)
            {
                count = count + 1;
            }
        }
        //end Laura section//
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
    public String getDateOfCreation(){
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

    public void createJSON(){
        this.allData = "{'name': '" + getFileName() + "',\n 'author': '"+getAuthor()+"',\n 'pagecount': "+getPageCount()+
                ",\n 'filesize': "+getFileSize()+",\n 'wordcount': "+getWordCount()+",\n 'created': '"+getDateOfCreation()+"'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(this.allData, Object.class);
        String outputFilePath = "./src/FileOutput/";

        // Write the JSON object to a file
        try (FileWriter fileWriter = new FileWriter(outputFilePath+getFileName()+".json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

}