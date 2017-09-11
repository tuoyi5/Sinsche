package com.clientcore.client.tool;

public class Base16 {

	/**
	 * ���ֽ����ݽ���Base16���롣
	 * 
	 * @param src
	 *            Դ�ֽ�����
	 * @return �������ַ���
	 */
	public static String encode(byte src[]) throws Exception {
		StringBuffer strbuf = new StringBuffer(src.length * 2);
		int i;

		for (i = 0; i < src.length; i++) {
			if (((int) src[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) src[i] & 0xff, 16));
		}

		return strbuf.toString();
	}

	/**
	 * ��Base16������ַ������н��롣
	 * 
	 * @param src
	 *            Դ�ִ�
	 * @return �������ֽ�����
	 */
	public static byte[] decode(String hexString) throws Exception {
		byte[] bts = new byte[hexString.length() / 2];
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
		}
		return bts;
	}
}
