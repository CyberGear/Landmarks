package lt.markav.landmarks.processor;

import android.app.Activity;
import android.view.View;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Set;
import java.util.stream.Stream;

import lt.markav.landmarks.processor.parser.Layout;
import lt.markav.landmarks.processor.parser.tag.Tag;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.ParameterSpec.builder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

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
                .orElseThrow(() -> new LegendException("Failed to createClassName"))
                + "Legend";
    }

    public JavaFile generate() {
        try {
            return createClassFile();
        } catch (Exception cause) {
            throw new LegendException(cause);
        }
    }

    private JavaFile createClassFile() throws Exception {

        TypeSpec.Builder classBuilder = TypeSpec
                .classBuilder(className)
                .addModifiers(PUBLIC, FINAL);

        //create fields
        layout.getTags().forEach(tag -> tag.declareField(classBuilder));

        //create activity constructor
        MethodSpec constructorForActivity = createConstructor(
                Activity.class, "activity", layout.getTags());
        classBuilder.addMethod(constructorForActivity);

        //create view constructor
        MethodSpec constructorForView = createConstructor(
                View.class, "view", layout.getTags());
        classBuilder.addMethod(constructorForView);

        //create file
        TypeSpec landmarksClass = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(appId, landmarksClass).build();
        javaFile.writeTo(System.out);
        return javaFile;
    }

    private MethodSpec createConstructor(Class<?> aClass, String name, Set<Tag> tags) {
        ParameterSpec param = builder(aClass, name).build();
        MethodSpec.Builder builder = constructorBuilder().addModifiers(PUBLIC).addParameter(param);
        tags.stream().forEach(tag -> tag.generateInitialization(builder, name));
        return builder.build();
    }


}
