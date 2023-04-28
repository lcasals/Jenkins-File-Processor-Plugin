package io.jenkins.plugins.docDataValidator;
import com.google.gson.Gson;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.opc.PackageProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class excelFile {

    private String name;

    /**
     * Constructor
     * @param name name of the file
     * @param Directory directory of the file
     * @throws IOException throws exception if file is not found
     */
    public excelFile(String name, String Directory, String outputDirectory) throws IOException{
        this.name = name;
        this.outputDirectory = outputDirectory;
        FileInputStream fis = new FileInputStream((Directory+name));
        try{

            Workbook workbook = WorkbookFactory.create(fis);
            getFileInformation(Directory, workbook);
            findUrl(workbook);

        }catch(EmptyFileException e){
            System.out.println("File not applicable");
            return;
        }
        this.createJSON();
    }

    private void getFileInformation(String filePath, Workbook workbook) throws IOException {
        Path path = new File(filePath).toPath();
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);


        setFileName(this.name);

        try {
            PackageProperties properties = ((XSSFWorkbook) workbook).getPackage().getPackageProperties();
            setAuthor(properties.getCreatorProperty().orElse("Unknown"));
        } catch (InvalidFormatException e) {
            setAuthor("Unknown");
            e.printStackTrace();
        }

        int wordCount = 0;
        int rowCount = 0;

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            for (Row row : sheet) {
                wordCount += row.getPhysicalNumberOfCells();
            }
            rowCount += sheet.getPhysicalNumberOfRows();
        }

        setWordCount(wordCount);
        setRowCount(rowCount);
        setFileSize(attrs.size());
        setCreationTime(attrs.creationTime().toString());
    }

    private String fileName;
    private String author;
    private int wordCount;
    private long fileSize;
    private int rowCount;
    private String creationTime;
    private String outputDirectory;
    private final ArrayList<String> locatedURLs = new ArrayList<>();

    // Getters
    public String getFileName() {
        return fileName;
    }

    public String getAuthor() {
        return author;
    }

    public int getWordCount() {
        return wordCount;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String getCreationTime() {
        return creationTime;
    }

    // Setters
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }
    public ArrayList<String> getLocatedURLs()
    {
        return this.locatedURLs;
    }
    private void findUrl(Workbook workbook){
        setFileName(this.name);
        try {
            PackageProperties properties = ((XSSFWorkbook) workbook).getPackage().getPackageProperties();
            setAuthor(properties.getCreatorProperty().orElse("Unknown"));
        } catch (InvalidFormatException e) {
            setAuthor("Unknown");
            e.printStackTrace();
        }

        String urlValue = " ";
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                urlValue = cell.toString();
                if(isValidUrl(urlValue))
                {
                    //System.out.println("The value of the cell hyperlink is " + urlValue);
                    locatedURLs.add(urlValue);
                }
            }
        }
    }
    //runs a regex against the text in the file and see if it matches
    boolean isValidUrl(String urlValue)
    {
        String regex = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";
        Pattern regexPattern = Pattern.compile(regex);
        if (urlValue == null)
        {
            return false;
        }
        Matcher acceptedUrl = regexPattern.matcher(urlValue);
        return acceptedUrl.matches();
    }
    public void createJSON() {
        String allData = "{'name': '" + getFileName() + "',\n 'author': '" + getAuthor() + "',\n 'page count': " + getRowCount() +
                ",\n 'file size': " + getFileSize() + ",\n 'word count': " + getWordCount() + ",\n 'created': '" + getCreationTime() +
                "',\n'URLs':'" + getLocatedURLs() + "'}";

        Gson gson = new Gson();

        // Convert the input string to a JSON object
        Object jsonObject = gson.fromJson(allData, Object.class);

        // Write the JSON object to a file
        try (FileWriter fileWriter = new FileWriter(outputDirectory + File.separator + getFileName() + ".json")) {
            gson.toJson(jsonObject, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}