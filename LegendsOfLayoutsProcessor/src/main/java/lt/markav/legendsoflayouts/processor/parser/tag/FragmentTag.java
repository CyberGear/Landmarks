package lt.markav.legendsoflayouts.processor.parser.tag;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import lt.markav.legendsoflayouts.processor.util.Logging;

import static lt.markav.legendsoflayouts.processor.util.ClassNameUtil.classNameForType;
import static lt.markav.legendsoflayouts.processor.util.ClassNameUtil.classNameToArgName;

public class FragmentTag implements Tag, Logging {

    private final String rawName;
    private final String rawId;
    private final String rawTag;

    private ClassName argType;
    private String argName;
    private FragmentType fragmentType;

    public FragmentTag(Element element) {
        rawName = element.getAttribute("android:name");
        rawId = element.getAttribute("android:id");
        rawTag = element.getAttribute("android:tag");

        argType = classNameForType(rawName);
        argName = classNameToArgName(argType);
    }

    @Override
    public void declareField(TypeSpec.Builder classBuilder) {
        classBuilder.addField(argType, argName, Modifier.PUBLIC/*, Modifier.FINAL*/);
    }

    @Override
    public void generateInitialization(MethodSpec.Builder builder, String name) {
        builder.addStatement(fragmentType.statement, argName, argType, name,
                rawId.replace("@+id/", "R.id.")
                        .replace("@id/", "R.id.")
                        .replace("@android:id/", "android.R.id.")
        );
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

    public FragmentType getFragmentType(Elements elementUtils) {
        TypeMirror superclass = elementUtils.getTypeElement(argType.toString()).getSuperclass();
        return fragmentType = FragmentType.from(superclass);
    }
}
