package com.viniciusSo.portifolio.controller;

import com.viniciusSo.portifolio.models.Project;
import com.viniciusSo.portifolio.models.ProjectResponse;
import com.viniciusSo.portifolio.repository.ProjectRepository;
import com.viniciusSo.portifolio.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")  // Permite chamadas de qualquer origem (ajuste conforme necessário)
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    private FileService fileService;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("url") String url,
            @RequestParam("history") List<String> history,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestParam(value = "photos", required = false) List<MultipartFile> photos) {

        try {
            Project project = new Project();
            project.setTitle(title);
            project.setDescription(description);
            project.setUrl(url);
            project.setHistory(history);

            // Salvar thumbnail no GridFS
            if (thumbnail != null && !thumbnail.isEmpty()) {
                String thumbnailId = fileService.saveFile(thumbnail);
                project.setThumbnailId(thumbnailId);
            }

            // Salvar fotos extras no GridFS
            if (photos != null && !photos.isEmpty()) {
                List<String> photoIds = photos.stream()
                        .map(file -> {
                            try {
                                return fileService.saveFile(file);
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(id -> id != null)
                        .toList();
                project.setPhotosIds(photoIds);
            }

            Project savedProject = projectRepository.save(project);
            return ResponseEntity.ok(savedProject);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable String id) {
        return projectRepository.findById(id)
                .map(project -> {
                    // Buscar as imagens no GridFS
                    byte[] thumbnail = fileService.getFile(project.getThumbnailId());
                    List<byte[]> photos = project.getPhotosIds().stream()
                            .map(fileService::getFile)
                            .toList();

                    // Criar resposta personalizada com imagens
                    ProjectResponse response = new ProjectResponse(project, thumbnail, photos);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/image/{imageId}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageId) {
        InputStream imageStream = fileService.getFileStream(imageId);
        if (imageStream == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(imageStream));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileId = fileService.saveFile(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
            return ResponseEntity.ok(fileId); // Retorna o ID do arquivo salvo no MongoDB
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
}
