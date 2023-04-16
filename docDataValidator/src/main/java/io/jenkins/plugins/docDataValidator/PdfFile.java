package io.jenkins.plugins.docDataValidator;

import com.google.gson.Gson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PdfFile {


    private File file;
    private PDDocument doc;
    private PDDocumentInformation pdd;
    private String outputDirectory;

    public PdfFile(String fileName, String directory, String outputDirectory) throws IOException {
        this.file = new File(directory, fileName);
        this.outputDirectory = outputDirectory;
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
        this.fileMonth = (pdd.getCreationDate()).get(Calendar.MONTH);
        this.fileDay = (pdd.getCreationDate()).get(Calendar.DATE);
        this.fileYear = (pdd.getCreationDate()).get(Calendar.YEAR);
        this.fileHour = (pdd.getCreationDate()).get(Calendar.HOUR);
        this.fileMinute = (pdd.getCreationDate()).get(Calendar.MINUTE);
        this.fileSecond = (pdd.getCreationDate()).get(Calendar.SECOND);

        //Goes through each PDF file and skips all spaces to get appropriate word count
        this.creationDate = getFileMonth()+"/"+getFileDay()+
                "/"+getFileYear()+" " +getFileHour()+":"+getFileMinute()+":"
                +getFileSecond();

        findUrl();

        System.out.println("The URLs found are in " + fileName + " are: "+ getLocatedURLs());

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
    private int pdfPageNum;
    //Stores each links response code
    private HashMap<String, Integer> linksInFile = new HashMap<String, Integer>();
    //Stores emails and if they're valid
    private HashMap<String, Integer> emailsInFile = new HashMap<String, Integer>();
    private HashMap<String, String> fileValidation = new HashMap<String, String>(){{
        put("emails","null");
        put("links", "null");
        put("grammar", "null");
    }};
    private ArrayList<String> locatedURLs = new ArrayList<>();
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
    public ArrayList<String> getLocatedURLs()
    {
        return this.locatedURLs;
    }
    public void findUrl() throws IOException {
        //https://svn.apache.org/repos/asf/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/PrintURLs.java
        pdfPageNum = 0;
        for( PDPage page : doc.getPages() )
        {
            pdfPageNum++;
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            List<PDAnnotation> annotations = page.getAnnotations();
            //first setup text extraction regions
            for( int j=0; j<annotations.size(); j++ )
            {
                PDAnnotation annot = annotations.get(j);
                if( annot instanceof PDAnnotationLink)
                {
                    PDAnnotationLink link = (PDAnnotationLink)annot;
                    PDRectangle rect = link.getRectangle();
                    //need to reposition link rectangle to match text space
                    float x = rect.getLowerLeftX();
                    float y = rect.getUpperRightY();
                    float width = rect.getWidth();
                    float height = rect.getHeight();
                    int rotation = page.getRotation();
                    if( rotation == 0 )
                    {
                        PDRectangle pageSize = page.getMediaBox();
                        y = pageSize.getHeight() - y;
                    }
                    else if( rotation == 90 )
                    {
                        //do nothing
                        System.out.println("Nothing occurred");
                    }

                    Rectangle2D.Float awtRect = new Rectangle2D.Float( x,y,width,height );
                    stripper.addRegion( "" + j, awtRect );
                }
            }

            stripper.extractRegions( page );

            for( int j=0; j<annotations.size(); j++ )
            {
                PDAnnotation annot = annotations.get(j);
                if( annot instanceof PDAnnotationLink )
                {
                    PDAnnotationLink link = (PDAnnotationLink)annot;
                    PDAction action = link.getAction();
                    String urlText = stripper.getTextForRegion( "" + j );
                    if( action instanceof PDActionURI)
                    {
                        PDActionURI uri = (PDActionURI)action;
                        //System.out.println( "Page " + pdfPageNum +":'" + urlText.trim() + "'=" + uri.getURI() );
                        locatedURLs.add(uri.getURI());
                    }
                }
            }
        }
    }

    public void createJSON(){
        this.allData = "{'name': '" + getFileName() + "',\n 'author': '"+getAuthor()+"',\n 'pagecount': "+getPageCount()+
                ",\n 'filesize': "+getFileSize()+",\n 'wordcount': "+getWordCount()+",\n 'created': '"+getDateOfCreation()+
                "',\n'URLs':'"+getLocatedURLs()+"'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(this.allData, Object.class);
        //String outputFilePath = "./src/FileOutput/";
        String outputFilePath = outputDirectory;
        System.out.println("THIS IS THE OUTPUT FILE PATH FOR THE PDF FILES' JSON: " + outputFilePath);

        //Check if Folder exists, if not: create it
        //using built in File Java object to use the mkdirs() and exists() methods
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