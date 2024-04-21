package com.example.hatio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hatio.models.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}