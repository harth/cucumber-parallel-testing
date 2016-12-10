package uk.co.hmtt.cucumber.parallel.model;

import java.util.HashSet;
import java.util.Set;

public class FeatureFileRecorder {

    private Set<String> featureFiles = new HashSet<>();

    public Set<String> getFeatureFiles() {
        return featureFiles;
    }

    public void setFeatureFiles(Set<String> featureFiles) {
        this.featureFiles = featureFiles;
    }
}
