package com.clientcore.client.struct;

import java.io.UnsupportedEncodingException;

public class BaseStruct {
	public final static String priKey = "308204BC020100300D06092A864886F70D0101010500048204A6308204A20201000282010100B50F167A48C3C3195A14CB0F76C73304E3D360C5E661153988E88E98AD03A594AE9EDB583D00B2C340D8F10F34F13E5DE00EBA65EBB30573A9FA0B21B2F150D00C3279F80FCCD6AB0389674704F4867174544D755304A72D45CB604898965DC52944314A5756E61EA29A41FD011862764CD9EF79C45E8B1F66294E5A98DE209ACA30509B41ADB0DCCB2E479F129ECBB4AE78A22E45084711F038D955EE55F3B71DDDE579FDA77DF0079AA4B883C7EAA8F4BB413D6F4583CE214C00BB5794A2ADEF7DFD90474B9C7E421A793A9B8DCEBF605A3F5ADCD3DAA5262D630910929800DEE549FD5EC63396B38B1412A4C574EB7AD572FF5C6696E0229A3397C5F5EDD5020111028201002A9A2368111F00BAABC8A83FDFB6665B80E6711F8180413AB6CD4EBA830FEAB9927FBB23D21E482DF123FC7C0C74FF9D9E21954528665BA2BE952FCBB1A23121E4C0952B4F0305556A3E72A74C75C547DF22E50C8C011846E33EE97A7E417F79AF5B56E450C92716446087FF4B8D445812152949D3DA029DF9EB99F7330716BA9C8D80A6F5EB19513321BED8971D1B57C2AD924C67D76180F8C02A8686FAF770A0F7215128C85418414F04198964B0BEED7743F041B6EE547E2E0D270B9E9E39B568CC6BC0EECBD1EB04E1D060F6ACE38960D39AEA58EBFA34A1038732548B517FE0B8C5760E780559E3EBC724501BF19256351F7CB8B2D711E0C830A1B2237102818100C25D50F3D7F7632EB97F080FB4E3A1DC9869DF2695C3B91CC14B61A65452CD2094BAC28F5C92DF029BDA16DC047AE688DE54B3EB8D582A5149120544D2B373109A8166FA31456431556614E40AFAC04EF7A55C4457B60AEA606379DF9C80FB129669BA15B1482B706D8F5D5E9B95D3DC5EB8B43095144E190BA698BA37359C1D02818100EE799CE1D48F2214F85FD476DB7F75A31AAD1542F5F12F910DBCC2F3DC588AF7DD08D551B3C139865630BC6FF76114F4A52BAC94CAA3E495BFF64390937F0F2811FF31CC220F5610C5DFA4A0F47AAFA960D95FC42123E4D3661D9A2ADE2A4CD3E9007EA077C08A0F87F30CA5AEDB2A4C6E2E5D08F54150B50AF8480EDF8B3B1902818100B6EE6A4EE9614E4A17FF16A55EF45C1AE9CD0E426ED653DED40AB642313EDF2DB9283EA50BD586990B279D0B4F82BADB2B9B03B08507AF5B9010F5E66BF4300FA079CA54E3142210506013A973BED31D255056D6E923CE09C42163A54800EC4DBABDDC50A6DA833CA359C149FBD84EED86537C69F5B8C1F974608FBE521456B1028180381C9D625021ADAA94CB410CE85A39CC0646D7D38529B0D6D60E4BFD24C98A1C34021413395AA41F9BCF3B65A39E5F489F559204E462CC5F78762E03E6783FCD316938E4BCB8507C6ACB35CB84D192BE712416888044AE4FDBCABADCE8FAE4E6912D4AF894A5C621C5A299906560BEA8926543112AA5F4DF4DE010F470D5775102818100A681A28F6B49F88393ED4D4195BAA27974A4DDC12B93FF493583DFAC2D685AE0ECDD2ED6C1E50ADD662A36F5EF08382A9BF21142D6DDE4731D5EF4E8698ACDB5377406B10004C34660842AA210AA3871BA23E577950EFF7D68C3A6B41BF14BAA5835CC13B38CEF0655882C7129ED86ABD7243BBB2110F17B64DFFF44C279F225";

	private static final String coding = "utf-8";

	// emHead
	public static final int emNone = 0x00;
	public static final int emNormalString = 0x01;
	public static final int emZip = 0x02;
	public static final int emAdler32 = 0x10;
	public static final int emNormal = 0x20;
	public static final int emEncrypt = 0x40;

	// emConfig
	protected static final int emMaxPackageType = 65535;
	protected static final int emMD5Buff = 32;
	protected static final int emClientID = 48;
	// HeadStx
	protected static final int emStx = 860805;
	// emFunID
	public static final int emFunNull = -1;
	public static final int emFunClosed = 0;
	public static final int emFunTimeout = 1;
	public static final int emFunError = 2;
	public static final int emFunConnected = 3;
	public static final int emFunLogin = 500;
	public static final int emFunHeart = 501;
	public static final int emFunLogout = 502;
	public static final int emFunServerTime = 503;

	protected static final int emFunBase = 1000;

	public static final int emMaxBuff = 8192;

	public static final int lenHead = 64;

	protected byte[] byteArrayData = new byte[emMaxBuff];

	public static byte[] IntToByteArray(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public static int ByteArrayToInt(byte[] bArr, int nIdex) {
		return (bArr[nIdex + 0] & 0xff) | ((bArr[nIdex + 1] << 8) & 0xff00) | ((bArr[nIdex + 2] << 24) >>> 8) | (bArr[nIdex + 3] << 24);
	}

	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	public static short getShort(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	public static boolean ByteArrayToBool(byte[] bArr, int nIndex) {
		return bArr[nIndex + 0] == 1 ? true : false;
	}

	public static byte[] BoolToByteArray(boolean bData) {
		byte[] b = new byte[1];
		b[0] = (byte) (bData ? 1 : 0);
		return b;
	}

	public static String ByteArraytoString(byte[] valArr, int nOffset, int maxLen) {
		String result = null;
		int nFind = 0;
		int MaxLen = valArr.length > maxLen ? maxLen : valArr.length;
		while (nFind < MaxLen) {
			if (valArr[nOffset + nFind] == 0) {
				break;
			}
			nFind++;
		}
		byte[] temp = new byte[nFind];
		System.arraycopy(valArr, nOffset, temp, 0, nFind);
		try {
			result = new String(temp, coding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static byte[] StringToByteArray(String str) {
		byte[] temp = null;
		try {
			temp = str.getBytes(coding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
	}

	public static byte[] float2byte(float f) {

		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);

		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}

		return dest;

	}

	public static float byte2float(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}

	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}

	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}
}
