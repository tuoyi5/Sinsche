package com.clientcore.client.tool;

public class Adler32 {
	private static final long BASE = 65521;

	private int byte2int(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) | ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public int update(byte[] input) {
		byte[] hash = new byte[4];
		long s1 = 1;
		long s2 = 0;

		int length = input.length;

		if (length % 8 != 0) {
			int nIndex = 0;
			do {
				s1 += input[nIndex] & 0xff;
				s2 += s1;
				nIndex++;
				length--;
			} while (length % 8 != 0);

			if (s1 >= BASE) {
				s1 -= BASE;
			}
			s2 %= BASE;
		}

		while (length > 0) {
			byte[] inputTemp = new byte[8];
			System.arraycopy(input, input.length - length, inputTemp, 0, inputTemp.length);
			s1 += inputTemp[0] & 0xff;
			s2 += s1;
			s1 += inputTemp[1] & 0xff;
			s2 += s1;
			s1 += inputTemp[2] & 0xff;
			s2 += s1;
			s1 += inputTemp[3] & 0xff;
			s2 += s1;
			s1 += inputTemp[4] & 0xff;
			s2 += s1;
			s1 += inputTemp[5] & 0xff;
			s2 += s1;
			s1 += inputTemp[6] & 0xff;
			s2 += s1;
			s1 += inputTemp[7] & 0xff;
			s2 += s1;

			length -= 8;

			if (s1 >= BASE) {
				s1 -= BASE;
			}
			if (length % 0x8000 == 0) {
				s2 %= BASE;
			}
		}

		hash[3] = (byte) (s1);
		hash[2] = (byte) (s1 >> 8);
		hash[1] = (byte) (s2);
		hash[0] = (byte) (s2 >> 8);
		return (int) byte2int(hash);
	}
}
