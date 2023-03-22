package io.jenkins.plugins.docDataValidator;

import com.ibm.icu.impl.InvalidFormatException;
import hudson.model.TaskListener;

import java.io.IOException;
import java.util.ArrayList;

public class driver {

    //change to accept an array of arrays and create a for loop to enter each to make it.
    public static void main(ArrayList<String> pdfArray, TaskListener listener, ArrayList<String> docxArray, ArrayList<String> pptxArray, String dir) throws IOException, InvalidFormatException {

        System.out.print("Running second program\n");
        FileObjectCreation createobj = new FileObjectCreation();

        createobj.createDocxObjects(docxArray, dir);
        createobj.createPdfObjects(pdfArray, dir);
        createobj.createPptxObjects(pptxArray,dir);
         listener.getLogger().println("\n\n-------------\n\nword documents\n\n-------------");
        for(DocxFile docs: createobj.getListOfDocxObjects()){
             listener.getLogger().println("name of file: "+docs.getFileName()+"\n");
             listener.getLogger().println("name of author: "+docs.getAuthor()+"\n");
             listener.getLogger().println("Word count: "+docs.getWordCount()+"\n");
             listener.getLogger().println("file size: " + docs.getFileSize()+"\n");
             listener.getLogger().println("Page count: "+docs.getPageCount()+"\n");
             listener.getLogger().println("Page count: "+docs.getDateOfCreation()+"\n");

             listener.getLogger().println("\n\n-------------------");
            docs.createJSON();
        }
         listener.getLogger().println("\n\nPrinting pdf file data \n\n-----------------");
        for(PdfFile pdf: createobj.getListOfPdfObjects()){


             listener.getLogger().println("name of file: " + pdf.getFileName()+"\n");
             listener.getLogger().println("name of author: " + pdf.getAuthor()+"\n");
             listener.getLogger().println("page count: " + pdf.getPageCount()+"\n");
             listener.getLogger().println("file size: " + pdf.getFileSize()+"\n");
             listener.getLogger().println("word count: " + pdf.getWordCount()+"\n");
             listener.getLogger().println("date created: " + pdf.getFileMonth()+"/"+pdf.getFileDay()+
                    "/"+pdf.getFileYear()+" " +pdf.getFileHour()+":"+pdf.getFileMinute()+":"
                    +pdf.getFileSecond()+"\n");
             listener.getLogger().println("\n\n-------------------");
            pdf.createJSON();
        }
         listener.getLogger().println("\n\nPowerpoint files \n\n---------------");
        for(PptxFile pptx: createobj.getListOfPptxObjects()){
             listener.getLogger().println("name of file: "+pptx.getFileName()+"\n");
             listener.getLogger().println("name of author: "+ pptx.getAuthor()+"\n");
             listener.getLogger().println("Word count: "+ pptx.getWordCount()+"\n");
             listener.getLogger().println("file size: " + pptx.getFileSize()+"\n");
             listener.getLogger().println("Page count: "+ pptx.getNumberOfSlides()+"\n");
             listener.getLogger().println("date created: " + pptx.getCreationDate());
             listener.getLogger().println("\n\n-------------------");
            pptx.createJSON();
        }

    }
}


