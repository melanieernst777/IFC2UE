# Downloading and running the backend

## Downloading the repository
1. Go to GitHub and copy the URL
2. Open an IDE, for example Intellij
3. Click on New -> Project from Version Control
4. Add the URL and select the folder you want to import the project

## Running the backend with Docker
- Navigate to the OntologyToMeshConverter folder
- Run `docker build -t ifc2ueimage`
- Run `docker run -p 8080:8080 ifc2ueimage`

## When modifying the code... 
Here is a step-by-step instruction of how to run the backend
5. Right-click on the `pom.xml` file and select `Convert to maven project`
6. Go to `OntologyToMeshConvert.java` and run the application