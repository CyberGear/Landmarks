package lt.markav.landmarks.processor.parser.tag;

import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

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
    public void declareField(TypeSpec.Builder classBuilder) {

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

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals("" + obj);
    }
}
