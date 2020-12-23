package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonShort implements MojangsonValue<Short> {
    private short value;

    public MojangsonShort() {

    }

    public MojangsonShort(short value) {
        this.value = value;
    }

    public Short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return short.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value).append(MojangsonToken.SHORT_SUFFIX);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        if (Character.toString(lastChar).toLowerCase().charAt(0) == MojangsonToken.SHORT_SUFFIX.getSymbol()) {
            string = string.substring(0, string.length() - 1);
        }

        try {
            value = Short.parseShort(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
