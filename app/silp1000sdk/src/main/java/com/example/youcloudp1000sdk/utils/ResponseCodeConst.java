package com.example.youcloudp1000sdk.utils;

public class ResponseCodeConst {
    // General
    public static final String USE_CHIP = "CARD01";
    public static final String INTERNET_ISSUE = "CD91";
    public static final String NULL_RESP = "CD91";
    public static final String SESSION_EXPIRED = "CD01";
    public static final String EXCEPTION = "CD02";
    public static final String DB_EXCEPTION = "CD03";
    public static final String RECORD_NOT_FOUND = "CD04";
    public static final String INVALID_REQ = "CD05";
    public static final String INVALID_IMEI_IMSI_NO = "CD06";
    public static final String FILL_ALL_DETAILS = "CD07";
    public static final String TRANS_SUCCESS = "CD00";
    public static final String SUCCESS = "CD00";
    public static final String WRONG_PIN = "CD08";
    public static final String USER_BLOCK = "CD09";
    public static final String USER_NOT_FOUND = "CD10";
    public static final String Device_Block = "CD11";
    public static final String VERSION_NOT_MATCH = "CD12";
    public static final String WRONG_DETAILS = "CD13";
    public static final String USER_ALREADY_REGISTER = "CD14";
    public static final String MOBILE_ALREADY_REGISTER = "CD15";
    public static final String MSISDN_NOT_FOUND = "CD16";
    public static final String TID_NOT_FOUND = "CD17";
    public static final String DEVICE_NOT_REGISTERD = "CD17";
    public static final String PIN_CHANGED_SUCCESSFULLY = "CD18";
    public static final String DEVICE_UNAUTHORISED = "CD19";
    public static final String GIVEN_INVOICE_NO_IS_ALREADY_VOID = "CD20";
    public static final String ORIGINAL_TRANSACTION_DECLINED = "CD21";

    // Merchant Creation
    public static final String INVALID_MERCHANT_ID = "CD22";
    public static final String INVALID_FIRST_NAME = "CD23";
    public static final String INVALID_LAST_NAME = "CD24";
    public static final String INVALID_MOB_NO = "CD25";
    public static final String MERCHANT_ID_NOT_FOUND = "CD26";
    public static final String MERCHANT_ID_NOT_FOUND_LINKAGE_MASTER = "CD27";
    public static final String MERCHANT_ID_NOT_FOUND_MPOS_USER_MASTER = "CD28";
    public static final String MERCHANT_ID_ALREADY_REG = "CD29";
    public static final String USER_ID_NOT_MERCHANT = "CD30";
    public static final String SUB_MERCHANT_CREATED = "CD31";
    public static final String DEVICE_REG_SUCCESS = "CD32";

    // card payment
    public static final String CARD_MERCHANT_NOT_FOUND = "CD33";
    public static final String INVALID_5513RESULT = "CD34";

    // change pass
    public static final String PIN_CHG_WRONG_DETAILS = "CD35";
    public static final String PIN_CHG_OTP_SENT = "OCD36";
    public static final String PIN_CHG_WRONG_OTP = "CD37";

    // Cash Drawer
    public static final String WALLET = "WALLET";
    public static final String CASH_TRN = "CASH";
    public static final String CARD_TRN = "CARD";
    public static final String VOID = "VOID";
    public static final String CARD_PRE_AUTH = "PRE-AUTH";
    public static final String YOU_CLOUD_MPOS_REGISTRATION_SUCCESSFULLY = "CD38";
    public static final String INVALID_DETAILS = "CD39";
    public static final String SEND_RECEIPT_ON_MOBILE_NO_AND_EMAIL_REGISTERED_SUCCESSFULLY = "CD40";
    public static final String UNABLE_TO_SUBMIT_RECEIPT = "CD41";
    public static final String FORGOT_PASS_OTP = "CD42";
    public static final String UPI_SMS = "CD43";

    // PreAuth
    public static final String INVALID_RRN = "CD44";
    public static final String LOG_CREATED_SUCCESSFULLY = "CD45";
    public static final String INVALID_TRACK_2 = "CD46";
    public static final String CONNECTION_TIMED_OUT = "CD47";
    public static final String TRANS_FAIL = "CD48";
    public static final String UPI = "CD49";
    public static final String SPECIAL_CHAR_NOT_ALLOWED = "CD50";
    public static final String YOU_REACHED_MAXIMUM_LIMIT_OF_OPT = "CD51";
    public static final String RECHARGE_FAILURE = "CD52";
    public static final String RECHARGE_SUCCESS = "CD53";
    public static final String PAYMENT_UNSUCCESSFUL = "CD54";
    public static final String INSUF_BAL_IN_WALLET = "CD55";
    public static final String WALLET_BAL_UPDATED_SUCCESSFULLY = "CD56";
}
