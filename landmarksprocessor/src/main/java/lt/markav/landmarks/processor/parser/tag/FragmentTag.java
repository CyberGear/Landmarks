package lt.markav.landmarks.processor.parser.tag;

import org.w3c.dom.Element;

import lt.markav.landmarks.processor.parser.tag.Tag;

public class FragmentTag implements Tag {

    private final String rawName;
    private final String rawId;
    private final String rawTag;

    public FragmentTag(Element element) {
        rawName = element.getAttribute("android:name");
        rawId = element.getAttribute("android:id");
        rawTag = element.getAttribute("android:tag");
    }

    @Override
    public void generateFiled() {

    }

    @Override
    public void generateInitialization() {

    }

    @Override
    public boolean isValid() {
        return !rawId.isEmpty() || !rawTag.isEmpty();
    }

    @Override
    public String toString() {
        return rawId + rawTag + " " + rawName;
    }
}
