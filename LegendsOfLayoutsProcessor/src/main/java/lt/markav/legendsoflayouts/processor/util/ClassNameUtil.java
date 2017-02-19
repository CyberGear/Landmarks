package lt.markav.legendsoflayouts.processor.util;

import com.squareup.javapoet.ClassName;

import java.util.stream.Stream;

import lt.markav.legendsoflayouts.processor.LegendException;

public class ClassNameUtil {

    public static ClassName classNameForType(String type) {
        if (type.contains(".")) {
            String[] parts = type.split("\\.");
            String simpleName = parts[parts.length - 1];
            return ClassName.get(type.replace("." + simpleName, ""), simpleName);
        }
        ClassName className = Android.getClassName(type);
        if (className == null) {
            throw new LegendException("Class '" + type + "' not found");
        }
        return className;
    }

    public static String classNameToArgName(ClassName className) {
        String name = className.simpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

}
