package ca.momothereal.mojangson.ex;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonParseException extends Exception {

    private ParseExceptionReason reason;

    public MojangsonParseException(String message, ParseExceptionReason reason) {
        super(message);
        this.reason = reason;
    }

    public ParseExceptionReason getReason() {
        return reason;
    }

    @Override
    public String getMessage() {
        return reason.getMessage() + ": " + super.getMessage();
    }

    public enum ParseExceptionReason {
        INVALID_FORMAT_NUM("Given value is not numerical"),
        UNEXPECTED_SYMBOL("Unexpected symbol in Mojangson string");

        private String message;

        ParseExceptionReason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
