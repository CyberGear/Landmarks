package lt.markav.landmarks.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("lt.markav.landmarks.annotation.GenerateLandmarks")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LandmarksProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;
        header();
        try {
            processWithErrors(annotations, roundEnv);
        } catch (Exception e) {
            print(e.getMessage());
        }
        header();
        return true;
    }

    private void processWithErrors(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        LayoutsProcessor locator = new LayoutsProcessor(processingEnv);
        locator.collectDataForLandmarksGeneration();

//        print(roundEnv);
//        print(roundEnv.getElementsAnnotatedWith(GenerateLandmarks.class));
//
//        print(layouts);
    }

    private void header() {
        print("\t\t\t##############################################33");
        print("\t\t\t##############################################33");
        print("\t\t\t##############################################33");
        print("\t\t\t##############################################33");
    }

    private void print(Object o) {
        System.out.println(o.toString());
    }
}
