package com.example.qrhunter;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class SeenHelper {
    private DocumentReference createdBy;
    private String image;
    private DocumentReference location;
    private List<DocumentReference> scanners;
    private List<DocumentReference> scans;
    private Double score;
    private Double timeCreated;

    public SeenHelper() {}

    public SeenHelper(DocumentReference createdBy, String image, DocumentReference location, List<DocumentReference> scanners, List<DocumentReference> scans, Double score, Double timeCreated) {
        this.createdBy = createdBy;
        this.image = image;
        this.location = location;
        this.scanners = scanners;
        this.scans = scans;
        this.score = score;
        this.timeCreated = timeCreated;
    }

    public DocumentReference getCreatedBy() {
        return createdBy;
    }

    public String getImage() {
        return image;
    }

    public DocumentReference getLocation() {
        return location;
    }

    public List<DocumentReference> getScanners() {
        return scanners;
    }

    public List<DocumentReference> getScans() {
        return scans;
    }

    public Double getScore() {
        return score;
    }

    public Double getTimeCreated() {
        return timeCreated;
    }
}
