package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonLong implements MojangsonValue<Long> {
    private long value;

    public MojangsonLong() {

    }

    public MojangsonLong(long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return long.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value).append(MojangsonToken.LONG_SUFFIX);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        if (Character.toString(lastChar).toLowerCase().charAt(0) == MojangsonToken.LONG_SUFFIX.getSymbol()) {
            string = string.substring(0, string.length() - 1);
        }

        try {
            value = Long.parseLong(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
