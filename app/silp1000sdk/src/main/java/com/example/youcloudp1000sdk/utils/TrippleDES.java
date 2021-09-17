package com.example.youcloudp1000sdk.utils;

import com.bw.jni.util.tlv.HexUtil;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TrippleDES {
	
	private static final String DESede = "DESede";
	
	public static enum MODE{
		ECB, CBC
	}
	
	public static enum PADDING{
		NoPadding, PKCS5Padding
	}
	
	public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		if(key.length == 16) key = concat(key, 0, 16, key, 0, 8);
		Cipher cipher = Cipher.getInstance("DESede");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, DESede));
		return cipher.doFinal(data);		
	}

	public static byte[] encrypt(byte[] data, byte[] key, MODE mode, PADDING padding) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		if(key.length == 16) key = concat(key, 0, 16, key, 0, 8);
		Cipher cipher = Cipher.getInstance("DESede/"+mode+"/"+padding);
		if(mode == MODE.CBC) cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, DESede),new IvParameterSpec(new byte[8]));
		else cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, DESede));
		return cipher.doFinal(data);		
	}
	
	public static byte[] decrypt(byte[] data, byte[] key, MODE mode, PADDING padding) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException
	{
		if(key.length == 16) key = concat(key, 0, 16, key, 0, 8);
		Cipher cipher = Cipher.getInstance("DESede/"+mode+"/"+padding);
		if(mode == MODE.CBC) cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, DESede),new IvParameterSpec(new byte[8]));
		else cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, DESede));
		return cipher.doFinal(data);		
	}
	
	public static byte[] concat(byte[] src1, int src1Pos, int src1Len, byte[] src2, int src2Pos, int src2Len)
	{
		byte[] dest = new byte[src1Len+src2Len];
		System.arraycopy(src1, src1Pos, dest, 0, src1Len);
		System.arraycopy(src2, src2Pos, dest, src1Len, src2Len);
		return dest;
	}




	private static byte[] hexStringToByteArray(String hexString)
	{
		if(hexString.length()%2 != 0) hexString = "0"+hexString;
		final int len = hexString.length();

		byte[] out = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			int h = hexToBin(hexString.charAt(i));
			int l = hexToBin(hexString.charAt(i + 1));
			if (h == -1 || l == -1) {
				throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexString);
			}
			out[i / 2] = (byte) (h * 16 + l);
		}
		return out;
	}

	private static int hexToBin(char ch) {
		if ('0' <= ch && ch <= '9') {
			return ch - '0';
		}
		if ('A' <= ch && ch <= 'F') {
			return ch - 'A' + 10;
		}
		if ('a' <= ch && ch <= 'f') {
			return ch - 'a' + 10;
		}
		return -1;
	}

}
