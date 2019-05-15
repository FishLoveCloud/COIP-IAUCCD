package edu.seu.base;

public enum LevelEnum {

    VISITOR(0),

    MEMVER(1),

    ADMIN(2);

    int value;
    LevelEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
