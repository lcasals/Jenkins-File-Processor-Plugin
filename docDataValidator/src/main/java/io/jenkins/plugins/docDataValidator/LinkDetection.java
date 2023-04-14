package io.jenkins.plugins.docDataValidator;

import hudson.model.TaskListener;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LinkDetection {
    public static void main(ArrayList<String> locatedURLs, TaskListener listener) throws IOException {
        if(locatedURLs.size() != 0)
        {
            System.out.println("Validating Links..." + "\n");
            for(String urls: locatedURLs)
            {
                String codeStatus = " ";
                URL url = new URL(urls);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int code = connection.getResponseCode();

                if(code >= 100 && code < 200)
                {
                    codeStatus = "informational response";
                }
                if(code >= 200 && code < 300)
                {
                    codeStatus = "Successful Response";
                }
                if(code >= 300 && code < 400)
                {
                    codeStatus = "Redirection";
                }
                if(code >= 400 && code < 500)
                {
                    codeStatus = "Client Error";
                }
                if(code >= 500 && code < 600)
                {
                    codeStatus = "Server Error";
                }
                listener.getLogger().println(urls + " " + code + " " + codeStatus);
            }
        }
        else
        {
            listener.getLogger().println("No links we detected in the file");
        }

    }
}
