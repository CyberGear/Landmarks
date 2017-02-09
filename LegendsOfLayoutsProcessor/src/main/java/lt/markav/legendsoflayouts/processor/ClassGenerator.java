package lt.markav.legendsoflayouts.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.stream.Stream;

import lt.markav.legendsoflayouts.processor.parser.Layout;
import lt.markav.legendsoflayouts.processor.util.Android;
import lt.markav.legendsoflayouts.processor.util.Logging;

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

        layout.getTags().forEach(tag -> tag.declareField(classBuilder));

        MethodSpec constructorForActivity = createConstructorForActivity();
        MethodSpec constructorForView = createConstructorForView();
        MethodSpec constructorForSupportFragment = createConstructorForSupportFragment();

        classBuilder.addMethod(constructorForActivity);
        classBuilder.addMethod(constructorForView);
        classBuilder.addMethod(constructorForSupportFragment);

        TypeSpec landmarksClass = classBuilder.build();
        return JavaFile.builder(appId, landmarksClass).build();
    }

    private MethodSpec createConstructorForActivity() {
        return createConstructor(Android.ACTIVITY, "activity", this::generateInitialization);
    }

    private MethodSpec createConstructorForView() {
        return createConstructor(Android.VIEW, "view", this::generateInitialization);
    }

    private MethodSpec createConstructorForSupportFragment() {
        TypeName fragment = ClassName.get("android.support.v4.app", "Fragment");
        return createConstructor(fragment, "fragment", (builder, name) -> {
            builder.addStatement("this($L.getView())", name);
        });
    }

    private MethodSpec createConstructor(TypeName className, String name, MethodFiller filler) {
        ParameterSpec param = builder(className, name).build();
        MethodSpec.Builder builder = constructorBuilder().addModifiers(PUBLIC).addParameter(param);
        filler.fill(builder, param.name);
        return builder.build();
    }

    private void generateInitialization(MethodSpec.Builder builder, String name) {
        layout.getTags().stream().forEach(tag -> tag.generateInitialization(builder, name));
    }

    public interface MethodFiller {

        void fill(MethodSpec.Builder builder, String name);

    }

}
