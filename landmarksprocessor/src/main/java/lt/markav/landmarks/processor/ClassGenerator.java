package lt.markav.landmarks.processor;

import android.view.*;
import android.widget.EditText;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;

public class ClassGenerator implements Logging {

    private final String appId;
    private Layout layout;

    private String className;

    public ClassGenerator(String appId, Layout layout) {
        this.appId = appId;
        this.layout = layout;

        className = createClassName(layout.getName());
    }

    private String createClassName(String name) {
        return Stream.of(name.replace(".xml", "").split("_"))
                .map(str -> str.substring(0, 1).toUpperCase() + str.substring(1))
                .reduce((str1, str2) -> str1 + str2)
                .orElseThrow(() -> new LandmarksException("Failed to createClassName"))
                + "Landmarks";
    }


    public JavaFile generate() {
        try {
            return createClassFile();
        } catch (Exception cause) {
            throw new LandmarksException(cause);
        }
    }

    private JavaFile createClassFile() throws Exception {

//        MethodSpec.constructorBuilder()




//        ParameterSpec android = ParameterSpec.builder(String.class, "android")
//                .addModifiers(Modifier.FINAL)
//                .build();
//
//        MethodSpec welcomeOverlords = MethodSpec.methodBuilder("welcomeOverlords")
//                .addParameter(android)
//                .addParameter(String.class, "robot", Modifier.FINAL)
//                .build();

        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

        layout.getViews().stream().distinct().forEach(view -> {
            ClassName classOfView = getClassOfView(view.getType());
            if (classOfView != null) {
                classBuilder.addField(classOfView, view.getName(), Modifier.PUBLIC/*, Modifier.FINAL*/);
            }
        });

        TypeSpec landmarksClass = classBuilder.build();

        return JavaFile.builder(appId, landmarksClass).build();
    }

    private ClassName getClassOfView(String type) {
        if (type.equals("fragment")) {
            return null;
        }
        try {
            return ClassName.get(Class.forName(type));
        } catch (ClassNotFoundException ignore) { }
        try {
            return ClassName.get(Class.forName("android.view." + type));
        } catch (ClassNotFoundException ignore) { }
        try {
            return ClassName.get(Class.forName("android.widget." + type));
        } catch (ClassNotFoundException ignore) { }

        if (type.contains("\\.")) {
            String simpleName = Stream.of(type.split("\\.")).reduce((a, b) -> b).orElse("");
            return ClassName.get(type.replace("." + simpleName, ""), simpleName);
        }
        return null;
    }

}
