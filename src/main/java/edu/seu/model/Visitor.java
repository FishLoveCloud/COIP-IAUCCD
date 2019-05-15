package edu.seu.model;

import edu.seu.base.LevelEnum;
import org.springframework.stereotype.Component;

@Component
public class Visitor extends User {
    public Visitor() {
        setId(-1);
        setLevel(LevelEnum.VISITOR.getValue());
    }
}
