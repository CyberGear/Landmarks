package lt.markav.landmarks.processor.parser.tag;

import org.w3c.dom.Element;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import lt.markav.landmarks.processor.LandmarksException;

public interface Tag {

    static Tag parse(Element element) {
        return Type.getFor(element).newInstance(element);
    }

    void generateFiled();

    void generateInitialization();

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
                            new LandmarksException("Unknown view type: " + element.getTagName()));
        }

        private Tag newInstance(Element element) {
            return fabric.apply(element);
        }
    }

}
