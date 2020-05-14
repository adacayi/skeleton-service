package uk.co.sancode.skeleton_service.model;

public enum LogCategory {
    VALIDATION("[Validation Category]"),
    AUTHORIZATION("[Authorization Category]");

    private final String name;

    LogCategory(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
