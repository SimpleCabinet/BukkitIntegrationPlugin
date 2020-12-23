package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonByte implements MojangsonValue<Byte> {
    private byte value;

    public MojangsonByte() {

    }

    public MojangsonByte(byte value) {
        this.value = value;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return byte.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value).append(MojangsonToken.BYTE_SUFFIX);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        if (Character.toString(lastChar).toLowerCase().charAt(0) == MojangsonToken.BYTE_SUFFIX.getSymbol()) {
            string = string.substring(0, string.length() - 1);
        }

        try {
            value = Byte.parseByte(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
