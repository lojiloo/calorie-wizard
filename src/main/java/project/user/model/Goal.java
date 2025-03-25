package project.user.model;

import java.util.Optional;

public enum Goal {
    WEIGHT_LOSS, MAINTENANCE, WEIGHT_GAIN;

    public static Optional<Goal> from(String stringValue) {
        for (Goal goal : values()) {
            if (goal.name().equalsIgnoreCase(stringValue)) {
                return Optional.of(goal);
            }
        }
        return Optional.empty();
    }
}
