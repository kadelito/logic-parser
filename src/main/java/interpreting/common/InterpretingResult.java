package interpreting.common;

public record InterpretingResult<T>(
        T value,
        String message
) {
}
