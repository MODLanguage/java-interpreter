package uk.modl.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class NotEqualsOperator implements Operator {

    public static NotEqualsOperator instance = new NotEqualsOperator();

    private NotEqualsOperator() {
    }

}
