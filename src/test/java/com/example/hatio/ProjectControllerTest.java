package com.example.hatio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.hatio.controller.ProjectController;
import com.example.hatio.models.Project;
import com.example.hatio.service.ProjectService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get All Projects Test")
    void testGetAllProjects() {
        List<Project> projects = Arrays.asList(createProject(1L, "Project 1"), createProject(2L, "Project 2"));
        when(projectService.getAllProjects()).thenReturn(projects);

        ResponseEntity<List<Project>> response = projectController.getAllProjects();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(projects, response.getBody());
    }

    @Test
    @DisplayName("Get Project By Existing ID Test")
    void testGetProjectById_ExistingId() {
        Long projectId = 1L;
        Project project = createProject(projectId, "Project 1");
        when(projectService.getProjectById(projectId)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(projectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    @DisplayName("Get Project By Non-existing ID Test")
    void testGetProjectById_NonExistingId() {
        Long projectId = 100L;
        when(projectService.getProjectById(projectId)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(projectId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Create Project Test")
    void testCreateProject() {
        Project projectToCreate = createProject(null, "New Project");
        Project createdProject = createProject(1L, "New Project");
        when(projectService.createProject(projectToCreate)).thenReturn(createdProject);

        ResponseEntity<Project> response = projectController.createProject(projectToCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdProject, response.getBody());
    }

    @Test
    @DisplayName("Update Project Title with Existing ID Test")
    void testUpdateProjectTitle_ExistingId() {
        Long projectId = 1L;
        Project existingProject = createProject(projectId, "Existing Project");
        Project updatedProjectDetails = createProject(projectId, "Updated Project");
        when(projectService.updateProjectTitle(projectId, updatedProjectDetails))
                .thenReturn(Optional.of(updatedProjectDetails));

        ResponseEntity<Project> response = projectController.updateProjectTitle(projectId, updatedProjectDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProjectDetails, response.getBody());
    }

    @Test
    @DisplayName("Update Project Title with Non-existing ID Test")
    void testUpdateProjectTitle_NonExistingId() {
        Long projectId = 100L;
        Project updatedProjectDetails = createProject(projectId, "Updated Project");
        when(projectService.updateProjectTitle(projectId, updatedProjectDetails)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.updateProjectTitle(projectId, updatedProjectDetails);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete Project By Existing ID Test")
    void testDeleteProjectById_ExistingId() {
        Long projectId = 1L;
        when(projectService.deleteProjectById(projectId)).thenReturn(true);

        ResponseEntity<?> response = projectController.deleteProjectById(projectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Delete Project By Non-existing ID Test")
    void testDeleteProjectById_NonExistingId() {
        Long projectId = 100L;
        when(projectService.deleteProjectById(projectId)).thenReturn(false);

        ResponseEntity<?> response = projectController.deleteProjectById(projectId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private Project createProject(Long projectId, String title) {
        Project project = new Project();
        project.setProjectId(projectId);
        project.setTitle(title);
        project.setCreatedDate(new Date());
        return project;
    }
}
