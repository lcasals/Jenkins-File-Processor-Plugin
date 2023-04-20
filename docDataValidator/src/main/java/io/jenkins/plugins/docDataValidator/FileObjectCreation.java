package io.jenkins.plugins.docDataValidator;

import com.ibm.icu.impl.InvalidFormatException;

import java.io.IOException;
import java.util.ArrayList;

public class FileObjectCreation {
    private static String directory;
    private String outputDirectory;
    private ArrayList<DocxFile> listOfDocxObjects = new ArrayList<>();
    private ArrayList<PptxFile> listOfPptxObjects = new ArrayList<>();
    private ArrayList<PdfFile> listOfPdfObjects = new ArrayList<>();
    private ArrayList<excelFile> listOfExcelObjects = new ArrayList<>();


    public ArrayList<DocxFile> getListOfDocxObjects() {
        return this.listOfDocxObjects;
    }
    public ArrayList<PdfFile> getListOfPdfObjects(){ return this.listOfPdfObjects;}
    public ArrayList<PptxFile> getListOfPptxObjects(){ return this.listOfPptxObjects;}
    public ArrayList<excelFile> getListOfExcelObjects(){ return this.listOfExcelObjects;}
    public void addDocxObject(DocxFile file){ this.listOfDocxObjects.add(file);}
    public void addPdfObject(PdfFile file){ this.listOfPdfObjects.add(file);}
    public void addPptxObject(PptxFile file){ this.listOfPptxObjects.add(file);}
    public void addExcelObject(excelFile file){ this.listOfExcelObjects.add(file);}

    public void createDocxObjects(ArrayList<String> docx, String dir, String outputDir) throws IOException, InvalidFormatException {
        directory = dir;
        outputDirectory = outputDir;
        for(String file: docx){
            try {
                addDocxObject(new DocxFile(file,directory, this.outputDirectory));
            } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void createPptxObjects(ArrayList<String> pptx, String dir, String outputDir) throws IOException, InvalidFormatException {
        directory = dir;
        outputDirectory = outputDir;
        for(String file: pptx){
            addPptxObject(new PptxFile(file,directory, this.outputDirectory));
        }
    }
    public void createPdfObjects(ArrayList<String> pdf, String dir, String outputDir) throws IOException {
        directory = dir;
        outputDirectory = outputDir;
        for(String file:pdf) {
            addPdfObject(new PdfFile(file, directory, this.outputDirectory));
        }
    }
    public void createExcelObjects(ArrayList<String> excel, String dir, String outputDir) throws IOException {
        directory = dir;
        outputDirectory = outputDir;
        for(String file:excel) {
            addExcelObject(new excelFile(file, directory,this.outputDirectory ));
        }
    }

}
