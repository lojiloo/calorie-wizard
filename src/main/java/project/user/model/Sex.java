package project.user.model;

import java.util.Optional;

public enum Sex {
    MALE, FEMALE;

    public static Optional<Sex> from(String stringValue) {
        for (Sex sex : Sex.values()) {
            if (sex.name().equalsIgnoreCase(stringValue)) {
                return Optional.of(sex);
            }
        }
        return Optional.empty();
    }
}
