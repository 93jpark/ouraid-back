package ouraid.ouraidback.Exception;

public class DuplicateMemberException extends RuntimeException {
    public DuplicateMemberException(String str) {
        super(str+" is already registered.");
    }
}
