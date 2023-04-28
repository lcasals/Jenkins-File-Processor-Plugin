package io.jenkins.plugins.docDataValidator;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import java.io.IOException;

public class DocDataValidatorBuilder extends Builder implements SimpleBuildStep {

    //Getters and setters for Parameters: name
    private String directory;
    private String outputDirectory;
    private boolean enableUrlCheck;
    private int urlFlag = 0;

    @DataBoundConstructor
    public DocDataValidatorBuilder(String directory) {
        this.directory = directory;
    }

    @DataBoundSetter
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
    public boolean isEnableUrlCheck() {
        return enableUrlCheck;
    }

    @DataBoundSetter
    public void setEnableUrlCheck(boolean enableUrlCheck) {
        this.enableUrlCheck = enableUrlCheck;
    }
    public String getDirectory() {
        return directory;
    }
    public String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public void perform(@NonNull Run<?, ?> run, @NonNull FilePath workspace, @NonNull EnvVars env,
                        @NonNull Launcher launcher, @NonNull TaskListener listener) throws InterruptedException, IOException {
        // Set the default value for directory (the input directory for files to be scanned) if not provided
        if (directory == null || directory.isEmpty()) {
            directory = workspace.getRemote(); // This sets the directory to the workspace root by default
        }

        // Set the default value for outputDirectory if not provided
        if (outputDirectory == null || outputDirectory.isEmpty()) {
            outputDirectory = workspace.getRemote() + "/jsonOutput";
        }

        //calling the HelloWorldAction and passing in the name. from "Extend the Plugin" docs:
        run.addAction(new DocDataValidationAction(directory, outputDirectory, enableUrlCheck));
        listener.getLogger().println("The directory selected is: " + directory);
        listener.getLogger().println("The output directory selected is: " + outputDirectory);

        //calling the main function in the FileTypeDetection.java file and passing the directory path from build step

        try {
            //try instantiating a new object and then call the main function
            if(enableUrlCheck)
            {
                urlFlag = 1;
                driver.main(directory, outputDirectory, listener, urlFlag);
            }
            else
            {
                driver.main(directory, outputDirectory, listener, urlFlag);
            }

        } catch (Exception e) {
            listener.getLogger().println("Error running Driver: " + e.getMessage());
        }
    }

    @Symbol("validateDocuments")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {


        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }
        //display name for the Build step. will show up when adding plugin as a build step in the dashboard
        @NonNull
        @Override
        public String getDisplayName() {
            return "Document Data Validator";
            //return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }
    }
}