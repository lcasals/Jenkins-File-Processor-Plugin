package io.jenkins.plugins.docDataValidator;

import com.ibm.icu.impl.InvalidFormatException;
import hudson.model.TaskListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileTypeDetection {
    //Path to input
    public FileTypeDetection(String DIRECTORY){
        this.directory = DIRECTORY;
    }
    private static String directory;
    public static String getDirectory()
    {
        return directory;
    }

    public static void setDIRECTORY(String dir)
    {
        directory = dir;
    }

    //getting file counts --> convert counts to hashmaps "name / count" easier to access and less commands.

    private static HashMap<String, Integer> fileCount = new HashMap<>();

    private static int totalCount = 0;

    public static int updateTotalCount(){
        return totalCount = totalCount+1;
    }
    public static int getTotalCount(){
        return totalCount;
    }
    //data structures to store names of files in dependent on file type
    private static ArrayList<String> pptxNames = new ArrayList<>();
    private static ArrayList<String> docxNames= new ArrayList<>();
    private static ArrayList<String> pdfNames = new ArrayList<>();

    //if data structure for a specific file type is not created and added to the file organization then it will be added here
    private static ArrayList<String> unknownNames = new ArrayList<>();

    public static ArrayList<ArrayList<String>>  fileArrayOfArrays= new ArrayList<>();

    public static void addArrayToArrayOfArrays(ArrayList<String> fileArray){
        fileArrayOfArrays.add(fileArray);
    }
    public static ArrayList<ArrayList<String>> getfileArrayOfArrays(){
        return fileArrayOfArrays;
    }

    public static void setPDFNames() {

    }
    public static void setDOCXNames(){

    }
    public static void setPPTXNames(){

    }
    public ArrayList<String> getPDFNames(){
        return this.pdfNames;
    }
    public ArrayList<String> getDOCXNames(){
        return this.docxNames;
    }
    public ArrayList<String> getPPTXNames(){
        return this.pptxNames;
    }
    public static void main(String[] args, String outputDirectory, TaskListener listener) throws IOException, InvalidFormatException {
        setDIRECTORY(args[0]);
        listener.getLogger().println("Working Directory = " + System.getProperty("user.dir"));
        listener.getLogger().println("\n\t Traversing files in directory: " + directory + "\n");

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
                    } else {
                        unknownNames.add(f.getName());
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("File not found: " + e);
        }
    }

    public void printFileNames(TaskListener listener) {
        //printing content of each array
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in docx\n" +
                        "---------------------------------------------");

        for (String s : docxNames) {
            listener.getLogger().println("|\t" + s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in pptx\n" +
                        "---------------------------------------------");

        for (String s : pptxNames) {
            listener.getLogger().println("|\t" + s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tfiles in pdf\n" +
                        "---------------------------------------------");

        for (String s : pdfNames) {
            listener.getLogger().println("|\t" + s + "\t\t\t|\n");
        }
        listener.getLogger().println(
                "---------------------------------------------\n" +
                        "\t\tOther files\n" +
                        "---------------------------------------------");

        for (String s : unknownNames) {
            listener.getLogger().println("|\t" + s + "\t\t\t|\n");
        }
    }


}
