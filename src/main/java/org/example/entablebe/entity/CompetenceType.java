package org.example.entablebe.entity;

public enum CompetenceType {
    D1("D1 Physical"),

    D2("D2 Social"),

    D3("D3 Occupational"),

    D4("D4 Emotional"),

    D5("D5 Intellectual"),

    D6("D6 Environmental"),

    D7("D7 Spiritual"),
    REMEDIES("Remedies");
    private String name;

    CompetenceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
