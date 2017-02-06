package lt.markav.landmarks.processor.parser.tag;

import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

public class IncludeTag implements Tag {

    private final String rawLayout;

    public IncludeTag(Element element) {
        rawLayout = element.getAttribute("layout");
    }

    @Override
    public void declareField(TypeSpec.Builder classBuilder) {

    }

    @Override
    public void generateInitialization() {

    }

    public String getLayoutName() {
        return rawLayout.replace("@layout/", "") + ".xml";
    }

    @Override
    public boolean isValid() {
        return !rawLayout.isEmpty() && !rawLayout.startsWith("@android:layout/");
    }

    @Override
    public String toString() {
        return rawLayout;
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
