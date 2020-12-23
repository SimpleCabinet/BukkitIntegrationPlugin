package ca.momothereal.mojangson.value;

import ca.momothereal.mojangson.ex.MojangsonParseException;

/**
 * Author https://github.com/aramperes/Mojangson
 * Bug fixes by Gravit'a
 */

/**
 * Represents a value inside a compound or array.
 * @param <T> The type of value this MojangsonValue holds
 */
public interface MojangsonValue<T> {

    /**
     * Writes the value to a StringBuilder buffer.
     * @param builder The buffer to write to
     */
    void write(StringBuilder builder);

    /**
     * Parses and updates the current value to the given string representation
     * @param string The string representation of the value
     * @throws MojangsonParseException if the given value cannot be parsed
     */
    void read(String string) throws MojangsonParseException;

    /**
     * Gets the current literal value
     * @return The current literal value of the MojangsonValue
     */
    T getValue();

    /**
     * Gets the literal value's class
     * @return The literal value's class
     */
    Class getValueClass();

}
