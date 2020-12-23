package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.MojangsonFinder;
import ca.momothereal.mojangson.ex.MojangsonParseException;

import java.util.ArrayList;
import java.util.List;

import static ca.momothereal.mojangson.MojangsonToken.*;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

public class MojangsonArray<T extends MojangsonValue<?>> extends ArrayList<T> implements MojangsonValue<List<T>> {

    private static final int C_ARRAY_START = 0;   // Parsing context
    private static final int C_ARRAY_ELEMENT = 1; // Parsing context

    private Class<? extends MojangsonValue<?>> type;

    public MojangsonArray() {

    }

    public MojangsonArray(Class<? extends MojangsonValue<?>> type) {
        this.type = type;
    }

    public MojangsonArray(Class<? extends MojangsonValue<?>> type, List<T> list) {
        this.type = type;
        addAll(list);
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(ARRAY_START);
        boolean start = true;

        for (MojangsonValue<?> value : this) {
            if (start) {
                start = false;
            } else {
                builder.append(ELEMENT_SEPERATOR);
            }
            value.write(builder);
        }
        builder.append(ARRAY_END);
    }

    @Override
    public List<T> getValue() {
        return this;
    }

    public Class<? extends MojangsonValue<?>> getType() {
        return type;
    }

    @Override
    public Class<?> getValueClass() {
        return List.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(String string) throws MojangsonParseException {
        int context = C_ARRAY_START;
        StringBuilder tmpval = new StringBuilder();
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
            if (context == C_ARRAY_START) {
                if (character != ARRAY_START.getSymbol()) {
                    parseException(index, character);
                    return;
                }
                context++;
                continue;
            }
            if (context == C_ARRAY_ELEMENT) {
                if (((character == ELEMENT_SEPERATOR.getSymbol() && scope <= 1) || (character == ARRAY_END.getSymbol() && scope <= 0)) && !inString) {
                    if (tmpval.length() == 0) {
                        continue;
                    }
                    T val = (T) MojangsonFinder.readFromValue(tmpval.toString());

                    if (this.getType() == null)
                        this.type = (Class<? extends MojangsonValue<?>>) val.getClass();

                    add(val);
                    tmpval = new StringBuilder();
                    continue;
                }
                tmpval.append(character);
            }
        }
    }

    private void parseException(int index, char symbol) throws MojangsonParseException {
        throw new MojangsonParseException("Index: " + index + ", symbol: '" + symbol + "'", MojangsonParseException.ParseExceptionReason.UNEXPECTED_SYMBOL);
    }
}
