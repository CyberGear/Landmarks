package lt.markav.legendsoflayouts.processor;

import java.util.Optional;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import lt.markav.legendsoflayouts.annotation.FragmentsType;
import lt.markav.legendsoflayouts.annotation.LegendsOfLayouts;

public class LegendsOfLayoutsAnnotation {

    private final String appId;
    @FragmentsType
    private final String fragmentsType;

    public LegendsOfLayoutsAnnotation(RoundEnvironment env) {
        Optional<? extends Element> element = env.getElementsAnnotatedWith(LegendsOfLayouts.class)
                .stream().findFirst();

        TypeElement typeElement = (TypeElement) element.orElseThrow(() ->
                new LegendException("No class annotated with 'LegendsOfLayouts' annotation")
        );

        LegendsOfLayouts annotation = typeElement.getAnnotation(LegendsOfLayouts.class);

        fragmentsType = annotation.fragmentsType();
        String appId;
        appId = getAppId(annotation::value);
        if (appId.equals("java.lang")) {
            appId = getAppId(annotation::rClass);
        }
        if (appId.equals("java.lang")) {
            throw new LegendException("No R class defined in annotation");
        }
        this.appId = appId;
    }

    private String getAppId(ClassGetter classGetter) {
        String appId;
        try {
            Class<?> aClass = classGetter.getClassParam();
            appId = aClass.getCanonicalName().replace("." + aClass.getSimpleName(), "");
        } catch (MirroredTypeException cause) {
            DeclaredType classTypeMirror = (DeclaredType) cause.getTypeMirror();
            TypeElement classElement = (TypeElement) classTypeMirror.asElement();
            String canonical = classElement.getQualifiedName().toString();
            appId = canonical.replace("." + classElement.getSimpleName().toString(), "");
        }
        return appId;
    }

    public String getAppId() {
        return appId;
    }

    public String getFragmentsType() {
        return fragmentsType;
    }

    @Override
    public String toString() {
        return appId + ":" + fragmentsType;
    }

    private interface ClassGetter {
        Class<?> getClassParam();
    }

}
