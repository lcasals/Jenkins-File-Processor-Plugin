package io.jenkins.plugins.docDataValidator;

import java.io.IOException;
import java.util.ArrayList;

import hudson.model.TaskListener;

public class driver {

    //change to accept an array of arrays and create a for loop to enter each to make it.
    public static void main(ArrayList<String> pdfArray, String dir, TaskListener listener) throws IOException {

        System.out.print("Running second program\n");
        FileObjectCreation create = new FileObjectCreation();

        create.createPdfObjects(pdfArray, dir);

        for(PdfFile pdf: create.getListOfPdfObjects()){

            listener.getLogger().println("name of file: " + pdf.getFileName()+"\n");
            listener.getLogger().println("name of author: " + pdf.getAuthor()+"\n");
            listener.getLogger().println("page count: " + pdf.getPageCount()+"\n");
            listener.getLogger().println("file size: " + (pdf.getFileSize()/1000) +"KB "
                    + pdf.getFileSize()+ " bytes" + "\n");
            listener.getLogger().println("word count: " + pdf.getWordCount()+"\n");
            listener.getLogger().println("date created: " + pdf.getFileMonth()+"/"+pdf.getFileDay()+
                    "/"+pdf.getFileYear()+" " +pdf.getFileHour()+":"+pdf.getFileMinute()+":"
                    +pdf.getFileSecond()+"\n");
        }
    }
}

