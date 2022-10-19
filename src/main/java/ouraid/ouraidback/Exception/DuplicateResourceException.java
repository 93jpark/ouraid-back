package ouraid.ouraidback.Exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String str) {
        super(str+" is already registered.");
    }
}
