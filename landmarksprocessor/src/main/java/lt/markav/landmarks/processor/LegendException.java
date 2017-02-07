package lt.markav.landmarks.processor;

public class LegendException extends RuntimeException {

    public LegendException(String message) {
        super(message);
    }

    public LegendException(Exception e) {
        super(e);
    }
}
