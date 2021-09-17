package com.example.youcloudp1000sdk.retrofit;



import com.example.youcloudp1000sdk.model.P1000Request;

public class RequestParams {
    private String requestcode;
    private String username;
    private String password;
    public String sessionId;
    private String verCode;
    private String verName;
    private String newpass;
    private String srno;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String ksn;
    private String carddata;
    private String pindata;
    private String op;
    private String token;
    private String amt;
    private String tipamt;
    private String cardno;
    private String fromdate;
    private String todate;
    private String mobileno;
    private String lpin;
    private String imei;
    private String imsi;
    private String companyid;
    private String paymenttype;
    private String remark;
    private String reader;
    private String tag55;
    private String merchantId;
    private String mid;
    private String subMerUserId;
    private String fname;
    private String lname;
    private String receiptTo;
    private String receipt;
    private String img;
    private String rrn;
    private String log;
    private String channelID;
    private String posLocation;
    private String modeOfPayment;
    private String rechargetype;
    private String operator;
    private String rechargeType;

    //newly added for DMT
    private String benAcctNo;
    private String benIfsc;

    public RequestParams(){}

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getModeOfPayment() {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        this.modeOfPayment = modeOfPayment;
    }

    public String getRechargetype() {
        return rechargetype;
    }

    public void setRechargetype(String rechargetype) {
        this.rechargetype = rechargetype;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getPosLocation() {
        return posLocation;
    }

    public void setPosLocation(String posLocation) {
        this.posLocation = posLocation;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    private String invoiceNo;

    public String getCashBackAmt() {
        return cashBackAmt;
    }

    public void setCashBackAmt(String cashBackAmt) {
        this.cashBackAmt = cashBackAmt;
    }

    private String cashBackAmt;

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getRequestcode() {
        return requestcode;
    }

    public void setRequestcode(String requestcode) {
        this.requestcode = requestcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getKsn() {
        return ksn;
    }

    public void setKsn(String ksn) {
        this.ksn = ksn;
    }

    public String getCarddata() {
        return carddata;
    }

    public void setCarddata(String carddata) {
        this.carddata = carddata;
    }

    public String getPindata() {
        return pindata;
    }

    public void setPindata(String pindata) {
        this.pindata = pindata;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getLpin() {
        return lpin;
    }

    public void setLpin(String lpin) {
        this.lpin = lpin;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getTag55() {
        return tag55;
    }

    public void setTag55(String tag55) {
        this.tag55 = tag55;
    }


    public String getNewpass() {
        return newpass;
    }

    public void setNewpass(String newpass) {
        this.newpass = newpass;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getSubMerUserId() {
        return subMerUserId;
    }

    public void setSubMerUserId(String subMerUserId) {
        this.subMerUserId = subMerUserId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getReceiptTo() {
        return receiptTo;
    }

    public void setReceiptTo(String receiptTo) {
        this.receiptTo = receiptTo;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getTipamt() {
        return tipamt;
    }

    public void setTipamt(String tipamt) {
        this.tipamt = tipamt;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getVerName() {
        return verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBenAcctNo() {
        return benAcctNo;
    }

    public void setBenAcctNo(String benAcctNo) {
        this.benAcctNo = benAcctNo;
    }

    public String getBenIfsc() {
        return benIfsc;
    }

    public void setBenIfsc(String benIfsc) {
        this.benIfsc = benIfsc;
    }


    @Override
    public String toString() {
        return "RequestParams{" +
                "requestcode='" + requestcode + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", verCode='" + verCode + '\'' +
                ", verName='" + verName + '\'' +
                ", newpass='" + newpass + '\'' +
                ", srno='" + srno + '\'' +
                ", ksn='" + ksn + '\'' +
                ", carddata='" + carddata + '\'' +
                ", pindata='" + pindata + '\'' +
                ", op='" + op + '\'' +
                ", token='" + token + '\'' +
                ", amt='" + amt + '\'' +
                ", tipamt='" + tipamt + '\'' +
                ", cardno='" + cardno + '\'' +
                ", fromdate='" + fromdate + '\'' +
                ", todate='" + todate + '\'' +
                ", mobileno='" + mobileno + '\'' +
                ", lpin='" + lpin + '\'' +
                ", imei='" + imei + '\'' +
                ", imsi='" + imsi + '\'' +
                ", companyid='" + companyid + '\'' +
                ", paymenttype='" + paymenttype + '\'' +
                ", remark='" + remark + '\'' +
                ", reader='" + reader + '\'' +
                ", tag55='" + tag55 + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", mid='" + mid + '\'' +
                ", subMerUserId='" + subMerUserId + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", receiptTo='" + receiptTo + '\'' +
                ", receipt='" + receipt + '\'' +
                ", img='" + img + '\'' +
                ", rrn='" + rrn + '\'' +
                ", log='" + log + '\'' +
                ", channelID='" + channelID + '\'' +
                ", posLocation='" + posLocation + '\'' +
                ", modeOfPayment='" + modeOfPayment + '\'' +
                ", rechargetype='" + rechargetype + '\'' +
                ", operator='" + operator + '\'' +
                ", rechargeType='" + rechargeType + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", cashBackAmt='" + cashBackAmt + '\'' +
                '}';
    }


    public RequestParams(P1000Request p1000Request) {
        username = p1000Request.getUsername();
        password = p1000Request.getPassword();
        companyid = p1000Request.getRefCompany();
        mid = p1000Request.getMid();
        srno = p1000Request.getTid();
        imei = p1000Request.getImei();
        imsi = p1000Request.getImsi();
        amt = p1000Request.getTxn_amount();
        remark = p1000Request.getRemark();
        rrn = p1000Request.getTransactionId();
        requestcode = p1000Request.getRequestCode().getCode();

        sessionId = p1000Request.getSession_Id();
    }

}
