package lt.markav.legendsoflayouts.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.ElementFilter;

import lt.markav.legendsoflayouts.annotation.LegendsOfLayouts;
import lt.markav.legendsoflayouts.processor.parser.Layout;
import lt.markav.legendsoflayouts.processor.parser.LayoutsParser;
import lt.markav.legendsoflayouts.processor.util.Logging;

import static com.squareup.javapoet.MethodSpec.constructorBuilder;
import static com.squareup.javapoet.ParameterSpec.builder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class LegendsOfLayoutsProcessor extends BaseProcessor implements Logging {

    public LegendsOfLayoutsProcessor() {
        super(LegendsOfLayouts.class, SourceVersion.RELEASE_7);
    }

    @Override
    protected void process(RoundEnvironment roundEnv) throws Exception {
        List<Layout> layouts = new LayoutsParser(processingEnv).parseLayouts();

        LegendsAnnotation annotation = new LegendsAnnotation(roundEnv);

        layouts.stream()
                .filter(layout -> layout.getName().contains("activity_main"))
                .map(layout -> new ClassGenerator(elementUtils, annotation, layout))
                .map(ClassGenerator::generate)
                .forEach(this::safe);
    }

    private void safe(JavaFile javaFile) {
        System.out.println("---------------------------------------------------------------------");
        try {
            javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            throw new LegendException(e.getMessage());
        }
    }

}
