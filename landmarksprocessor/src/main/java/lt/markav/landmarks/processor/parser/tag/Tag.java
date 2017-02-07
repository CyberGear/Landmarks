package lt.markav.landmarks.processor.parser.tag;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import org.w3c.dom.Element;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import lt.markav.landmarks.processor.LegendException;

public interface Tag {

    static Tag parse(Element element) {
        return Type.getFor(element).newInstance(element);
    }

    void declareField(TypeSpec.Builder classBuilder);

    void generateInitialization(MethodSpec.Builder builder, String name);

    boolean isValid();

    enum Type {
        FRAGMENT("fragment", FragmentTag::new),
        INCLUDE("include", IncludeTag::new),
        VIEW(".*", ViewTag::new);

        private final Pattern pattern;
        private final Function<Element, Tag> fabric;

        Type(String pattern, Function<Element, Tag> fabric) {
            this.pattern = Pattern.compile(pattern);
            this.fabric = fabric;
        }

        public static Type getFor(Element element) {
            return Stream.of(values())
                    .filter(type -> type.pattern.matcher(element.getTagName()).matches())
                    .findFirst()
                    .orElseThrow(() ->
                            new LegendException("Unknown view type: " + element.getTagName()));
        }

        private Tag newInstance(Element element) {
            return fabric.apply(element);
        }
    }

}
