package lt.markav.legendsoflayouts.processor.parser.tag;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import static lt.markav.legendsoflayouts.processor.util.ClassNameUtil.classNameForType;

public class ViewTag implements Tag {

    private String type;
    private String rawId;
    private ClassName argType;
    private String argName;

    public ViewTag(Element element) {
        type = element.getTagName();
        rawId = element.getAttribute("android:id");
    }

    @Override
    public void declareField(TypeSpec.Builder classBuilder) {
        classBuilder.addField(
                argType = classNameForType(type),
                argName = getArgName(),
                Modifier.PUBLIC, Modifier.FINAL);
    }

    private String getArgName() {
        String cleanId = rawId.replace("@+id/", "").replace("@id/", "")
                .replace("@android:id/", "android_");

        String camelCase = Stream.of(cleanId.split("_"))
                .map(this::firstUpperCase)
                .collect(Collectors.joining());
        return camelCase.substring(0, 1).toLowerCase() + camelCase.substring(1);
    }

    private String firstUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void generateInitialization(MethodSpec.Builder builder, String name) {
        builder.addStatement("$L = ($T) $L.findViewById($L)",
                argName, argType, name,
                rawId.replace("@+id/", "R.id.")
                        .replace("@id/", "R.id.")
                        .replace("@android:id/", "android.R.id.")
        );
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
