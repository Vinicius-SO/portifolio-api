package com.viniciusSo.portifolio.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "projects")  // Define que esta classe representa a coleção "projects"
public class Project {

    @Id
    private String id;
    private String title;
    private String description;
    private List<Image> thumbnail;
    private List<Image> photos;
    private List<String> history;
    private String url;

    // Classe interna para representar imagens
    public static class Image {
        private String url;
        private String alt;

        // Construtores, Getters e Setters
        public Image() {}

        public Image(String url, String alt) {
            this.url = url;
            this.alt = alt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }
    }

    // Construtores, Getters e Setters
    public Project() {}

    public Project(String title, String description, List<Image> thumbnail, List<Image> photos, List<String> history, String url) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.photos = photos;
        this.history = history;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Image> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(List<Image> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Image> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Image> photos) {
        this.photos = photos;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
