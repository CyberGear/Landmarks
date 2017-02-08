package lt.markav.legendsoflayouts.processor.util;

public interface Logging {

    default void log(Object o) {
        System.out.println("\t"+getClass().getSimpleName() + ": " + (o != null ? o.toString() : null));
    }

}
