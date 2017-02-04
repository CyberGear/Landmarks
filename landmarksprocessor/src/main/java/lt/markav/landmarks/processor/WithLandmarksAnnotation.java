package lt.markav.landmarks.processor;

import java.util.Optional;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;

import lt.markav.landmarks.annotation.WithLandmarks;

public class WithLandmarksAnnotation {

    private final String appId;

    public WithLandmarksAnnotation(RoundEnvironment env) {
        Optional<? extends Element> element = env.getElementsAnnotatedWith(WithLandmarks.class)
                .stream().findFirst();

        TypeElement typeElement = (TypeElement) element.orElseThrow(() ->
                new LandmarksException("No class annotated with 'WithLandmarks' annotation")
        );

        WithLandmarks annotation = typeElement.getAnnotation(WithLandmarks.class);

        String appId;
        try {
            Class<?> aClass = annotation.value();
            appId = aClass.getCanonicalName().replace("." + aClass.getSimpleName(), "");
        } catch (MirroredTypeException cause) {
            DeclaredType classTypeMirror = (DeclaredType) cause.getTypeMirror();
            TypeElement classElement = (TypeElement) classTypeMirror.asElement();
            String canonical = classElement.getQualifiedName().toString();
            appId = canonical.replace("." + classElement.getSimpleName().toString(), "");
        }

        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    @Override
    public String toString() {
        return appId;
    }

}
