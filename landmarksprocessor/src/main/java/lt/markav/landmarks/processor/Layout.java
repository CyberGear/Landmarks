package lt.markav.landmarks.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Layout implements Logging{

    private final String name;
    private List<File> xmls = new ArrayList<>();
    private List<View> views = new ArrayList<>();

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
        String string = views.stream()
                .map(view -> "\n\t\t" + view.toString())
                .reduce((str1, str2) -> str1 + str2)
                .orElseGet(() -> "");
        return name + "{" + string + " }";
    }

    public List<View> getViews() {
        return views;
    }

    public void setViews(List<View> views) {
        this.views = views;
    }

    public void addViews(List<View> views) {
        this.views.addAll(views.stream().filter(View::isUseful).collect(Collectors.toList()));
    }

    public void prepare() {
        this.views = views.stream().distinct().map(View::prepare).collect(Collectors.toList());
    }

}
