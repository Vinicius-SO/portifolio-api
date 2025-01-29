package com.viniciusSo.portifolio.repository;

import com.viniciusSo.portifolio.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
