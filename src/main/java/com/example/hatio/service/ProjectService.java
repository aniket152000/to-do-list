package com.example.hatio.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hatio.models.Project;
import com.example.hatio.repository.ProjectRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    // Retrieves all projects
    public List<Project> getAllProjects() {
        log.info("Fetching all projects from the database");
        return projectRepository.findAll();
    }

    // Retrieves a project by its ID
    public Optional<Project> getProjectById(Long id) {
        log.info("Fetching project with ID {} from the database", id);
        return projectRepository.findById(id);
    }

    // Creates a new project
    public Project createProject(Project project) {
        log.info("Creating a new project");
        project.setCreatedDate(new Date());
        return projectRepository.save(project);
    }

    // Updates the title of a project by its ID
    public Optional<Project> updateProjectTitle(Long id, Project projectDetails) {
        log.info("Updating project with ID {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setTitle(projectDetails.getTitle());
            final Project updatedProject = projectRepository.save(project);
            return Optional.of(updatedProject);
        } else {
            log.warn("Project with ID {} not found for updating", id);
            return Optional.empty();
        }
    }

    // Deletes a project by its ID
    public boolean deleteProjectById(Long id) {
        log.info("Deleting project with ID {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            projectRepository.deleteById(id);
            log.info("Project with ID {} deleted successfully", id);
            return true;
        } else {
            log.warn("Project with ID {} not found for deletion", id);
            return false;
        }
    }
}