package com.example.qrhunter;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;
// a helper to manage data
public class SeenHelper {
    private DocumentReference createdBy;
    private String image;
    private DocumentReference location;
    private List<DocumentReference> scanners;
    private List<DocumentReference> scans;
    private Double score;
    private Double timeCreated;

    /**
     * get who created this code
     * @return created by reference
     */
    public DocumentReference getCreatedBy() {
        return createdBy;
    }

    /**
     * get who location this code
     * @return location reference
     */
    public DocumentReference getLocation() {
        return location;
    }

    /**
     * get scanners
     * @return scanners reference
     */
    public List<DocumentReference> getScanners() {
        return scanners;
    }

    /**
     * get scans
     * @return scans reference
     */
    public List<DocumentReference> getScans() {
        return scans;
    }

    /**
     * get score
     * @return score
     */
    public Double getScore() {
        return score;
    }

}
