package parallel.model;

import java.util.HashSet;
import java.util.Set;

public class RecorderWrapper {

    private Set<String> features = new HashSet<>();

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }
}
