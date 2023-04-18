# Jenkins-Document-Data-Validator
Document Data Validator is a Jenkins plugin that extracts metadata from PDF, Word, and PPTX files, validates the content, and saves metadata into JSON files. It detects and lists files in a directory, prints out file information, and report errors such as broken links to the console. 
## Features:  
- Detects and lists files in a directory
- Extracts metadata from PDF, Word, and PPTX files
- Validates content for broken links
- Saves metadata and errors into json files. 



## Usage: 
### Importing the plugin into Jenkins: 
Use jenkins dashboard web UI: 
	1. Go to jenkins dashboard
	2. After logging in, click on Manage Jenkins, then Manage Plugins
	3. Click on Advanced Settings
	4. In the Deploy Plugin section, click browse to choose a file and select the .hpi file 
	5. Click Upload
May need to restart the Jenkins instance to complete installation

### To use the Document Data Validator plugin in a declarative Jenkins pipeline, add the following step to your `Jenkinsfile`:
### Windows Systems
```
pipeline {
    agent any

    stages {
        stage('Validation') {
            steps {
                script {
                    validateDocuments(directory: 'C:\\path\\to\\inputFolder\\')
                }
            }
        }
    }
}

```
Replace "C:\\path\\to\\inputFolder\\" with the path to the directory containing the documents you want to validate.
make sure to include the backslash at the end
### Unix Systems
```
pipeline {
    agent any

    stages {
        stage('Validation') {
            steps {
                script {
                    validateDocuments(directory: '/path/to/inputFolder/')
                }
            }
        }
    }
}

```
Replace "/path/to/inputFolder/" with the path to the directory containing the documents you want to validate. 
Make sure to include the forward slash at the end.

## Default Directories: 
### Default Input Directory: 
The plugin scans for documents in the root of the Jenkins workspace. If you want to specify a different directory, you can provide the `directory` parameter in the `validateDocuments` step as shown above under "Usage"
- Note: when you use a pipeline script and check out files from a GitHub repository, Jenkins creates a workspace folder for the job run. The workspace folder will contain the files and directories from the checked-out repository. The files should be available in the workspace folder when the plugin is called.

### Default JSON Output Directory: 
The plugin saves metadata and error information in JSON files, which are stored in a directory named jsonOutput inside the Jenkins workspace. If you want to specify a different output directory, you can provide the outputDirectory parameter in the validateDocuments step as shown above under "Usage"
## Using the Default Input and Output Directories: 
By not providing the directory and outputDirectory parameters, the plugin will use the default input directory (the root of the Jenkins workspace) to scan for documents and the default output directory (jsonOutput inside the Jenkins workspace) to save the JSON files with metadata.

### Using Default Directories in a Jenkins Pipeline
To use the default input and output directories in your Jenkins pipeline, you don't need to provide the `directory` and `outputDirectory` parameters in the `validateDocuments` step. Here's an example of how to use the default directories in a Jenkinsfile:

```groovy
pipeline {
    agent any

    stages {
        stage('Validation') {
            steps {
                script {
                    validateDocuments()
                }
            }
        }
    }
}
```
## how to test Locally:
1. install maven
2. Open project folder in command line
3. run the following command:   mvn hpi:run
4. jenkins dashboard should be available at http://localhost:8080/jenkins/ , 
5. if port is being used, you can specify the port like this:    mvn hpi:run -Dport=8081  
