package org.example.entablebe.entity;

public enum CompetenceType {
    D1("D1"),

    D2("D2"),

    D3("D3"),

    D4("D4"),

    D5("D5"),

    D6("D6"),

    D7("D7");
    private String name;

    CompetenceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
