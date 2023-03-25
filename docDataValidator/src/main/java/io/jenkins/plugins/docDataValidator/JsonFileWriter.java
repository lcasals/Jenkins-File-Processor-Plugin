package io.jenkins.plugins.docDataValidator;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter {

    private static final String DEFAULT_OUTPUT_DIRECTORY = "documents/"; //constant for the default output folder where the JSON files will be stored

    public static void writeToJson(Object object, String fileName, String outputDirectory) throws IOException {
        // If output directory is not specified, use the default location
        if (outputDirectory == null || outputDirectory.isEmpty()) {
            outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
        }

        // Create output directory if it doesn't exist
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Gson gson = new Gson();

        // Converting the input object to a JSON string
        // the input object is the instance of pdfFile, docxFile, or pptxFile in which this function is being called
        // which contains the metadata that's been extracted from the file
        String jsonString = gson.toJson(object);

        // Write the JSON string to a file
        String outputFilePath = outputDirectory + fileName + ".json";
        try (FileWriter fileWriter = new FileWriter(outputFilePath)) {
            fileWriter.write(jsonString);
        }
    }

    public static void writeToJson(Object object, String fileName) throws IOException {
        writeToJson(object, fileName, null);
    }

}