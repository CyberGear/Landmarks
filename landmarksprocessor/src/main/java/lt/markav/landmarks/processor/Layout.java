package lt.markav.landmarks.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Layout {

    private final String name;
    private List<File> xmls = new ArrayList<>();
    private List<String> rawIds = new ArrayList<>();
    private List<String> includes = new ArrayList<>();

    public Layout(String name) {
        this.name = name;
    }

    public void addXml(File layoutXml) {
        xmls.add(layoutXml);
    }

    @Override
    public String toString() {
        return name + xmls;
    }

    public List<File> getXmls() {
        return xmls;
    }

    public String getName() {
        return name;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getRawIds() {
        return rawIds;
    }
}
