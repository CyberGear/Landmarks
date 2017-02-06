package lt.markav.landmarks.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import java.util.stream.Stream;

import javax.lang.model.element.Modifier;

import lt.markav.landmarks.processor.parser.Layout;

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

        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder(className)
                .addModifiers(Modifier.PUBLIC);

        layout.getTags().forEach(tag -> tag.declareField(classBuilder));

        TypeSpec landmarksClass = classBuilder.build();

        return JavaFile.builder(appId, landmarksClass).build();
    }

//        MethodSpec.constructorBuilder()




//        ParameterSpec android = ParameterSpec.builder(String.class, "android")
//                .addModifiers(Modifier.FINAL)
//                .build();
//
//        MethodSpec welcomeOverlords = MethodSpec.methodBuilder("welcomeOverlords")
//                .addParameter(android)
//                .addParameter(String.class, "robot", Modifier.FINAL)
//                .build();

}
