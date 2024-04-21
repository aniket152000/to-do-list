const projectForm = document.getElementById('projectForm');
const projectNameInput = document.getElementById('projectName');
const projectList = document.getElementById('projectList');

// Function to fetch all projects from the API
async function fetchProjects() {
    const response = await fetch('http://localhost:8080/projects');
    const projects = await response.json();
    return projects;
}

async function createProject(projectName) {
    try {
        const response = await fetch('http://localhost:8080/projects', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ title: projectName })
        });

        if (response.ok) {
            const newProject = await response.json();
            alert('Project created successfully!');
            return newProject;
        } else {
            throw new Error('Failed to create project');
        }
    } catch (error) {
        console.error('Error creating project:', error);
        alert('Unable to create project. Please try again.');
        throw error; // Re-throw the error to handle it further if needed
    }
}


// Function to display projects in the UI
async function displayProjects() {
    projectList.innerHTML = ''; // Clear existing list
    const projects = await fetchProjects();
    projects.forEach(project => {
        const listItem = document.createElement('li');
        listItem.innerHTML = `
            <span>${project.title}</span>
            <button class="viewBtn" data-project-id="${project.projectId}">View</button>
            <button class="deleteBtn" data-project-id="${project.projectId}">Delete</button>
        `;
        projectList.appendChild(listItem);
    });
}

projectForm.addEventListener('submit', async function(event) {
    event.preventDefault();
    const projectName = projectNameInput.value.trim();
    if (projectName !== '') {
        await createProject(projectName);
        await displayProjects();
        projectNameInput.value = '';
    }
});

projectList.addEventListener('click', async function(event) {
    if (event.target.classList.contains('deleteBtn')) {
        const projectId = event.target.getAttribute('data-project-id');
        // Make delete request to API using projectId
        // Update UI by removing the deleted project
    } else if (event.target.classList.contains('viewBtn')) {
        const projectId = event.target.getAttribute('data-project-id');
        // Redirect to todolist.html with projectId as a query parameter
        window.location.href = `todolist.html?projectId=${projectId}`;
    }
});

// Initial display of projects when the page loads
displayProjects();


function getUrlParameter(name) {

    name = name.replace(/[\[\]]/g, '\\$&');

    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');

    const results = regex.exec(window.location.href);

    if (!results) return null;

    if (!results[2]) return '';

    return decodeURIComponent(results[2].replace(/\+/g, ' '));

}


// Update project title based on query parameter

const projectName = getUrlParameter('project');

if (projectName) {
    document.getElementById('projectTitle').textContent = projectName;
}

projectList.addEventListener('click', async function(event) {
    if (event.target.classList.contains('deleteBtn')) {
        const projectId = event.target.getAttribute('data-project-id');
        // Make delete request to API using projectId
        const deleteResponse = await fetch(`http://localhost:8080/projects/${projectId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
        });

        if (deleteResponse.ok) {
            // Remove the deleted project from the UI
            event.target.parentElement.remove(); // Remove the list item containing the delete button
            alert('Project deleted successfully!');
        } else {
            alert('Failed to delete project. Please try again.');
        }
    } else if (event.target.classList.contains('viewBtn')) {
        const projectId = event.target.getAttribute('data-project-id');
        window.location.href = `todolist.html?projectId=${projectId}`;
    }
});
