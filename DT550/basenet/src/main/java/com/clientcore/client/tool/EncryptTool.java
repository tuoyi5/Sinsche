package com.clientcore.client.tool;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptTool {

	private static final String rsa = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
	private static final String aes = "AES/CFB/NoPadding";

	public byte[] EncodeAES(byte[] byteContent, String password) {
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(aes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ???建�????????
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(password.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ???�????
		catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(byteContent);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new byte[1];
	}

	public byte[] DecodeAES(byte[] strContent, String password) {
		if (strContent == null) {
			return null;
		}
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
		Cipher cipher = null;
		try {// AES/CFB/NoPadding
			cipher = Cipher.getInstance(aes);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ???建�????????
		try {
			IvParameterSpec ivSpec = new IvParameterSpec(password.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// ???�????
		catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return cipher.doFinal(strContent);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String MD5(byte[] source) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(source);
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			return buf.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String decryptByPrivateKey(String data, String strprivateKey) {
		PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(decode(strprivateKey));
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Key privateKey = null;
		try {
			privateKey = keyFactory.generatePrivate(pkcs8);
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (privateKey != null) {
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance(rsa);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				cipher.init(Cipher.DECRYPT_MODE, privateKey);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				return new String(cipher.doFinal(decode(data)));
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private final char[] ENC_TAB = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	// decoding characters table.
	private final byte[] DEC_TAB = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 16
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 32
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 48
			0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 64

			0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 80
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 96
			0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 112
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };

	/**
	 * encode byte array data to base 16 hex string.
	 * 
	 * @param data
	 *            byte array data.
	 * @return base 16 hex string.
	 */
	private String encode(byte[] data) {
		return encode(data, 0, data.length);
	}

	/**
	 * encode byte array data from offset to offset+length to base 16 hex
	 * string.
	 * 
	 * @param data
	 *            byte array data.
	 * @param offset
	 *            start index, included.
	 * @param length
	 *            total encode length.
	 * @return the base 16 hex string.
	 */
	private String encode(byte[] data, int offset, int length) {
		StringBuffer buff = new StringBuffer(length * 2);
		int i = offset, total = offset + length;
		while (i < total) {
			buff.append(ENC_TAB[(data[i] & 0xF0) >> 4]);
			buff.append(ENC_TAB[data[i] & 0x0F]);
			i++;
		}

		return buff.toString();
	}

	/**
	 * decode base 16 hex string to byte array.
	 * 
	 * @param hex
	 *            base 16 hex string.
	 * @return byte array data.
	 */
	private byte[] decode(String hex) {
		byte[] data = new byte[hex.length() / 2];
		decode(hex, data, 0);
		return data;
	}

	/**
	 * decode base 16 hex string to byte array.
	 * 
	 * @param hex
	 *            base 16 hex string.
	 * @param data
	 *            byte array data.
	 * @param offset
	 *            byte array data start index, included.
	 */
	private void decode(String hex, byte[] data, int offset) {
		int i = 0, total = (hex.length() / 2) * 2, idx = offset;
		while (i < total) {
			data[idx++] = (byte) ((DEC_TAB[hex.charAt(i++)] << 4) | DEC_TAB[hex.charAt(i++)]);
		}
	}
}
