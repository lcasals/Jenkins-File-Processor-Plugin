package io.jenkins.plugins.fileProcessor;

import java.io.IOException;
import java.util.ArrayList;

public class driver {

    //change to accept an array of arrays and create a for loop to enter each to make it.
    public static void main(ArrayList<String> pdfArray, String dir) throws IOException {

        System.out.print("Running second program\n");
        FileObjectCreation create = new FileObjectCreation();

        create.createPdfObjects(pdfArray, dir);

        for(PdfFile pdf: create.getListOfPdfObjects()){

            System.out.println("name of file: " + pdf.getFileName()+"\n");
            System.out.println("name of author: " + pdf.getAuthor()+"\n");
            System.out.println("page count: " + pdf.getPageCount()+"\n");
            System.out.println("file size: " + (pdf.getFileSize()/1000) +"KB "
                    + pdf.getFileSize()+ " bytes" + "\n");
            System.out.println("word count: " + pdf.getWordCount()+"\n");
            System.out.println("date created: " + pdf.getFileMonth()+"/"+pdf.getFileDay()+
                    "/"+pdf.getFileYear()+" " +pdf.getFileHour()+":"+pdf.getFileMinute()+":"
                    +pdf.getFileSecond()+"\n");
        }
    }
}

