package io.jenkins.plugins.docDataValidator;

import com.ibm.icu.impl.InvalidFormatException;
import hudson.model.TaskListener;

import java.io.IOException;

public class driver {

    //change to accept an array of arrays and create a for loop to enter each to make it.
    public static void main(String inputDirectory, String outputDirectory, TaskListener listener, int urlFlag) throws IOException, InvalidFormatException {
        FileTypeDetection FilesForClassOne = new FileTypeDetection(inputDirectory);
        FilesForClassOne.setFileNames(listener);
        FileObjectCreation createObj = new FileObjectCreation();

        createObj.createDocxObjects(FilesForClassOne.getDOCXNames(), inputDirectory,outputDirectory);
        createObj.createPdfObjects(FilesForClassOne.getPDFNames(), inputDirectory,outputDirectory);
        createObj.createPptxObjects(FilesForClassOne.getPPTXNames(),inputDirectory,outputDirectory);
        createObj.createExcelObjects(FilesForClassOne.getEXCELNames(),inputDirectory,outputDirectory);


        listener.getLogger().println("\n\n-------------\n\nword documents\n\n-------------");
        for(DocxFile docs: createObj.getListOfDocxObjects()){
             listener.getLogger().println("name of file: "+docs.getFileName()+"\n");
             listener.getLogger().println("name of author: "+docs.getAuthor()+"\n");
             listener.getLogger().println("Word count: "+docs.getWordCount()+"\n");
             listener.getLogger().println("file size: " + docs.getFileSize()+"\n");
             listener.getLogger().println("Page count: "+docs.getPageCount()+"\n");
             listener.getLogger().println("Page count: "+docs.getDateOfCreation()+"\n");
             if(urlFlag == 1)
             {
                 LinkDetection.main(docs.getLocatedURLs(),listener);
             }
             listener.getLogger().println("\n\n-------------------");
             docs.createJSON();
        }
         listener.getLogger().println("\n\nPrinting pdf file data \n\n-----------------");
        for(PdfFile pdf: createObj.getListOfPdfObjects()){


             listener.getLogger().println("name of file: " + pdf.getFileName()+"\n");
             listener.getLogger().println("name of author: " + pdf.getAuthor()+"\n");
             listener.getLogger().println("page count: " + pdf.getPageCount()+"\n");
             listener.getLogger().println("file size: " + pdf.getFileSize()+"\n");
             listener.getLogger().println("word count: " + pdf.getWordCount()+"\n");
             listener.getLogger().println("date created: " + pdf.getFileMonth()+"/"+pdf.getFileDay()+
                    "/"+pdf.getFileYear()+" " +pdf.getFileHour()+":"+pdf.getFileMinute()+":"
                    +pdf.getFileSecond()+"\n");
            if(urlFlag == 1)
            {
                LinkDetection.main(pdf.getLocatedURLs(), listener);
            }
             listener.getLogger().println("\n\n-------------------");
            pdf.createJSON();
        }
         listener.getLogger().println("\n\nPowerpoint files \n\n---------------");
        for(PptxFile pptx: createObj.getListOfPptxObjects()){
             listener.getLogger().println("name of file: "+pptx.getFileName()+"\n");
             listener.getLogger().println("name of author: "+ pptx.getAuthor()+"\n");
             listener.getLogger().println("Word count: "+ pptx.getWordCount()+"\n");
             listener.getLogger().println("file size: " + pptx.getFileSize()+"\n");
             listener.getLogger().println("Page count: "+ pptx.getNumberOfSlides()+"\n");
             listener.getLogger().println("date created: " + pptx.getCreationDate()+"\n");
            if(urlFlag == 1)
            {
                LinkDetection.main(pptx.getLocatedURLs(),listener);
            }
             listener.getLogger().println("\n\n-------------------");
            pptx.createJSON();
        }

        listener.getLogger().println("\n\nExcel files \n\n---------------");
        for(excelFile excel: createObj.getListOfExcelObjects()){
            listener.getLogger().println("name of file: "+excel.getFileName()+"\n");
            listener.getLogger().println("name of author: "+ excel.getAuthor()+"\n");
            listener.getLogger().println("Word count: "+ excel.getWordCount()+"\n");
            listener.getLogger().println("file size: " + excel.getFileSize()+"\n");
            listener.getLogger().println("Row count: " + excel.getRowCount()+"\n");
            listener.getLogger().println("date created: " + excel.getCreationTime());
            listener.getLogger().println("\n\n-------------------");
            excel.createJSON();
        }

        listener.getLogger().println("\n\n-------------\n\nUnknown documents\n\n-------------");
        for(String unknownFileNames : FilesForClassOne.getUNKNOWNNames())
        {
            listener.getLogger().println("name of file: "+ unknownFileNames +"\n");
            listener.getLogger().println("\n\n-------------------");
        }

    }
}


