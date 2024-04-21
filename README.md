# to-do-list Project

Project Summary:

I developed a task management system utilizing Spring Boot as the backend framework and HTML, CSS, and JavaScript as the frontend technologies, with MS SQL serving as the database. This system enables users to create new projects, manage project tasks (add, edit, remove, mark as complete/pending), and export projects as Gists or download them as Markdown files. Additionally, users can modify project titles within the system.

how to setup this project :

1. Clone the Repository:

Clone the repository using Git with the following command:

git clone -b master https://github.com/aniket152000/to-do-list.git

2. Set up the Database:

Create a database in MS SQL Server.
Replace the placeholder "hatioDB" in the application.properties file with the actual database name.

3. Generate GitHub Personal Access Token:

Go to your GitHub account's settings.
Navigate to "Developer Settings" > "Personal Access Tokens" > "Tokens (classic)."
Generate a new personal access token with the required permissions.
Copy the generated access token.

4. Configure the Access Token:

Open the toDoList.js file in your project.
Replace the placeholder "GITHUB_ACCESS_TOKEN" with the actual GitHub personal access token obtained in the previous step.

5. Run the Spring Boot Application:

Open a terminal and navigate to the root directory of the project.
Run the following Maven command to start the Spring Boot application:

mvn spring-boot:run

6. Configure Launch Settings for Chrome:

Create a launch.json file for configuring the launch settings in Visual Studio Code (assuming you are using VS Code).
Add the following configuration to the launch.json file:

{
    "type": "chrome",
    "request": "launch",
    "name": "Launch Chrome against localhost",
    "url": "file:///{workingdir}/login.html"
}
Replace {workingdir} with the actual path to your project's working directory.

7. Run the Application:

In Visual Studio Code, select the "Run" menu and choose "Start Without Debugging."
This will launch Chrome and open the login page (login.html).

8. Login to the Application:

Use the following credentials to log in:
Username: admin
Password: password



