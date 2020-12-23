package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonString implements MojangsonValue<String> {
    private String value;

    public MojangsonString() {

    }

    public MojangsonString(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(MojangsonToken.STRING_QUOTES).append(value).append(MojangsonToken.STRING_QUOTES);
    }

    @Override
    public Class<?> getValueClass() {
        return String.class;
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        char firstChar = string.charAt(0);

        if (firstChar == MojangsonToken.STRING_QUOTES.getSymbol() && lastChar == MojangsonToken.STRING_QUOTES.getSymbol()) {
            value = string.substring(1, string.length() - 1);
        } else {
            value = string;
        }
    }
}
