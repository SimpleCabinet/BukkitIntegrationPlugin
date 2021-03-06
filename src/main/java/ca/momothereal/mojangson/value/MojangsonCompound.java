package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonFinder;
import ca.momothereal.mojangson.ex.MojangsonParseException;

import java.util.HashMap;
import java.util.Map;

import static ca.momothereal.mojangson.MojangsonToken.*;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonCompound extends HashMap<String, MojangsonValue<?>> implements MojangsonValue<Map<String, MojangsonValue<?>>> {

    private final int C_COMPOUND_START = 0;      // Parsing context
    private final int C_COMPOUND_PAIR_KEY = 1;   // Parsing context
    private final int C_COMPOUND_PAIR_VALUE = 2; // Parsing context

    public MojangsonCompound() {

    }
    @SuppressWarnings("unchecked")
    public MojangsonCompound(Map<?, ?> map) {
        super((Map<? extends String, ? extends MojangsonValue<?>>) map);
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(COMPOUND_START);
        boolean start = true;

        for (String key : keySet()) {
            if (start) {
                start = false;
            } else {
                builder.append(ELEMENT_SEPERATOR);
            }

            builder.append(key).append(ELEMENT_PAIR_SEPERATOR);
            MojangsonValue<?> value = get(key);
            value.write(builder);
        }
        builder.append(COMPOUND_END);
    }

    @Override
    public void read(String string) throws MojangsonParseException {
        int context = C_COMPOUND_START;
        StringBuilder tmp_key = new StringBuilder();
        StringBuilder tmp_val = new StringBuilder();
        int scope = 0;
        boolean inString = false;

        for (int index = 0; index < string.length(); index++) {
            char character = string.charAt(index);

            if (character == STRING_QUOTES.getSymbol()) {
                inString = !inString;
            }
            if (character == WHITE_SPACE.getSymbol()) {
                if (!inString)
                    continue;
            }
            if ((character == COMPOUND_START.getSymbol() || character == ARRAY_START.getSymbol()) && !inString) {
                scope++;
            }
            if ((character == COMPOUND_END.getSymbol() || character == ARRAY_END.getSymbol()) && !inString) {
                scope--;
            }
            if (context == C_COMPOUND_START) {
                if (character != COMPOUND_START.getSymbol()) {
                    parseException(index, character);
                    return;
                }
                context++;
                continue;
            }
            if (context == C_COMPOUND_PAIR_KEY) {
                if (character == ELEMENT_PAIR_SEPERATOR.getSymbol() && scope <= 1) {
                    context++;
                    continue;
                }
                tmp_key.append(character);
                continue;
            }
            if (context == C_COMPOUND_PAIR_VALUE) {
                if (((character == ELEMENT_SEPERATOR.getSymbol() && scope <= 1) || (character == COMPOUND_END.getSymbol() && scope <= 0)) && !inString) {
                    context = C_COMPOUND_PAIR_KEY;
                    put(tmp_key.toString(), MojangsonFinder.readFromValue(tmp_val.toString()));
                    tmp_key = new StringBuilder();
                    tmp_val = new StringBuilder();
                    continue;
                }
                tmp_val.append(character);
            }
        }
    }

    @Override
    public Map<String, MojangsonValue<?>> getValue() {
        return this;
    }

    @Override
    public Class<?> getValueClass() {
        return Map.class;
    }

    private void parseException(int index, char symbol) throws MojangsonParseException {
        throw new MojangsonParseException("Index: " + index + ", symbol: \'" + symbol + "\'", MojangsonParseException.ParseExceptionReason.UNEXPECTED_SYMBOL);
    }
}
