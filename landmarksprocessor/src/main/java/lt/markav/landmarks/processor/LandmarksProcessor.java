package lt.markav.landmarks.processor;

import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("lt.markav.landmarks.annotation.GenerateLandmarks")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class LandmarksProcessor extends AbstractProcessor implements Logging{

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) return true;
        header();
        try {
            processWithErrors(annotations, roundEnv);
        } catch (Exception e) {
            log(e.getMessage());
        }
        header();
        return true;
    }

    private void processWithErrors(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws Exception {
        LayoutsParser parser = new LayoutsParser(processingEnv);
        List<Layout> layouts = parser.parseAllLayouts();





        layouts.forEach(this::log);
    }

    private void header() {
        System.out.println("#####################################################################");
    }

}
