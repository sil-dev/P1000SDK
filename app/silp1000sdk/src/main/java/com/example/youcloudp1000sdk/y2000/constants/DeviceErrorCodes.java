package com.example.youcloudp1000sdk.y2000.constants;

import com.pos.sdk.emvcore.PosEmvErrCode;

public class DeviceErrorCodes {
    //old
   /* public static final int INVALID_PIN = 65535;
    public static final int CHIP_REMOVE_BEFORE_TC = -2;
    public static final int DECLINEDBY_DEVICE = -11;
    public static final int CARD_READ_TIMEOUT = -8;
    public static final int BLOCKED_CARD = -3;
    public static final int EMV_APP_BLOCKED = 65284;
    public static final String FALLBACK = "65281";*/

    public static final int INVALID_PIN = 25538;
    public static final int CHIP_REMOVE_BEFORE_TC = -2;
    public static final int DECLINEDBY_DEVICE = -11;
    public static final int CARD_READ_TIMEOUT = -8;
    public static final int BLOCKED_CARD = -3;
    public static final int EMV_APP_BLOCKED = -5;
    public static final int FALLBACK = PosEmvErrCode.ERR_NEED_OTHERCHANL

            ;
}
