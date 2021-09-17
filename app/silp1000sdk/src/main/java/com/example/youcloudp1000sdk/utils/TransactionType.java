package com.example.youcloudp1000sdk.utils;


public enum TransactionType {
    DEBIT("Debit", Constants.DEBIT),
    WITHDRAWAL("Withdrawal", Constants.SDK_WITHDRAW_SBM),
    INQUIRY("Inquiry", Constants.SDK_ENQUIRY_SBM);

    private String code;
    private String label;

    TransactionType(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }
}

