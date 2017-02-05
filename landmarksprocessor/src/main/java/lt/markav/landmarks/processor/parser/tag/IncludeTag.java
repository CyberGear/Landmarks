package lt.markav.landmarks.processor.parser.tag;

import org.w3c.dom.Element;

import lt.markav.landmarks.processor.parser.tag.Tag;

public class IncludeTag implements Tag {

    private final String rawLayout;

    public IncludeTag(Element element) {
        rawLayout = element.getAttribute("layout");
    }

    @Override
    public void generateFiled() {

    }

    @Override
    public void generateInitialization() {

    }

    @Override
    public boolean isValid() {
        return !rawLayout.isEmpty();
    }

    @Override
    public String toString() {
        return rawLayout;
    }
}
