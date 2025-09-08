package interpreting;

public record InterpretingResult<T>(
        T value,
        String message
) {
}
