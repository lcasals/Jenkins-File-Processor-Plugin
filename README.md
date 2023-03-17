# Jenkins-Document-Data-Validator
Document Data Validator is a Jenkins plugin that extracts metadata from PDF, Word, and text files, validates the content, and saves metadata into a JSON file. It detects and lists files in a directory, prints out file information, and reports errors such as spelling mistakes and broken links to the console. The plugin is designed to fail the Jenkins pipeline if any errors are detected

## Features:  
- Extracts metadata from PDF, Word, and text files
- Validates content for spelling errors and broken links
- Detects and lists files in a directory
- Saves metadata and errors in a JSON file
- Fails the Jenkins pipeline if any errors are detected

## Usage: 
To use the Document Data Validator plugin in your Jenkins pipeline, add the following step to your `Jenkinsfile`:

```
pipeline {
    agent any

    stages {
        stage('Validation') {
            steps {
                script {
                    validateDocuments(directory: 'path/to/your/files')
                }
            }
        }
    }
}

```
Replace 'C:\path\to\inputFolder\' with the path to the directory containing the documents you want to validate.
make sure to include the backslash at the end

## how to test Locally:
1. install maven
2. Open project folder in command line
3. run the following command:   mvn hpi:run
4. jenkins dashboard should be available at http://localhost:8080/jenkins/ , 
5. if port is being used, you can specify the port like this:    mvn hpi:run Dport=8081  
