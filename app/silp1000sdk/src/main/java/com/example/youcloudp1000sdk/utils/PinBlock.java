package com.example.youcloudp1000sdk.utils;

import android.util.Log;

import java.math.BigInteger;
import java.util.Random;


public class PinBlock {

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * @param PIN & PAN
     * @return (0 + PIN.length + PIN + FFFFFFFFFF) ^ 0000+Rightmost 12 digits of PAN excluding check digit
     */
    public static String getISO0PinBlock(String PIN, String PAN) {
        String formatedPIN = String.format("%-16s", "0" + Integer.toString(PIN.length(), 16) + PIN).replace(' ', 'F');
        String formatedPAN = "0000" + PAN.substring(3, 15);
        return xor(formatedPIN, formatedPAN);
    }

    /**
     * @param PIN
     * @return 1+PIN.length+PIN+Randon Hex String
     */
    public static String getISO1PinBlock(String PIN) {
        String formatedPIN = "1" + Integer.toHexString(PIN.length()) + PIN;
        return formatedPIN + randomHexString(16 - formatedPIN.length());
    }

    /**
     * @param PIN & PAN
     * @return (3 + PIN.length + PIN + Randon Hex String) ^ (Rightmost 12 digits of PAN excluding check digit)
     */
    public static String getISO3PinBlock(String PIN, String PAN) {
        String formatedPIN = "3" + Integer.toHexString(PIN.length()) + PIN;
        System.out.println(formatedPIN);
        formatedPIN = formatedPIN + randomHexString(16 - formatedPIN.length());
        String formatedPAN = "0000" + PAN.substring(3, 15);
        return xor(formatedPAN, formatedPIN);
    }

    /**
     * @param PIN
     * @return PIN+"FFFFFFFFFFFF"
     */
    public static String getIBM3624PinBlock(String PIN) {
        return String.format("%-16s", PIN).replace(' ', 'F');
    }

    public static String getISO1Pinblock_TPK(String PIN, String TPK) {
        Log.d("PIN", "" + PIN);
        String PINBLOCK = getISO1PinBlock(PIN);
        try {
            return byteArrayToHexString(TrippleDES.encrypt(hexStringToByteArray(PINBLOCK), hexStringToByteArray(TPK), TrippleDES.MODE.ECB, TrippleDES.PADDING.NoPadding));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        //C41EA69CD1D3CBB2
        System.out.println(getISO1Pinblock_TPK("8735", "" + Constants.LoginTPK));

    }

    public static String byteArrayToHexString(byte[] data) {
        StringBuilder s = new StringBuilder(data.length * 2);
        for (byte b : data) {
            s.append(hexCode[(b >> 4) & 0xF]);
            s.append(hexCode[(b & 0xF)]);
        }
        return s.toString();
    }

    private static byte[] hexStringToByteArray(String hexString) {
        if (hexString.length() % 2 != 0) hexString = "0" + hexString;
        byte[] byteArray = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            int h = hexToByte(hexString.charAt(i));
            int l = hexToByte(hexString.charAt(i + 1));
            if (h == -1 || l == -1)
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexString);
            byteArray[i / 2] = (byte) (h * 16 + l);
        }
        return byteArray;
    }

    private static int hexToByte(char ch) {
        if ('0' <= ch && ch <= '9') return ch - '0';
        if ('A' <= ch && ch <= 'F') return ch - 'A' + 10;
        if ('a' <= ch && ch <= 'f') return ch - 'a' + 10;
        return -1;
    }

    private static String randomHexString(int len) {
        byte[] randomBytes = new byte[len / 2 + 1];
        new Random().nextBytes(randomBytes);
        return byteArrayToHexString(randomBytes).substring(0, len);
    }

    private static String xor(String string1, String string2) {
        return String.format("%" + string1.length() + "s", new BigInteger(string1, 16).xor(new BigInteger(string2, 16)).toString(16)).replace(' ', '0').toUpperCase();
    }
}
