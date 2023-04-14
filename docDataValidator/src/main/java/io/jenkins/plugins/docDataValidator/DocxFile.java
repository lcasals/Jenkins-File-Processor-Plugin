package io.jenkins.plugins.docDataValidator;

import com.google.gson.Gson;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class DocxFile {
    File file;

    XWPFDocument document;
    public DocxFile(String name, String Directory, String outputDirectory) throws IOException, InvalidFormatException {
        this.fileName = name;
        this.Directory = Directory;
        this.outputDirectory = outputDirectory;
        this.file = new File(Directory+name);
        this.document = new XWPFDocument(new FileInputStream(Directory+name));
        this.setWordCount();
        this.setPageCount();
        this.setAuthor();
        this.setDateOfCreation();
        this.setFileSize();
        findUrl();
        System.out.println("The files found are in " + fileName + " are: "+ getLocatedURLs());
    }
    private String fileName;
    private String Directory;
    private String outputDirectory;
    private String allData;
    private int wordCount;
    private int pageCount;
    private String author;
    private long fileSize;
    private Date dateOfCreation;
    private ArrayList<String> locatedURLs = new ArrayList<>();

    //Stores each links response code
    private HashMap<String, Integer> linksInFile = new HashMap<String, Integer>();
    //Stores emails and if they're valid
    private HashMap<String, Integer> emailsInFile = new HashMap<String, Integer>();
    private HashMap<String, String> fileValidation = new HashMap<String, String>(){{
        put("emails","null");
        put("links", "null");
        put("grammar", "null");
    }};

    public void setWordCount() throws IOException {
        XWPFWordExtractor extractor = new XWPFWordExtractor(this.document);
        String allText = extractor.getText();
        String[] words = allText.split("\\s+");
        int wordCount = words.length;
        //int wordCount = allText.length();
        this.wordCount = wordCount;

    }
    public Integer getWordCount(){
        return this.wordCount;
    }
    public void setPageCount() throws IOException {
        int pageCount = this.document.getProperties().getExtendedProperties().getPages();

        this.pageCount = pageCount;
    }
    public int getPageCount(){
        return this.pageCount;
    }
    public void setAuthor() throws IOException {
        String author = this.document.getProperties().getCoreProperties().getCreator();
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setFileSize(){
        File file = new File(Directory+fileName);

        // Get the size of the file in bytes
        long fileSizeInBytes = file.length();

        // Print the size of the file
        this.fileSize = fileSizeInBytes;
    }
    public long getFileSize(){
        return this.fileSize;
    }
    public void setDateOfCreation() throws IOException {

        // Get the creation date of the document from its properties
        Date creationDate = this.document.getProperties().getCoreProperties().getCreated();
        Date date = creationDate;

        // Close the document
        this.dateOfCreation = date;
    }
    public Date getDateOfCreation(){
        return this.dateOfCreation;
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
    public ArrayList<String> getLocatedURLs()
    {
        return this.locatedURLs;
    }
    public void findUrl() throws IOException {
        try {
            XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(this.document);
            Iterator<XWPFParagraph> i = document.getParagraphsIterator();
            while(i.hasNext()) {
                XWPFParagraph paragraph = i.next();
                //Going through paragraph text
                for (XWPFRun run : paragraph.getRuns()) {
                    if (run instanceof XWPFHyperlinkRun) {
                        //Finding the urls
                        XWPFHyperlink link = ((XWPFHyperlinkRun) run).getHyperlink(document);
                        String linkURL = link.getURL();
                        locatedURLs.add(linkURL);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createJSON(){
        this.allData = "{'name': '" + getFileName() + "',\n 'author': '"+getAuthor()+"',\n 'pagecount': "+getPageCount()+
                ",\n 'filesize': "+getFileSize()+",\n 'wordcount': "+getWordCount()+",\n 'created': '"+getDateOfCreation()+
                "',\n'URLs':'"+getLocatedURLs()+"'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(this.allData, Object.class);

        String outputFilePath = outputDirectory;

        //Check if Folder exists, if not: create it
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        // Write the JSON object to a file
        try (FileWriter fileWriter = new FileWriter(outputFilePath + File.separator + getFileName() + ".json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
