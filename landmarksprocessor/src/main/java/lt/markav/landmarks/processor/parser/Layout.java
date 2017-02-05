package lt.markav.landmarks.processor.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lt.markav.landmarks.processor.Logging;
import lt.markav.landmarks.processor.parser.tag.Tag;

public class Layout implements Logging {

    private final String name;
    private List<File> xmls = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    public Layout(String name) {
        this.name = name;
    }

    public void addXml(File layoutXml) {
        xmls.add(layoutXml);
    }

    public List<File> getXmls() {
        return xmls;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        String string = tags.stream()
                .map(tag -> "\n\t\t" + tag.toString())
                .reduce((str1, str2) -> str1 + str2)
                .orElseGet(() -> "");
        return name + "{" + string + " }";
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setViews(List<Tag> tags) {
        this.tags = tags;
    }

    public void addViews(List<Tag> tags) {
        this.tags.addAll(tags.stream().filter(Tag::isValid).collect(Collectors.toList()));
    }

    public void prepare() {
//        this.tags = tags.stream().distinct().map(Tag::prepare).collect(Collectors.toList());
    }

}
