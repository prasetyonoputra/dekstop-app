package utils;

public enum UserConfig {
    BASE_URL("jdbc:mysql://localhost:3306/"),
    DB_USER("root"),
    DB_PASSWORD("");

    private final String value;

    UserConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
