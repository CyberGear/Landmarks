package lt.markav.landmarks.processor.parser.tag;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

public class ViewTag implements Tag {

    private String type;
    private String rawId;

    public ViewTag(Element element) {
        type = element.getTagName();
        rawId = element.getAttribute("android:id");
    }

    @Override
    public void declareField(TypeSpec.Builder classBuilder) {
        classBuilder.addField(getTypeName(), getArgName(), Modifier.PUBLIC);
    }

    private String getArgName() {
        String cleanId = rawId.replace("@+id/", "").replace("@id/", "")
                .replace("@android:id/", "android_");

        String camelCase = Stream.of(cleanId.split("_"))
                .map(this::camelCase)
                .collect(Collectors.joining());
        return camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
    }

    private String camelCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private ClassName getTypeName() {
        try {
            return ClassName.get(Class.forName(type));
        } catch (ClassNotFoundException ignore) { }
        if (type.contains("\\.")) {
            String simpleName = Stream.of(type.split("\\.")).reduce((a, b) -> b).orElse("");
            return ClassName.get(type.replace("." + simpleName, ""), simpleName);
        }
        try {
            return ClassName.get(Class.forName("android.view." + type));
        } catch (ClassNotFoundException ignore) { }
        try {
            return ClassName.get(Class.forName("android.widget." + type));
        } catch (ClassNotFoundException ignore) { }
        return null;
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

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals("" + obj);
    }
}
