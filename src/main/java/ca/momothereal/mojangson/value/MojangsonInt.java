package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonInt implements MojangsonValue<Integer> {
    private int value;

    public MojangsonInt() {

    }

    public MojangsonInt(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return int.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        try {
            value = Integer.parseInt(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
