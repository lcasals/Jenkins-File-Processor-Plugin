package io.jenkins.plugins.docDataValidator;

import com.ibm.icu.impl.InvalidFormatException;
import hudson.model.TaskListener;

import java.io.IOException;

public class driver {

    public static void main(String inputDirectory, String outputDirectory, TaskListener listener, int urlFlag) throws IOException, InvalidFormatException {
        //Create FilTypeDetection Object and set the file name then print them
        FileTypeDetection FilesForClassOne = new FileTypeDetection(inputDirectory);
        FilesForClassOne.setFileNames(listener);
        FilesForClassOne.printFileNames(listener);

        //Creates objects for each class
        FileObjectCreation createObj = new FileObjectCreation();
        createObj.createDocxObjects(FilesForClassOne.getDOCXNames(), inputDirectory,outputDirectory);
        createObj.createPdfObjects(FilesForClassOne.getPDFNames(), inputDirectory,outputDirectory);
        createObj.createPptxObjects(FilesForClassOne.getPPTXNames(),inputDirectory,outputDirectory);
        createObj.createExcelObjects(FilesForClassOne.getEXCELNames(),inputDirectory,outputDirectory);

        //outputs the current directory
        listener.getLogger().println("In the Driver.java class. Checking the directory:");
        listener.getLogger().println("\n\t Traversing files in directory: " + inputDirectory + "\n");

        //Everything past this line simply prints file information onto console
        listener.getLogger().println("\n\n-------------\n\nword documents\n\n-------------");
        for(DocxFile docs: createObj.getListOfDocxObjects()){

             listener.getLogger().println("name of file: "+docs.getFileName()+"\n");
             listener.getLogger().println("name of author: "+docs.getAuthor()+"\n");
             listener.getLogger().println("Word count: "+docs.getWordCount()+"\n");
             listener.getLogger().println("file size: " + docs.getFileSize()+"\n");
             listener.getLogger().println("Page count: "+docs.getPageCount()+"\n");
             listener.getLogger().println("Date created: "+docs.getDateOfCreation()+"\n");

            //if the checkbox is clicked or 'true', it will check the URLs
             if(urlFlag == 1)
             {
                 LinkDetection.main(docs.getLocatedURLs(),listener);
             }
             listener.getLogger().println("\n\n-------------------");

            //creates JSON equivalent of docx file(s)
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

            //if the checkbox is clicked or 'true', it will check the URLs
            if(urlFlag == 1)
            {
                LinkDetection.main(pdf.getLocatedURLs(), listener);
            }
             listener.getLogger().println("\n\n-------------------");

            //creates JSON equivalent of pdf file(s)
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

            //if the checkbox is clicked or 'true', it will check the URLs
            if(urlFlag == 1)
            {
                LinkDetection.main(pptx.getLocatedURLs(),listener);
            }
             listener.getLogger().println("\n\n-------------------");

            //creates JSON equivalent of pptx file(s)
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

            //if the checkbox is clicked or 'true', it will check the URLs
            if(urlFlag == 1)
            {
                LinkDetection.main(excel.getLocatedURLs(),listener);
            }
            listener.getLogger().println("\n\n-------------------");

            //creates JSON equivalent of Excel file(s)
            excel.createJSON();
        }

        //Outputs and files that are not of the file types above
        listener.getLogger().println("\n\n-------------\n\nOther documents\n\n-------------");
        for(String unknownFileNames : FilesForClassOne.getUNKNOWNNames())
        {
            listener.getLogger().println("name of file: "+ unknownFileNames +"\n");
            listener.getLogger().println("\n\n-------------------");
        }

    }
}


