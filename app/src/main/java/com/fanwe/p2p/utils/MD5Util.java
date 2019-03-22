package com.fanwe.p2p.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Util {
	
	/**
	 * md5�����㷨
	 * 
	 * @return
	 */
	public static String getMD5(String str) {
		StringBuffer buf = new StringBuffer();
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			byte bytes[] = md5.digest();
			
			for (int i = 0; i < bytes.length; i++) {
				String s = Integer.toHexString(bytes[i] & 0xff);
				if (s.length() == 1) {
					buf.append("0");
				}
				buf.append(s);
			}
			
		} catch (Exception ex) {
			
		}
		
		return buf.toString();

	}
	
	////////////////��¼ǰ��������
	public static int[] DE_KEY;
	public static int[] EN_KEY;

	static {
		int[] arrayOfInt1 = new int[8];
		arrayOfInt1[0] = 7;
		arrayOfInt1[1] = 2;
		arrayOfInt1[2] = 5;
		arrayOfInt1[3] = 4;
		arrayOfInt1[5] = 1;
		arrayOfInt1[6] = 3;
		arrayOfInt1[7] = 6;
		EN_KEY = arrayOfInt1;
		int[] arrayOfInt2 = new int[8];
		arrayOfInt2[0] = 4;
		arrayOfInt2[1] = 5;
		arrayOfInt2[2] = 1;
		arrayOfInt2[3] = 6;
		arrayOfInt2[4] = 3;
		arrayOfInt2[5] = 2;
		arrayOfInt2[6] = 7;
		DE_KEY = arrayOfInt2;
	}

	public static byte byteDecryption(byte nSrc) {
		byte nDst = 0;
		byte nBit = 0;
		int i;
		for (i = 0; i < 8; i++) {
			nBit = (byte) (1 << DE_KEY[i]);
			if ((nSrc & nBit) != 0)
				nDst |= (1 << i);
		}
		return nDst;
	}

	public static byte byteEncryption(byte nSrc) {
		byte nDst = 0;
		byte nBit = 0;
		int i;
		for (i = 0; i < 8; i++) {
			nBit = (byte) (1 << EN_KEY[i]);
			if ((nSrc & nBit) != 0)
				nDst |= (1 << i);
		}
		return nDst;
	}

	/**
	 * ���ܣ����ؼ��ܺ���ַ�
	 * 
	 * @param source
	 *            ԭ�ַ�
	 * @return ���ܺ������ֵ���ַ�48.12.30��
	 * @throws UnsupportedEncodingException
	 */
	public static String getCipherString(String source) {
		if (source.trim().equals("")) {
			return "";
		}
		String s = source;
		byte[] sb;
		try {
			sb = s.getBytes("UTF-8");
		
		String d = new String(sb, "UTF-8");
		sb = d.getBytes("UTF-8");
		byte[] sbNew = new byte[sb.length];
		StringBuilder sbb = new StringBuilder();

		for (int i = 0; i < sb.length; i++) {
			byte t = byteEncryption(sb[i]);

			sbNew[i] = t;
			char c = (char) t;
			sbb.append(c);
		}
		// String ss=new String(sbb.toString().getBytes("UTF-8"),"UTF-8");
		return sbb.toString();
		} catch (UnsupportedEncodingException e) {
		
			return source;

		}

	}

	@Deprecated
	public static String getCipherStringForPerference(String source) throws UnsupportedEncodingException {
		byte[] arrayByteOfSource = source.getBytes("UTF-8");
		StringBuilder mStringBuildedr = new StringBuilder();
		int i = 0;
		while (true) {
			int j = arrayByteOfSource.length;
			if (i >= j) {
				byte[] arrayOfByte2 = mStringBuildedr.toString().getBytes();
				return new String(arrayOfByte2);
			}
			int k = byteEncryption(arrayByteOfSource[i]);
			StringBuilder localStringBuilder2 = mStringBuildedr.append(k);
			StringBuilder localStringBuilder3 = mStringBuildedr.append(",");
			i += 1;
		}
	}

	/**
	 * ���ܣ�����ԭ�ȱ���Ķ�����ֵ�ַ�����ԭ�ַ�
	 * 
	 * @param cipherString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getOriginString(String cipherString){
		if (cipherString.trim().equals("")) {
			return "";
		}
		String drr = cipherString;

		byte[] drrByte = new byte[drr.length()];
		for (int i = 0; i < drrByte.length; i++) {
			drrByte[i] = byteDecryption(Byte.valueOf((byte) drr.charAt(i)));
		}

		String des;
		try {
			des = new String(drrByte, "UTF-8");
			return des;
		} catch (UnsupportedEncodingException e) {
			 return cipherString;
		}
	
	}

//	public static String getMD5(String content) {
//        String result = null;
//        try {
//            MessageDigest md = MessageDigest.getInstance("md5");
//            md.update(content.getBytes("utf8"));
//            byte[] md5Result = md.digest();
//            result = parseByte2HexStr(md5Result);
//        } catch (Exception ex) {
////            logger.error("encrypt md5�����쳣",ex);
//        }
//        return result;
//    }
//	
//	private static String parseByte2HexStr(byte buf[]) {
//        StringBuilder sb = new StringBuilder(32);
//        for (int i = 0; i < buf.length; i++) {
//            String hex = Integer.toHexString(buf[i] & 0xFF);
//            if (hex.length() == 1) {
//                hex = '0' + hex;
//            }
//            sb.append(hex.toUpperCase());
//        }
//        return sb.toString();
//    }
	
	

}
