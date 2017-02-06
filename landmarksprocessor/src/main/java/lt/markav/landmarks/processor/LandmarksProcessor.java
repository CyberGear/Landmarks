package lt.markav.landmarks.processor;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import lt.markav.landmarks.annotation.WithLandmarks;
import lt.markav.landmarks.processor.parser.Layout;
import lt.markav.landmarks.processor.parser.LayoutsParser;

import static javax.tools.Diagnostic.Kind.WARNING;

@SupportedAnnotationTypes("lt.markav.landmarks.annotation.WithLandmarks")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LandmarksProcessor extends AbstractProcessor implements Logging {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(WithLandmarks.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;

        header();
        try {
            processWithErrors(annotations, roundEnv);
        } catch (Exception e) {
            messager.printMessage(WARNING, "SKIPPING: " + e.getMessage());
            return false;
        }
        header();
        return true;
    }

    private void processWithErrors(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        LayoutsParser parser = new LayoutsParser(processingEnv);

        List<Layout> layouts = parser.parseLayouts();
        WithLandmarksAnnotation annotation = new WithLandmarksAnnotation(roundEnv);

        layouts.forEach(this::log);
        layouts.stream()
                .map(layout -> new ClassGenerator(annotation.getAppId(), layout))
                .map(ClassGenerator::generate)
                .forEach(this::safe);
    }

    private void safe(JavaFile javaFile) {
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new LandmarksException(e.getMessage());
        }
    }

    private void header() {
        System.out.println("#####################################################################");
    }

}
