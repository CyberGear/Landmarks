package lt.markav.legendsoflayouts.processor.parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lt.markav.legendsoflayouts.processor.util.Logging;
import lt.markav.legendsoflayouts.processor.parser.tag.IncludeTag;
import lt.markav.legendsoflayouts.processor.parser.tag.Tag;

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

    public List<Layout> parseLayouts() {
        Map<String, Layout> layouts = findLayouts();
        parseLayouts(layouts);
        resolveIncludes(layouts);
        return new ArrayList<>(layouts.values());
    }

    private void resolveIncludes(Map<String, Layout> layouts) {
        layouts.values().stream().forEach(layout -> {
            List<IncludeTag> includes = filterIncludes(layout);

            Set<Tag> inheritedTags = includes.stream().flatMap(include ->
                    layouts.get(include.getLayoutName()).getTags().stream())
                    .collect(Collectors.toSet());

            layout.getTags().removeAll(includes);
            layout.getTags().addAll(inheritedTags);
        });
    }

    private List<IncludeTag> filterIncludes(Layout layout) {
        return layout.getTags().stream()
                .filter(tag -> tag instanceof IncludeTag)
                .map(tag -> (IncludeTag) tag)
                .collect(Collectors.toList());
    }

    private Map<String, Layout> findLayouts() {
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

    private Map<String, Layout> resolveLayouts(List<File> layoutFolders) {
        Map<String, Layout> layouts = new HashMap<>();

        layoutFolders.stream()
                .flatMap(folder -> Stream.of(folder.listFiles()))
                .forEach(xml -> {
                    if (!layouts.containsKey(xml.getName())) {
                        layouts.put(xml.getName(), new Layout(xml.getName()));
                    }
                    layouts.get(xml.getName()).addXml(xml);
                });

        return layouts;
    }

    private void parseLayouts(Map<String, Layout> layouts) {
        layouts.values().forEach(layout -> {
            layout.getXmls().forEach(xml -> {
                layout.addTags(parseLayout(xml));
            });
        });
    }

    private Set<Tag> parseLayout(File xml) {
        try {
            List<Element> elementsToSearch = new ArrayList<>();
            elementsToSearch.add(documentBuilder.parse(xml).getDocumentElement());
            Set<Tag> tags = new HashSet<>();

            for (int i = 0; i < elementsToSearch.size(); i++) {
                Element element = elementsToSearch.get(i);
                tags.add(Tag.parse(element));

                elementsToSearch.addAll(getSubElements(element));
            }

            return tags;
        } catch (Exception e) {
            log(e.getMessage());
            return Collections.EMPTY_SET;
        }
    }

    private List<Element> getSubElements(Element element) {
        List<Element> subElements = new ArrayList<>();
        if (element.hasChildNodes()) {
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node item = childNodes.item(j);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    subElements.add((Element) item);
                }
            }
        }
        return subElements;
    }
}
