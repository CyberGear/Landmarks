package lt.markav.landmarks.processor.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lt.markav.landmarks.processor.Logging;
import lt.markav.landmarks.processor.parser.tag.Tag;

public class LayoutsParser implements Logging {

    private final File root;
    private final DocumentBuilder documentBuilder;

    public LayoutsParser(ProcessingEnvironment processingEnv) {
        try {
            root = getRootDir(processingEnv);
            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e) {
            throw new IllegalStateException("Failing to find module for layouts discovery", e);
        }
    }

    private File getRootDir(ProcessingEnvironment processingEnv) throws IOException {
        JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile("dummy");
        File file = new File(sourceFile.toUri());
        sourceFile.openWriter().close();

        while (file.getParent() != null) {
            file = file.getParentFile();
            if (Arrays.asList(file.list()).contains("build.gradle")) {
                return file;
            }
        }
        throw new IllegalStateException("root dir not found: no parent has 'build.gradle'");
    }

    public List<Layout> parseAllLayouts() {
        List<Layout> layouts = findLayouts();
        parseLayouts(layouts);
        prepareLayouts(layouts);
        return layouts;
    }

    private void prepareLayouts(List<Layout> layouts) {
        layouts.stream().forEach(Layout::prepare);
        layouts.forEach(layout -> {
//            List<Vieww> includes = layout.getTags().stream()
//                    .filter(view -> view.getType().equals("include"))
//                    .collect(Collectors.toList());
//            if (includes.isEmpty()) {
//                return;
//            }
//
//            List<Vieww> inheritedViews = includes.stream().flatMap(include ->
//                    layouts.stream()
//                            .filter(layout1 -> layout1.getName().equals(include.getLayout()))
//                            .flatMap(l -> l.getViews().stream())
//            ).collect(Collectors.toList());
//
//            layout.getViews().removeAll(includes);
//            layout.getViews().addAll(inheritedViews);
        });
    }

    private List<Layout> findLayouts() {
        List<File> layoutFolders = findLayoutFolders(root);
        return resolveLayouts(layoutFolders);
    }

    private List<File> findLayoutFolders(File rootFile) {
        List<File> layoutFolders = new ArrayList<>();
        List<File> searchFolders = new ArrayList<>();
        searchFolders.add(rootFile);

        for (int i = 0; i < searchFolders.size(); i++) {
            File file = searchFolders.get(i);
            if (file.isDirectory()) {
                if (file.getName().startsWith("layout")) {
                    layoutFolders.add(file);
                } else if (!file.getName().startsWith("build")) {
                    searchFolders.addAll(Arrays.asList(file.listFiles()));
                }
            }
        }

        return layoutFolders;
    }

    private List<Layout> resolveLayouts(List<File> layoutFolders) {
        Map<String, Layout> layouts = new HashMap<>();

        layoutFolders.stream()
                .flatMap(folder -> Stream.of(folder.listFiles()))
                .forEach(xml -> {
                    if (!layouts.containsKey(xml.getName())) {
                        layouts.put(xml.getName(), new Layout(xml.getName()));
                    }
                    layouts.get(xml.getName()).addXml(xml);
                });

        return new ArrayList<>(layouts.values());
    }

    private void parseLayouts(List<Layout> layouts) {
        layouts.forEach(layout ->
                layout.getXmls().forEach(xml ->
                        layout.addViews(parseLayout(xml))));
    }

    private List<Tag> parseLayout(File xml) {
        try {
            List<Element> elementsToSearch = new ArrayList<>();
            elementsToSearch.add(documentBuilder.parse(xml).getDocumentElement());
            List<Tag> tags = new ArrayList<>();
            for (int i = 0; i < elementsToSearch.size(); i++) {
                Element element = elementsToSearch.get(i);
                tags.add(Tag.parse(element));

                if (element.hasChildNodes()) {
                    NodeList childNodes = element.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node item = childNodes.item(j);
                        if (item.getNodeType() == Node.ELEMENT_NODE) {
                            elementsToSearch.add((Element) item);
                        }
                    }
                }
            }

            return tags;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }
}
