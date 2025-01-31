package com.viniciusSo.portifolio.models;

import lombok.Data;
import java.util.List;

@Data
public class ProjectResponse {
    private String id;
    private String title;
    private String description;
    private String url;
    private List<String> history;
    private byte[] thumbnail;
    private List<byte[]> photos;

    public ProjectResponse(Project project, byte[] thumbnail, List<byte[]> photos) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.url = project.getUrl();
        this.history = project.getHistory();
        this.thumbnail = thumbnail;
        this.photos = photos;
    }
}
