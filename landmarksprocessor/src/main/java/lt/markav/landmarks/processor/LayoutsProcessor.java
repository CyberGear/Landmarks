package lt.markav.landmarks.processor;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class LayoutsProcessor {

    private final File root;
    private final DocumentBuilder documentBuilder;
    private Map<String, Layout> layouts;

    LayoutsProcessor(ProcessingEnvironment processingEnv) {
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
        sourceFile.delete();

        while (file.getParent() != null) {
            file = file.getParentFile();
            if (Arrays.asList(file.list()).contains("build.gradle")) {
                return file;
            }
        }
        throw new IllegalStateException("root dir not found: no parent has 'build.gradle'");
    }

    List<Layout> collectDataForLandmarksGeneration() {
        resolveLayouts();
        collectIds();
        return new ArrayList<>(layouts.values());
    }

    private void resolveLayouts() {
        List<File> folders = new ArrayList<>();
        findLayoutFolders(root, folders);
        collectToLayouts(folders);
    }

    private void findLayoutFolders(File rootFile, List<File> files) {
        if (rootFile.isDirectory() && rootFile.getName().startsWith("layout")) {
            files.add(rootFile);
        } else if (rootFile.isDirectory() && !rootFile.getName().contains("build")) {
            for (File file : rootFile.listFiles()) {
                findLayoutFolders(file, files);
            }
        }
    }

    private void collectToLayouts(List<File> folders) {
        layouts = new HashMap<>();
        for (File folder : folders) {
            for (File layoutXml : folder.listFiles()) {
                Layout layout = getLayout(layouts, layoutXml.getName());
                layout.addXml(layoutXml);
            }
        }
    }

    private Layout getLayout(Map<String, Layout> layouts, String name) {
        if (!layouts.containsKey(name)) {
            layouts.put(name, new Layout(name.replace(".xml", "")));
        }
        return layouts.get(name);
    }

    private void collectIds() {
        for (Layout layout : layouts.values()) {
            try {
                findIsForLayout(layout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void findIsForLayout(Layout layout) throws IOException, SAXException {
        List<Element> allElements = new ArrayList<>();
        findAllElements(layout, allElements);
        removeRedundantElements(allElements);



        System.out.println(">> " + layout.getName());
        for (Element el : allElements) {
            System.out.println(">>     " + el.getNodeName()  + " "  + el.getAttribute("android:id"));
        }


//        layout.pl(">>> " + xml.getDocumentElement().getTagName());
//        NodeList childNodes = xml.getDocumentElement().getChildNodes();
//        for (int i = 0; i < childNodes.getLength(); i++){
//            if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
//                Element element = (Element) childNodes.item(i);
//                pl(">>>> " + element.getNodeName());
//            }
//        }
    }

    private void removeRedundantElements(List<Element> allElements) {
        for (int i = allElements.size() -1; i > -1; i--) {
            Element element = allElements.get(i);
            if (element.getAttribute("android:id").equals("") && !element.getNodeName().equals("include")) {
                allElements.remove(element);
            }
        }
    }

    private void findAllElements(Layout layout, List<Element> allElements) throws SAXException, IOException {
        for (File layoutXml : layout.getXmls()) {
            Element root = documentBuilder.parse(layoutXml).getDocumentElement();
            collectElements(root, allElements);
        }
    }

    private void collectElements(Element element, List<Element> allElements) {
        allElements.add(element);
        if (element.hasChildNodes()) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {
                    collectElements((Element)item, allElements);
                }
            }
        }
    }
}
