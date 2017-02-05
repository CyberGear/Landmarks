package lt.markav.landmarks.processor.parser.tag;

import org.w3c.dom.Element;

public class ViewTag implements Tag {

    private String type;
    private String rawId;

    public ViewTag(Element element) {
        type = element.getTagName();
        rawId = element.getAttribute("android:id");
    }

    @Override
    public void generateFiled() {

    }

    @Override
    public void generateInitialization() {

    }

    @Override
    public boolean isValid() {
        return !rawId.isEmpty();
    }

    @Override
    public String toString() {
        return rawId + " " + type;
    }
}
