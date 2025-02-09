package org.example.entablebe.entity;

public enum CompetenceType {
    D1("D1 Physical", 0),

    D2("D2 Social", 1),

    D3("D3 Occupational", 2),

    D4("D4 Emotional", 3),

    D5("D5 Intellectual", 4),

    D6("D6 Environmental", 5),

    D7("D7 Spiritual", 6),

    REMEDIES("Remedies", 7),

    NUTRITION("Nutrition", 8),
    OTHER("Other requirements", 9);
    private final String name;
    private final int index;

    CompetenceType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static CompetenceType mapCompetenceType(String competence) {
        String comp = competence.split(" ")[0].toUpperCase();
        return CompetenceType.valueOf(comp);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
