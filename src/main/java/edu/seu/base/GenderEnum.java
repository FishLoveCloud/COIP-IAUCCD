package edu.seu.base;

public enum GenderEnum {

    Male(0),

    Female(1);

    int value;
    GenderEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
