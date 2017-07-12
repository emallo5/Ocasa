package com.android.ocasa.util;

/**
 * Created by leandro on 12/7/17.
 */

public enum Operator {

    EQUALS("=") {
        @Override
        public boolean operate(String pivot, String other) {
            return pivot.equalsIgnoreCase(other);
        }
    }, DIFFERENT("<>") {
        @Override
        public boolean operate(String pivot, String other) {
            return !pivot.equalsIgnoreCase(other);
        }
    };

    String type;

    Operator (String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Operator findOperator(String type) {
        for (Operator o : Operator.values()) {
            if (type.equals(o.getType())) return o;
        }
        return EQUALS; // por defecto va el "igual"
    }

    public boolean operate (String pivot, String other) {
        return false;
    }
}
