package utils;

public enum UserConfig {
    BASE_URL("jdbc:mysql://localhost:3306/personal_finance"),
    DB_USER("root"),
    DB_PASSWORD("pokemontio");

    private final String value;

    UserConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
