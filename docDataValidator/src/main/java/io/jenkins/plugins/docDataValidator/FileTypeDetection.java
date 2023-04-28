package io.jenkins.plugins.docDataValidator;

import hudson.model.TaskListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileTypeDetection {
    //Path to input
    public FileTypeDetection(String DIRECTORY){directory = DIRECTORY;}
    private static String directory;
    public static String getDirectory()
    {
        return directory;
    }

    //getting file counts --> convert counts to hashmaps "name / count" easier to access and fewer commands.
    private static HashMap<String, Integer> fileCount = new HashMap<>();

    private static int totalCount = 0;

    public static void updateTotalCount(){
        totalCount = totalCount + 1;
    }

    //data structures to store names of files in dependent on file type
    private static ArrayList<String> pptxNames = new ArrayList<>();
    private static ArrayList<String> docxNames= new ArrayList<>();
    private static ArrayList<String> pdfNames = new ArrayList<>();
    private ArrayList<String> excelNames = new ArrayList<>();

    //if data structure for a specific file type is not created and added to the file organization then it will be added here
    private static ArrayList<String> unknownNames = new ArrayList<>();

    public ArrayList<String> getPDFNames(){
        return pdfNames;
    }
    public ArrayList<String> getDOCXNames(){
        return docxNames;
    }
    public ArrayList<String> getPPTXNames(){
        return pptxNames;
    }
    public ArrayList<String> getEXCELNames(){
        return this.excelNames;
    }
    public ArrayList<String> getUNKNOWNNames(){
        return unknownNames;
    }
    public void setFileNames(TaskListener listener) {
        listener.getLogger().println("\n\t Traversing files in directory: " + directory + "\n");
        listener.getLogger().println("Beginning to traverse the files");
        //Begins traversing file directory
        try {
            for (File f : new File(directory).listFiles()) {

                updateTotalCount();
                String fileName = f.toString();
                listener.getLogger().println("File name: " + f.getName());

                //takes extension of a file
                int index = fileName.lastIndexOf('.');
                if (index > 0) {

                    String extension = fileName.substring(index + 1);

                    if (!fileCount.containsKey(extension)) {
                        fileCount.put(extension, 1);

                    } else {

                        fileCount.put(extension, fileCount.get(extension) + 1);

                    }
                    //storing doc names into appropriate data structures
                    if (extension.equals("docx")) {
                        docxNames.add(f.getName());
                    } else if (extension.equals("pptx")) {
                        pptxNames.add(f.getName());
                    } else if (extension.equals("pdf")) {
                        pdfNames.add(f.getName());
                    }else if (extension.equals("xlsx")) {
                        excelNames.add(f.getName());
                    } else {
                        unknownNames.add(f.getName());
                    }
                }
            }
            listener.getLogger().println("printing the values of DocxNames: " + docxNames);
            listener.getLogger().println("printing the values of PDFNames: " + pdfNames);
            listener.getLogger().println("printing the values of excelNames: " + excelNames);
            listener.getLogger().println("printing the values of pptxNames: " + pptxNames);
        } catch (NullPointerException e) {
            System.out.println("File not found: " + e);
        }
    }
    /**
     * Prints the data retrieved from a document and placed into an object to the console
     */
    public void printFileNames(TaskListener listener){
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in docx\n" +
                        "---------------------------------------------");

        for (String s : docxNames) {
            listener.getLogger().println("|\t"+ s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in pptx\n" +
                        "---------------------------------------------");

        for (String s : pptxNames) {
            listener.getLogger().println("|\t"+ s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in pdf\n" +
                        "---------------------------------------------");

        for (String s : pdfNames) {
            listener.getLogger().println("|\t"+ s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in excel\n" +
                        "---------------------------------------------");

        for (String s : this.excelNames) {
            listener.getLogger().println("|\t"+ s + "\t\t\t|\n");
        }

        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tOther files\n" +
                        "---------------------------------------------");

        for (String s : unknownNames) {
            listener.getLogger().println("|\t"+ s + "\t\t\t|\n");
        }

    }
}
