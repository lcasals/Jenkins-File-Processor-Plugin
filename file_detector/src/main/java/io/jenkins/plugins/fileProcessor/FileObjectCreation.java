package io.jenkins.plugins.fileProcessor;

import java.io.IOException;
import java.util.ArrayList;

public class FileObjectCreation {

    //private static String DIRECTORY = "/Users/anacasals/IdeaProjects/Jenkins-File-Processor-Plugin/file_detector/FileInput/" ;
    private static String directory;
    private ArrayList<DocxFile> listOfDocxObjects = new ArrayList<>();
    private ArrayList<DocxFile> listOfPptxObjects = new ArrayList<>();
    private ArrayList<PdfFile> listOfPdfObjects = new ArrayList<>();


    public ArrayList<DocxFile> getListOfDocxObjects() {
        return this.listOfDocxObjects;
    }
    public ArrayList<PdfFile> getListOfPdfObjects(){ return this.listOfPdfObjects;}
    public void addDocxObject(DocxFile file){
        this.listOfDocxObjects.add(file);
    }
    public void addPdfObject(PdfFile file){ this.listOfPdfObjects.add(file);};
    public void createDocxObjects(ArrayList<String> docx){
        for(String file: docx){
            addDocxObject(new DocxFile(file,this.directory));
        }
    }
    public void createPdfObjects(ArrayList<String> pdf, String dir) throws IOException {
        directory = dir;
        for(String file:pdf) {
            addPdfObject(new PdfFile(file, this.directory));
        }
    }



    public static void main(String args){

    }

}
