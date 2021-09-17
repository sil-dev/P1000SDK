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

 /*   public static ArrayList<HistoryListItem> historyListItemList = new ArrayList<HistoryListItem>();

    public static ArrayList<HistoryListItem> getHistoryListItemList() {
        return historyListItemList;
    }

    public static void setHistoryListItemList(ArrayList<HistoryListItem> historyListItemList) {
         Constants.historyListItemList = historyListItemList;
    }
*/
    public static final String smsMobno = "9223212270";   //243
    // public static final String smsMobno = "9223185705";   //uat

   /**
    * transaction type
    */
   public static final String DEBIT = "sdksale";
 //  public static final String SDK_WITHDRAW_ICICI = "sdkwithdrawIcici";
   public static final String SDK_WITHDRAW_SBM = "microCashWithdrawSbm";
   //public static final String SDK_ENQUIRY_ICICI = "sdkenquiryIcici";
   public static final String SDK_ENQUIRY_SBM = "microBalEnqSbm";

   //Menu Names

    public static final String menuSaleByCash = "firstpasschange";
    public static final String menuCashOnPOS = "firstpasschange";
    public static final String menuSaleByCard = "firstpasschange";
    public static final String menuUpiPayment = "firstpasschange";
    public static final String menuVoid = "firstpasschange";
    public static final String menuPurchaseWithCashBack = "firstpasschange";
    public static final String menuStartDrawer = "firstpasschange";
    public static final String menuEndDrawer = "firstpasschange";
    public static final String menuPerformanceReport = "firstpasschange";
    public static final String menuPasswdMgmt = "firstpasschange";
    public static final String menuUserDetails = "firstpasschange";
    public static final String menuMenuMgmt = "firstpasschange";
    public static final String menuPinPadSettings = "firstpasschange";
    public static final String menuValueAddedService = "firstpasschange";
    public static final String menuVASReport = "firstpasschange";
    public static final String menuSettlementReport = "firstpasschange";
    public static final String menuSendLog = "firstpasschange";
    public static final String menuChatBox = "firstpasschange";
    public static final String menuUpdateApp = "firstpasschange";

    //request code
    public static final String wslogincode = "doLogin";
    public static final String wsfirstpasschangecode = "firstpasschange";
    public static final String wschangepass = "changepass";
    public static final String wsgetMainKey = "getSdkMainKey";
    public static final String wscardpay = "doCardPayment";

    public static final String MICRO_MINI_STMT = "microMiniStmt";

    public static final String OTHER_BANK_MICRO_BAL_ENQ = "microBalEnq";
    public static final String OTHER_BANK_MICRO_CASH_WITHDRAW = "microCashWithdraw";

    public static final String ICICI_BANK_MICRO_BAL_ENQ = "microBalEnqIcici";
    public static final String ICICI_CASH_WITHDRAW = "microCashWithdrawIcici";

    public static String MICRO_BAL_ENQ = "microBalEnqIcici";
    public static String MICRO_CASH_WITHDRAW = "microCashWithdrawIcici";

    public static String walletTopup = "walletTopup";


    public static final String SBM_AEPS_WALLET_WITHDRAW = "aepsWalletWithdrawSbm";
    public static final String SBM_AEPS_WALLET_BALANCE = "aepsWalletBalanceSbm";
    public static final String SBM_AEPS_COMM_WALLET_WITHDRAW = "aepsCommWalletWithdrawSbm";
    public static final String SBM_AEPS_COMM_WALLET_BALANCE = "aepsCommWalletBalanceSbm";
    public static final String MICRO_MINI_STMT_SBM = "microMiniStmtSbm";
    public static final String MICRO_BAL_ENQ_SBM = "microBalEnqSbm";
    public static final String MICRO_CASH_WITHDRAW_SBM = "microCashWithdrawSbm";
    public static final String wsReversalMatmSbm = "wsReversalMatmSbm";
    public static final String SBM_AEPS_TRANSACTION = "aepsTxnSbm";

    public static final String loadWalletReq = "loadWalletReq";

    public static final String settlementHistoryBankTrnsf = "settlementHistoryBankTrnsf";

    public static final String transactionSummaryReport = "getTxnSummaryReport";

    public static final String MATM_WALLET_WITHDRAW_SBM = "matmWalletWithdrawSbm";
    public static final String MATM_WALLET_BALANCE_SBM = "matmWalletBalanceSbm";
    public static final String MATM_COMM_WALLET_BALANCE_SBM = "matmCommWalletBalanceSbm";
    public static final String MATM_COMM_WALLET_WITHDRAW_SBM = "matmCommWalletWithdrawSbm";

    public static final String CASHOUT_WALLET_WITHDRAW = "cashoutWalletWithdraw";
    public static final String CASHOUT_WALLET_BALANCE_ENQ = "cashoutWalletBalance";

    public static final String AEPS_WALLET_WITHDRAW = "aepsWalletWithdraw";
    public static final String AEPS_WALLET_BALANCE = "aepsWalletBalance";

    public static final String getposWalletBalance = "posWalletBalance";
    public static final String posWalletWithdraw = "posWalletWithdraw";

    public static final String getMatmWalletBalance = "matmWalletBalance";
    public static final String matmWalletWithdraw = "matmWalletWithdraw";

    public static final String wspurchaseWithCashback = "purchaseWithCashback";
    public static final String getARQC = "getARQC";
    public static final String sendReceipt = "sendReceipt";
    public static final String wscashonpos = "cashOnPOS";
    public static final String wsforgotpasscode = "forgotpass";
    public static String uploadReceipt = "uploadReceipt";
    public static final String wsregstatus = "regstatus";
    public static final String otpverify = "otpverify";
    public static final String getPerformanceReport = "getPerformanceReport";
    public static final String getMDRReport = "getMDRReport";
    public static final String createSubMarchant = "createSubMarchant";
    public static final String drawerTrans = "drawerTrans";
    public static final String saleByCash = "saleByCash";
    public static final String transactionHistory = "transactionHistory";
    public static final String cashTrans_StartDrawer = "SD";
    public static final String cashTrans_EndDrawer = "ED";
    public static final String cashTrans_SaleByCash = "SC";
    public static final String deviceUserRegister = "deviceUserReg";
    public static final String wsPreAuth = "preAuthReq";
    public static final String wsRefund = "refund";
    public static final String wsVoid = "void";
    public static final String getVoidRec = "getVoidRec";
    public static final String wsReversal = "wsReversal";
    public static final String wsReversalMatm = "wsReversalMatm";
    public static final String getRefundConfirm = "getRefundPreConfirm";
    public static final String RefundConfirm = "getRefundConfirm";
    public static final String PreAuthList = "preAuthList";
    public static final String drawerTotalAmount = "getDrawerAmount";
    //sessionconstants
    public static final String sessionName = "MySession";
    public static final String LoginTPK = "75161CDC859467CBBCF7B33B9D54BF32";
    //public static final String LoginTPK = "64D1AC87FCD506AF1FE76E01F99214D9";//SSM
    public static final String PreAuthCompletion = "preAuthCompletion";
    public static final String getLog = "getLog";
    public static final String wsget5513 = "get5513";
    public static final String UPICheckStatus = "UPICheckStatus";
    public static final String UPIAutoCheckStatus = "UPIAutoCheckStatus";
    public static final String rechargeHistory = "rechargeHistory";

    public static final String rechargeWalletCard = "rechargeWalletWithCard";
    public static final String rechargeWalletCash = "rechargeWalletWithCash";
    public static final String getWalletBalance = "getWalletBalance";
    public static final String recharge = "recharge";
    public static final String FETCH_BILLER = "fetchBiller";

    public static final String DMT_VALIDATE = "dmtValidate";
    public static final String DMT_TX = "dmtTx";

    public static final String AEPS_BANK_DETAILS = "aepsBanks";
    public static final String AEPS_TRANSACTION = "aepsTxn";
    public static final String AEPS_STATUS_TRANSACTION = "aepsStatus";

    public static final String TPKTMK = "TPKTMK";
    public static final String INJECTKEY = "INJECTKEY"; // F : No inject , T : Inject

    //Card Type Mapping
    public static final String CARD_TYPE_RUPAY = "01";
    public static final String CARD_TYPE_VISA = "02";
    public static final String CARD_TYPE_MASTER = "03";
    public static final String CARD_TYPE_MESTRO = "04";
    public static final String CARD_TYPE_BFS = "05";
    public static final String CARD_TYPE_BNB = "06";

    public static final String CURRENCY_NAME = "INR";
    public static final String DEFAULT_SERVICE_BIN = "01000000000000000000000000000000000000000000000000";


    //Time Format Constant
    public static final String DATE_FORMAT_DMY = "dd/MMM/yyyy";
    public static final String DATE_FORMAT_YMD = "yyyyMMdd";
    /*public static final String DATE_FORMAT_YMD = "yyyy/MM/dd";*/
    /*public static final String DATE_FORMAT_DMY [[= "dd MMM yyyy";*/
    public static final String DATE_FORMAT_DMY_S = "dd/MM/yyyy";
    /*public static final String DATE_FORMAT_M_D_Y = "MM-dd-yyyy";*/
    public static final String DATE_FORMAT_DMY_TIME = "dd MMM yyyy HH:mm:ss";
    public static final String DATE_FORMAT_DMY_T = "dd/MMM/yyyy HH:mm";
    public static final String DATE_FORMAT_DMY_T_A = "dd/MMM/yyyy HH:mm:ss a";
    public static final String DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_DATE_PICKER = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String DATE_FORMAT_TIME = "HH:mm";
}
