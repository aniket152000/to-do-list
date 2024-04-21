package com.example.hatio.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hatio.models.Project;
import com.example.hatio.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // Retrieves all projects
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        log.info("Fetching all projects");
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // Retrieves a project by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        log.info("Fetching project with ID: {}", id);
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Creates a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        log.info("Creating a new project");
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    // Updates the title of a project by its ID
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProjectTitle(@PathVariable Long id, @RequestBody Project projectDetails) {
        log.info("Updating project with ID: {}", id);
        Optional<Project> optionalProject = projectService.updateProjectTitle(id, projectDetails);
        return optionalProject.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Deletes a project by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        log.info("Deleting project with ID: {}", id);
        boolean deleted = projectService.deleteProjectById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}