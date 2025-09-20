package interpreting.common;

/**
 * Represents the result of some process during interpreting.
 * If the process succeeded, <code>value</code> holds the result of the process, and <code>message</code> should be <code>null</code>.
 * If the process failed, <code>value</code> should be <code>null</code>, and <code>message</code> should describe what happened.
 * <p>
 * To be particular, exactly one of <code>value</code> or <code>message</code> should be <code>null</code>.
 * <p>
 * Additionally, if an error does occur, messages should "bubble up". Example usage:
 * <pre> {@code
 * InterpretingResult<T> result = ...
 * T value = result.value()
 * if (value == null)
 *     return new InterpretingResult<>(null, "An error occurred: " + result.message());
 * } </pre>
 * @param value the return value (null if an error occurred)
 * @param message the message (null if no error occurred)
 * @param <T> the type of <code>value</code>
 */
public record InterpretingResult<T>(
        T value,
        String message
) {
}
