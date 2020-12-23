package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */
public class MojangsonFloat implements MojangsonValue<Float> {
    private float value;

    public MojangsonFloat() {

    }

    public MojangsonFloat(float value) {
        this.value = value;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return float.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value).append(MojangsonToken.FLOAT_SUFFIX);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        if (Character.toString(lastChar).toLowerCase().charAt(0) == MojangsonToken.FLOAT_SUFFIX.getSymbol()) {
            string = string.substring(0, string.length() - 1);
        }

        try {
            value = Float.parseFloat(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
