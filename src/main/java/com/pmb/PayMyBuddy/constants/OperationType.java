package com.pmb.PayMyBuddy.constants;

public enum OperationType {

    DEBIT ("DEBIT") ,CREDIT("CREDIT");
    private final String OperationType;


    OperationType(String type) {
        this.OperationType = type;
    }

    public String getOperationType() {
        return OperationType;
    }
}
