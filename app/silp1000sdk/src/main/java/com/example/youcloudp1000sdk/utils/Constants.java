package com.example.youcloudp1000sdk.utils;

import java.util.ArrayList;


/**
 * Created by shankar.savant on 21-01-2017.
 */

public class Constants {

 public static final String PACKAGE_NAME_ERROR = "Package Name Error. Contact Developer";
 public static final int PACKAGE_NAME_ERROR_CODE = 201;
 public static final String REQUEST_ERROR = "UCubeRequest params cannot be empty";
 public  static final String CONTEXT_ERROR_MSG = "Context Cannot be null";
 public static final String KEY_ERROR = "Key Cannot be null or Empty";
 public  static final String VALIDATE_REQUEST_CODE = "validateSdk";

 public static final String PACKAGE_INVALID = "The SDK cannot be integrated with your project. Contact Developer";

 public static final String INITIATE_TRANSACTION_ERROR = "Error occured while initiating Transaction. Contact Developer";

 public static final String NETWORK_EXCEPTION = "Server is down. Try again later";

 public static final String TRANSACTION_WITHDRAW = "withdraw";
 public static final String TRANSACTION_SALE = "sale";
 public static final String TRANSACTION_STATUS = "sdkTxnStatusSbm";

 public static final int REQUEST_ERROR_CODE = 202;
 public static final int CONTEXT_ERROR_CODE = 203;
 public static final int KEY_ERROR_CODE = 204;
 public static final int PACKAGE_INVALID_CODE = 205;
 public static final int NETWORK_EXCEPTION_CODE = 207;
 public static final int TRANSACTION_ID_MISSING_CODE = 206;
 public static final int UNKOWN_ERROR = 109;

 /**
  * transaction type
  */
 public static final String DEBIT = "sdksale";
 public static final String SDK_WITHDRAW_SBM = "microCashWithdrawSbm";
 public static final String SDK_ENQUIRY_SBM = "microBalEnqSbm";

 //request code
 public static final String wsgetMainKey = "getSdkMainKey";
 public static final String wscardpay = "doCardPayment";

 public static final String MICRO_MINI_STMT = "microMiniStmt";

 public static String MICRO_BAL_ENQ = "microBalEnqIcici";
 public static String MICRO_CASH_WITHDRAW = "microCashWithdrawIcici";



 public static final String MICRO_CASH_WITHDRAW_SBM = "microCashWithdrawSbm";
 public static final String wsReversalMatmSbm = "wsReversalMatmSbm";

 public static final String wsReversal = "wsReversal";

 //sessionconstants
 public static final String sessionName = "MySession";
 public static final String LoginTPK = "75161CDC859467CBBCF7B33B9D54BF32";
 //public static final String LoginTPK = "64D1AC87FCD506AF1FE76E01F99214D9";//SSM
 public static final String wsget5513 = "get5513";
 //Card Type Mapping
 public static final String CURRENCY_NAME = "INR";


}
