package lt.markav.legendsoflayouts.processor.parser.tag;

import java.util.stream.Stream;

import javax.lang.model.type.TypeMirror;

public enum FragmentType {

    SUPPORT("android.support.v4.app.Fragment", "$L = ($T) $L.getSupportFragmentManager().findFragmentById($L)"),
    NATIVE("android.app.Fragment", "$L = ($T) $L.getFragmentManager().findFragmentById($L)");

    private final String classNane;
    public final String statement;

    FragmentType(String classNane, String statement) {
        this.classNane = classNane;
        this.statement = statement;
    }

    public static FragmentType from(TypeMirror superclass) {
        return Stream.of(values())
                .filter(it -> it.classNane.equals(superclass.toString()))
                .findFirst()
                .orElse(null);
    }

}
