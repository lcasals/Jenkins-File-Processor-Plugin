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

    public static void setDIRECTORY(String dir)
    {
        directory = dir;
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
                    switch (extension) {
                        case "docx":
                            docxNames.add(f.getName());
                            break;
                        case "pptx":
                            pptxNames.add(f.getName());
                            break;
                        case "pdf":
                            pdfNames.add(f.getName());
                            break;
                        case "xlsx":
                            excelNames.add(f.getName());
                            break;
                        default:
                            unknownNames.add(f.getName());
                            break;
                    }
                }
            }
        } catch (NullPointerException e) {
            System.out.println("File not found: " + e);
        }
    }
}
