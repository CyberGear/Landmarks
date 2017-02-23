package lt.markav.legendsoflayouts.processor;

import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import lt.markav.legendsoflayouts.processor.util.Logging;

import static javax.tools.Diagnostic.Kind.ERROR;

    public abstract class BaseProcessor extends AbstractProcessor implements Logging {

    private final Class<?> annotation;
    private final SourceVersion version;

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;

    public BaseProcessor(Class<?> annotation, SourceVersion version) {
        this.annotation = annotation;
        this.version = version;
    }

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
        return Collections.singleton(annotation.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() { // @SupportedSourceVersion(SourceVersion.RELEASE_7)
        return version;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        header();
        if (annotations.isEmpty()) {
            return false;
        }

        try {
            process(roundEnv);
            header();
            return true;
        } catch (Exception e) {
            messager.printMessage(ERROR, "SKIPPING: " + e.getMessage());
            header();
            return false;
        }
    }

    protected abstract void process(RoundEnvironment roundEnv) throws Exception ;

    private void header() {
        System.out.println("#####################################################################");
    }

}
