/*DocDataValidator Action  */

package io.jenkins.plugins.docDataValidator;
import hudson.model.Run;
import jenkins.model.RunAction2;
public class DocDataValidationAction implements RunAction2 {
    private transient Run run;

    @Override
    public void onAttached(Run<?, ?> run) {
        this.run = run;
    }

    @Override
    public void onLoad(Run<?, ?> run) {
        this.run = run;
    }

    public Run getRun() {
        return run;
    }
    //With these changes, the action Display Name will show in the build’s side panel, and link to the URL \http://JENKINS/job/JOBNAME/BUILDNUMBER/greeting/.
    //the page at that url needs to be defined with apache commons Jelly
    @Override
    public String getIconFileName() {
        //This is the icon used for the side panel item. document.png is one of the predefined icons bundled with Jenkins.
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        //This is the label used for the side panel item.
        return "Files";
    }

    @Override
    public String getUrlName() {
        //This is the URL fragment used for this action.
        return "files";
    }



    private String directory;
    private String outputDirectory;
    private boolean enableUrlCheck;
    //constructor
    public DocDataValidationAction(String directory, String outputDirectory, boolean enableUrlCheck) {
        this.directory = directory;
        this.outputDirectory = outputDirectory;
        this.enableUrlCheck = enableUrlCheck;
    }
    //getters
    public String getDirectory() {
        return directory;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getUrlValidationStatus() {
        return enableUrlCheck ? "Enabled" : "Disabled";
    }


}