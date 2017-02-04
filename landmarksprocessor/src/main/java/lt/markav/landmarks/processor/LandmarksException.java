package lt.markav.landmarks.processor;

public class LandmarksException extends RuntimeException {

    public LandmarksException(String message) {
        super(message);
    }

    public LandmarksException(Exception e) {
        super(e);
    }
}
