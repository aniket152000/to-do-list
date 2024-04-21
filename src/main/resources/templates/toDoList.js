let tasks = [];

function addTask(projectName) {
    const taskInput = document.getElementById('taskInput');
    const taskText = taskInput.value.trim();

    if (taskText !== '') {
        const task = {
            id: Date.now(),
            description: taskText,
            completed: false
        };

        tasks.push(task);
        renderTasks();
        taskInput.value = '';
    }
}


function renderTasks() {
    const pendingTodos = document.getElementById('pendingTodos');
    const completedTodos = document.getElementById('completedTodos');
    pendingTodos.innerHTML = '';
    completedTodos.innerHTML = '';

    let completedCount = 0;

    tasks.forEach(task => {
        const taskItem = document.createElement('li');
        taskItem.innerText = task.description;

        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.checked = task.completed;
        checkbox.addEventListener('change', () => toggleTaskStatus(task.id));

        taskItem.prepend(checkbox);

        const updateButton = document.createElement('button');
        updateButton.textContent = 'Update';
        updateButton.className = 'updateBtn';
        updateButton.addEventListener('click', () => updateTaskDescription(task.id));

        const removeButton = document.createElement('button');
        removeButton.textContent = 'Remove';
        removeButton.className = 'removeBtn';
        removeButton.addEventListener('click', () => removeTask(task.id));

        taskItem.appendChild(updateButton);
        taskItem.appendChild(removeButton);

        if (task.completed) {
            completedCount++;
            completedTodos.appendChild(taskItem);
        } else {
            pendingTodos.appendChild(taskItem);
        }
    });

    const summary = document.getElementById('summary');
    summary.innerText = `Summary: ${completedCount} / ${tasks.length} completed`;
}


renderTasks();

function toggleEditProjectTitle() {
    const projectTitle = document.getElementById('projectTitle');
    const editButton = document.getElementById('editProjectButton');
    const saveButton = document.getElementById('saveProjectButton');

    projectTitle.contentEditable = true; // Allow editing directly in the h1 element
    projectTitle.focus(); // Focus on the editable title
    editButton.style.display = 'none';
    saveButton.style.display = 'inline-block';
}

function saveProjectTitle() {
    const projectTitle = document.getElementById('projectTitle');
    const newTitle = projectTitle.textContent.trim();

    if (newTitle !== '') {
        fetch(`http://localhost:8080/projects/${projectId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ title: newTitle }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Project title updated:', data);
                projectTitle.contentEditable = false; // Disable editing after saving
                toggleEditProjectTitle(); // Hide edit mode after saving
                alert('Project title updated successfully!');
                showEditProjectButton();
                hideSaveProjectButton();
            })
            .catch(error => {
                console.error('Error updating project title:', error);
            });
    }
}

function hideSaveProjectButton() {
    const saveButton = document.getElementById('saveProjectButton');
    saveButton.style.display = 'none';
}

function showEditProjectButton() {
    const editButton = document.getElementById('editProjectButton');
    editButton.style.display = 'inline-block';
}


function updateProjectTitle(projectId, newTitle) {
    fetch('http://localhost:8080/api/updateProjectTitle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ projectId, newTitle }), // Send projectId and newTitle
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json(); // Parse JSON response
        })
        .then(data => {
            console.log(data); // Log the parsed JSON data
        })
        .catch(error => {
            console.error('There was a problem with your fetch operation:', error);
        });

}

// Function to fetch project details by ID from the backend API
async function fetchProjectById(projectId) {
    try {
        const response = await fetch(`http://localhost:8080/projects/${projectId}`);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const projectData = await response.json();
        return projectData;
    } catch (error) {
        console.error('Error fetching project details:', error);
        return null;
    }
}

// Function to update project title in the UI
async function updateProjectTitleInUI(projectId) {
    const projectData = await fetchProjectById(projectId);
    if (projectData) {
        const projectTitleElement = document.getElementById('projectTitle');
        projectTitleElement.textContent = projectData.title;
    }
}

// Initial rendering of tasks
renderTasks();

// Get the projectId from the query parameter
const projectId = getUrlParameter('projectId');
if (projectId) {
    // Update project title in the UI based on the projectId
    updateProjectTitleInUI(projectId);
}

// Function to get URL parameter by name
function getUrlParameter(name) {
    name = name.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(window.location.href);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}



function toggleTaskStatus(id) {
    tasks = tasks.map(task =>
        task.id === id ? { ...task, completed: !task.completed } : task
    );
    renderTasks();
}

function updateTaskDescription(id) {
    const newDescription = prompt('Enter the new description for this task:');
    if (newDescription && newDescription.trim() !== '') {
        tasks = tasks.map(task =>
            task.id === id ? { ...task, description: newDescription.trim() } : task
        );
        renderTasks();
    }
}

function removeTask(id) {
    tasks = tasks.filter(task => task.id !== id);
    renderTasks();
}

function updateProjectTitleFromUrlParameter() {
    // Function to get URL parameter by name
    function getUrlParameter(name) {
        name = name.replace(/[\[\]]/g, '\\$&');
        const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
        const results = regex.exec(window.location.href);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    const projectName = getUrlParameter('project');
    if (projectName) {
        document.getElementById('projectTitle').textContent = projectName;
    }
}

function generateAndDownloadMarkdown(exportType) {
    const projectTitle = document.getElementById('projectTitle').innerText;
    const pendingTasks = Array.from(document.querySelectorAll('#pendingTodos li')).map(task => task.textContent.trim().replace(/(Update|Remove)/g, ''));
    const completedTasks = Array.from(document.querySelectorAll('#completedTodos li')).map(task => task.textContent.trim().replace(/(Update|Remove)/g, ''));

    let markdownContent = `
# ${projectTitle}

Summary: ${completedTasks.length} / ${pendingTasks.length + completedTasks.length} completed.

## Pending Todos
${pendingTasks.map(task => `- [ ] ${task}`).join('\n')}

## Completed Todos
${completedTasks.map(task => `- [x] ${task}`).join('\n')}
`;

    if (exportType === 'gist') {
        const content = `
# ${projectTitle}

Summary: ${completedTasks.length} / ${pendingTasks.length + completedTasks.length} completed.

## Pending Todos
${pendingTasks.map(task => `- [ ] ${task}`).join('\n')}

## Completed Todos
${completedTasks.map(task => `- [x] ${task}`).join('\n')}
`;

        fetch('https://api.github.com/gists', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer GITHUB_ACCESS_TOKEN',
                'Content-Type': 'application/json',
                'Accept': 'application/vnd.github.v3+json'
            },
            body: JSON.stringify({
                description: 'To-Do List Summary',
                public: false,
                files: {
                    [`${projectTitle}.md`]: {
                        content
                    }
                }
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Error creating secret gist');
                }
                return response.json();
            })
            .then(data => {
                const gistUrl = data.html_url;
                console.log('Secret Gist created:', gistUrl);
                alert('Secret Gist created! URL: ' + gistUrl);
            })
            .catch(error => {
                console.error('Error creating secret gist:', error);
                alert('Error creating secret gist. Please check the console for details.');
            });
    } else if (exportType === 'download') {
        download(`${projectTitle}.md`, markdownContent);
    } else {
        alert('Please select an export type.');
    }
}

function download(filename, text) {
    const element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);

    element.style.display = 'none';
    document.body.appendChild(element);

    element.click();

    document.body.removeChild(element);
}
