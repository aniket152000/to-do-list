package com.example.hatio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.hatio.models.Project;
import com.example.hatio.repository.ProjectRepository;
import com.example.hatio.service.ProjectService;

@SpringBootTest(classes = HatioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Get All Projects Test")
    void testGetAllProjects() {
        List<Project> projects = new ArrayList<>();
        projects.add(createProject(1L, "Project 1"));
        projects.add(createProject(2L, "Project 2"));

        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(projects.size(), result.size());
        assertTrue(result.containsAll(projects));
    }

    @Test
    @DisplayName("Get Project By Existing ID Test")
    void testGetProjectById_ExistingId() {
        Long projectId = 1L;
        Project project = createProject(projectId, "Project 1");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(projectId);

        assertTrue(result.isPresent());
        assertEquals(project, result.get());
    }

    @Test
    @DisplayName("Get Project By ID By Non-existing ID Test")
    void testGetProjectById_NonExistingId() {
        Long projectId = 100L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectById(projectId);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Create Project Test")
    void testCreateProject() {
        Project projectToCreate = createProject(null, "New Project");
        Project createdProject = createProject(1L, "New Project");
        createdProject.setCreatedDate(new Date());

        when(projectRepository.save(projectToCreate)).thenReturn(createdProject);

        Project result = projectService.createProject(projectToCreate);

        assertNotNull(result.getProjectId());
        assertEquals(createdProject.getTitle(), result.getTitle());
        assertNotNull(result.getCreatedDate());
    }

    @Test
    @DisplayName("Update Project Title By Existing ID Test")
    void testUpdateProjectTitle_ExistingId() {
        Long projectId = 1L;
        Project existingProject = createProject(projectId, "Existing Project");
        Project updatedProjectDetails = createProject(projectId, "Updated Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(updatedProjectDetails);

        Optional<Project> result = projectService.updateProjectTitle(projectId, updatedProjectDetails);

        assertTrue(result.isPresent());
        assertEquals(updatedProjectDetails.getTitle(), result.get().getTitle());
    }

    @Test
    @DisplayName("Update Project Title By Non-existing ID Test")
    void testUpdateProjectTitle_NonExistingId() {
        Long projectId = 100L;
        Project updatedProjectDetails = createProject(projectId, "Updated Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.updateProjectTitle(projectId, updatedProjectDetails);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Delete Project By Existing ID Test")
    void testDeleteProjectById_ExistingId() {
        Long projectId = 1L;
        Project existingProject = createProject(projectId, "Existing Project");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        doNothing().when(projectRepository).deleteById(projectId);

        boolean result = projectService.deleteProjectById(projectId);

        assertTrue(result);
    }

    @Test
    @DisplayName("Delete Project By Non-existing ID Test")
    void testDeleteProjectById_NonExistingId() {
        Long projectId = 100L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        boolean result = projectService.deleteProjectById(projectId);

        assertFalse(result);
    }

    private Project createProject(Long projectId, String title) {
        Project project = new Project();
        project.setProjectId(projectId);
        project.setTitle(title);
        project.setCreatedDate(new Date()); 
        return project;
    }
}
