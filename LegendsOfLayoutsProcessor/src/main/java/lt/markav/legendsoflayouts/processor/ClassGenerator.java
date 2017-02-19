package lt.markav.legendsoflayouts.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.util.Elements;

import lt.markav.legendsoflayouts.annotation.FragmentsType;
import lt.markav.legendsoflayouts.processor.parser.Layout;
import lt.markav.legendsoflayouts.processor.parser.tag.FragmentTag;
import lt.markav.legendsoflayouts.processor.parser.tag.FragmentType;
import lt.markav.legendsoflayouts.processor.util.Android;
import lt.markav.legendsoflayouts.processor.util.Logging;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.ParameterSpec.builder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static lt.markav.legendsoflayouts.processor.parser.tag.FragmentType.SUPPORT;

public class ClassGenerator implements Logging {

    @FragmentsType
    private final String fragmentsType;
    private final String appId;
    private final Layout layout;
    private final String className;
    private final Elements elementUtils;

    public ClassGenerator(Elements elementUtils, LegendsOfLayoutsAnnotation annotation, Layout layout) {
        this.fragmentsType = annotation.getFragmentsType();
        this.appId = annotation.getAppId();
        this.layout = layout;
        this.className = createClassName(layout.getName());
        this.elementUtils = elementUtils;
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
        TypeSpec.Builder legendClass = TypeSpec.classBuilder(className).addModifiers(PUBLIC, FINAL);

        Set<FragmentType> fragmentTypes = layout.getTags().stream()
                .filter(tag -> tag instanceof FragmentTag)
                .map(tag -> (FragmentTag) tag)
                .map(fragmentTag -> fragmentTag.getFragmentType(elementUtils))
                .collect(Collectors.toSet());

        layout.streamViews().forEach(tag -> tag.declareField(legendClass));
        layout.streamFragments().forEach(tag -> tag.declareField(legendClass));

        System.out.println("fragments: " + fragmentTypes);
        legendClass.addMethod(getConstructorForActivity(fragmentTypes));

//        legendClass.addMethod(getConstructorForView(fragmentsTypes));

//        if (!fragmentsType.equals(FragmentsType.NATIVE)) {
//            legendClass.addMethod(getConstructorForSupportFragment());
//        }
//        if (!fragmentsType.equals(FragmentsType.SUPPORT)) {
//            legendClass.addMethod(getConstructorForNativeFragment());
//        }

        TypeSpec landmarksClass = legendClass.build();
        return JavaFile.builder(appId, landmarksClass).build();
    }

    private MethodSpec getConstructorForActivity(Set<FragmentType> types) {
        ClassName type = types.contains(SUPPORT) ? Android.APP_COMPAT_ACTIVITY : Android.ACTIVITY;
        return createConstructor(type, "activity", this::generateInitialization);
    }

    private MethodSpec getConstructorForView(Set<FragmentType> fragments) {
        return createConstructor(Android.VIEW, "view", this::generateInitialization);
    }

    private MethodSpec getConstructorForSupportFragment() {
        TypeName fragment = ClassName.get("android.support.v4.app", "Fragment");
        return createConstructor(fragment, "fragment", (builder, name) -> {
            builder.addStatement("this($L.getView())", name);
        });
    }

    private MethodSpec getConstructorForNativeFragment() {
        TypeName fragment = ClassName.get("android.app", "Fragment");
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
        layout.streamViews().forEach(tag -> tag.generateInitialization(builder, name));
        layout.streamFragments().forEach(tag -> tag.generateInitialization(builder, name));
    }

    public interface MethodFiller {

        void fill(MethodSpec.Builder builder, String name);

    }

}
