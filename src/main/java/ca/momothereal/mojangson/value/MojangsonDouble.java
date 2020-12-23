package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonToken;
import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonDouble implements MojangsonValue<Double> {
    private double value;

    public MojangsonDouble() {

    }

    public MojangsonDouble(double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public Class<?> getValueClass() {
        return double.class;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(value).append(MojangsonToken.DOUBLE_SUFFIX);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        char lastChar = string.charAt(string.length() - 1);
        if (Character.toString(lastChar).toLowerCase().charAt(0) == MojangsonToken.DOUBLE_SUFFIX.getSymbol()) {
            string = string.substring(0, string.length() - 1);
        }

        try {
            value = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            throw new MojangsonParseException("\'" + string + "\'", MojangsonParseException.ParseExceptionReason.INVALID_FORMAT_NUM);
        }
    }
}
