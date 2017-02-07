package lt.markav.legendsoflayouts.processor.util;

import com.squareup.javapoet.ClassName;

import java.util.stream.Stream;

import lt.markav.legendsoflayouts.processor.LegendException;

public class ClassNameUtil {

    public static ClassName classNameForType(String type) {
        try {
            return ClassName.get(Class.forName(type));
        } catch (ClassNotFoundException ignore) { }
        if (type.contains("\\.")) {
            String simpleName = Stream.of(type.split("\\.")).reduce((a, b) -> b).orElse("");
            return ClassName.get(type.replace("." + simpleName, ""), simpleName);
        }
        try {
            return ClassName.get(Class.forName("android.view." + type));
        } catch (ClassNotFoundException ignored) { }
        try {
            return ClassName.get(Class.forName("android.widget." + type));
        } catch (ClassNotFoundException ignored) { }

        throw new LegendException("Class '" + type + "' not found");
    }

}
