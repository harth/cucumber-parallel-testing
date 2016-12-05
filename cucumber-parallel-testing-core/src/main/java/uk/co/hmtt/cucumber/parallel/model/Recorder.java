package uk.co.hmtt.cucumber.parallel.model;

import java.util.HashSet;
import java.util.Set;

public class Recorder {

    private Set<String> features = new HashSet<>();

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }
}
