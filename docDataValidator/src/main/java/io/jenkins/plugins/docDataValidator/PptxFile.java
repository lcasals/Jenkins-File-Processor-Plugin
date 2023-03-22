package io.jenkins.plugins.docDataValidator;
import com.google.gson.Gson;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class PptxFile {
    File file;

    public PptxFile(String nameOfFile, String directory) throws IOException {
        this.file = new File(directory + nameOfFile);
        this.Directory = directory;
        this.fileName = nameOfFile;

        this.pptx = new XMLSlideShow(new FileInputStream(Directory + fileName));

        //sets the value of fileName to the parameter nameOfFile


        //calls the setFileSize method and returns the files length in bytes
        setFileSize();

        //calls the setAuthorName method and extracts the name of the file creator
        setAuthorName();

        //calls the setCreationDate method to extract timestamp
        setCreationDate();

        //calls the setNumberOfSlides methods and returns the number of slides counted
        setNumberOfSlides();

        //calls the setWordCount method which returns the word count
        //Issue, this method also counts words in images...
        setWordCount();

    }

    //variable definitions

    private String fileName;
    private String Directory;

    private XMLSlideShow pptx;
    private int numberOfSlides;
    private String author;
    private String creationDate;
    private long fileSize;
    private int wordCount;
    private String allData;

    //get and set methods

    public String getFileName() {
        return this.fileName;
    }

    public void setFileSize() {
        this.fileSize = this.file.length();
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setAuthorName() {
        POIXMLProperties.CoreProperties props = this.pptx.getProperties().getCoreProperties();
        this.author = String.valueOf(props.getCreator());
    }

    public String getAuthor() {
        return this.author;
    }

    public void setNumberOfSlides() throws IOException {
        int slideCount = 0;
        //changed slide to ignored...not exactly sure how this works...
        for (XSLFSlide ignored : this.pptx.getSlides()) {
            slideCount++;
        }
        this.numberOfSlides = slideCount;
    }

    public int getNumberOfSlides() {
        return this.numberOfSlides;
    }

    public void setCreationDate() {
        POIXMLProperties.CoreProperties props = this.pptx.getProperties().getCoreProperties();
        this.creationDate = String.valueOf(props.getCreated());
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setWordCount() {
        int count = 0;
        for (XSLFSlide slide : this.pptx.getSlides()) {
            XSLFShape[] shapes = slide.getShapes().toArray(new XSLFShape[0]);
            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    String text = textShape.getText().trim();
                    if (text.length() != 0) {
                        count++;
                        //System.out.println("Text: " + text);
                    }
                }
            }
        }
        this.wordCount = count;
    }

    public int getWordCount() {
        return this.wordCount;
    }

    public void createJSON() {
        this.allData = "{'name': '" + getFileName() + "',\n 'author': '" + getAuthor() + "',\n 'slide count': " + getNumberOfSlides() +
                ",\n 'filesize': " + getFileSize() + ",\n 'word count': " + getWordCount() + ",\n 'created': '" + getCreationDate() + "'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(this.allData, Object.class);
        String outputFilePath = "./src/FileOutput/";

        // Write the JSON object to a file
        try (FileWriter fileWriter = new FileWriter(outputFilePath + getFileName() + ".json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
