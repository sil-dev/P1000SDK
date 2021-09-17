package com.example.youcloudp1000sdk.utils.emvdecoder;

import java.util.TreeMap;


public class BERTLV {

    public static TreeMap<String, String> parseTLV(String string) {
        TreeMap<String, String> tlvMap = new TreeMap<String, String>();
        byte[] tlvBytes = null;
        try {
            tlvBytes = DataConverter.hexStringToByteArray(string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String tagName = "";
        String tagValue = "";
        int i = 0;
        while (i < tlvBytes.length) {
            try {
                tagName = tagName + string.substring(2 * i, 2 * i + 2);
                if ((tlvBytes[i] & 0x1F) == 0x1F && tagName.length() < 4) {
                    i++;
                    continue;
                } else {
                    i++;
                    int len = tlvBytes[i];
                    i++;
                    tagValue = string.substring(i * 2, i * 2 + len * 2);
                    i = i + len;
                    tlvMap.put(tagName, tagValue);
                    tagName = "";
                    tagValue = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
        return tlvMap;
    }

    public static void main(String[] args) {
        /*TreeMap<String, String> tlvMap = BERTLV.parseTLV("DF3E0A110000F15CAD88000255DF3F08030C76D197E12563");
		System.out.println(tlvMap.get("DF3E"));
		System.out.println(tlvMap.get("DF3F"));
		//System.out.println("Tag : 5F2A\t"+"Value : "+tlvMap.get("5F2A"));
		//System.out.println(BERTLV.parseTLV("5F2A0203565F340101820278008407A0000001523010950508800480009A031612149C01009F02060000000500009F090200029F10080105A000038400009F1A0203569F1E08324B3534353336399F2608D0D1341E9C52D6049F2701809F3303E040C89F34034203009F3501229F360200129F3704268C3F179F4103000011"));
		System.out.println(tlvMap.toString().replace(',', '\n'));*/

        TreeMap<String, String> tlvMap = BERTLV.parseTLV("910A0B3BBF6B89DF18B10014");
        System.out.println(tlvMap.toString());
    }
}
