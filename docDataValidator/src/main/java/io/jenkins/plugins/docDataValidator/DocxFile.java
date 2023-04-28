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
    }
    private String fileName;
    private String Directory;
    private String outputDirectory;
    private int wordCount;
    private int pageCount;
    private String author;
    private long fileSize;
    private Date dateOfCreation;
    private final ArrayList<String> locatedURLs = new ArrayList<>();

    public void setWordCount(){
        XWPFWordExtractor extractor = new XWPFWordExtractor(this.document);
        String allText = extractor.getText();
        String[] words = allText.split("\\s+");
        this.wordCount = words.length;;

    }
    public Integer getWordCount(){
        return this.wordCount;
    }
    public void setPageCount() {
        this.pageCount= this.document.getProperties().getExtendedProperties().getPages();
    }
    public int getPageCount(){
        return this.pageCount;
    }
    public void setAuthor() throws IOException {
        this.author= this.document.getProperties().getCoreProperties().getCreator();
    }
    public String getAuthor(){
        return this.author;
    }
    public void setFileSize(){
        File file = new File(Directory+fileName);

        // Get the size of the file in bytes
        this.fileSize = file.length();
    }
    public long getFileSize(){
        return this.fileSize;
    }
    public void setDateOfCreation() {
        // Get the creation date of the document from its properties
        this.dateOfCreation = this.document.getProperties().getCoreProperties().getCreated();
    }
    public Date getDateOfCreation(){
        return this.dateOfCreation;
    }
    public String getFileName() {
        return this.fileName;
    }
    public ArrayList<String> getLocatedURLs()
    {
        return this.locatedURLs;
    }
    public void findUrl(){
        try {
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
        String allData = "{'name': '" + getFileName() + "',\n 'author': '" + getAuthor() + "',\n 'page count': " + getPageCount() +
                ",\n 'file size': " + getFileSize() + ",\n 'word count': " + getWordCount() + ",\n 'created': '" + getDateOfCreation() +
                "',\n'URLs':'" + getLocatedURLs() + "'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(allData, Object.class);

        //Check if Folder exists, if not: create it
        File outputDir = new File(outputDirectory);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        // Write the JSON object to a file
        try (FileWriter fileWriter = new FileWriter(outputDirectory + File.separator + getFileName() + ".json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
