package lt.markav.legendsoflayouts.processor.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lt.markav.legendsoflayouts.processor.util.Logging;
import lt.markav.legendsoflayouts.processor.parser.tag.Tag;

public class Layout implements Logging {

    private final String name;
    private List<File> xmls = new ArrayList<>();
    private Set<Tag> tags = new HashSet<>();

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

    public Set<Tag> getTags() {
        return tags;
    }

    public void addTags(Set<Tag> tags) {
        Set<Tag> set = tags.stream().filter(Tag::isValid).collect(Collectors.toSet());
        this.tags.addAll(set);
    }

}
