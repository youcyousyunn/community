package com.ycs.community.basebo.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.util.Base64Utils;

public class XXTEA {
	public static final String XXTEA_KEY = "C54SZKFBK5A5MNG"; // 数据库密码加解密key

	/**
	 * Base64加密
	 * @param data
	 * @param key
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptWithBase64(String data, byte[] key, String encoding) throws UnsupportedEncodingException {
		return base64Encode(encrypt(data.getBytes(), key));
	}

	private static String base64Encode(byte[] data) {
		return Base64Utils.encodeToString(data);
	}
	
	public static byte[] encrypt(byte[] data, byte[] key) {
		return data.length == 0 ? data : toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
	}
	
	private static byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;
		if (includeLength) {
			int result = data[data.length - 1];
			if (result > n) {
				return null;
			}

			n = result;
		}

		byte[] arg4 = new byte[n];

		for (int i = 0; i < n; ++i) {
			arg4[i] = (byte) (data[i >>> 2] >>> ((i & 3) << 3) & 255);
		}

		return arg4;
	}
	
	public static int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;
		if (n < 1) {
			return v;
		} else {
			if (k.length < 4) {
				int[] z = new int[4];
				System.arraycopy(k, 0, z, 0, k.length);
				k = z;
			}

			int arg9 = v[n];
			int y = v[0];
			int delta = -1640531527;
			int sum = 0;

			int e;
			int p;
			for (int q = 6 + 52 / (n + 1); q-- > 0; arg9 = v[n] += (arg9 >>> 5 ^ y << 2) + (y >>> 3 ^ arg9 << 4)
					^ (sum ^ y) + (k[p & 3 ^ e] ^ arg9)) {
				sum += delta;
				e = sum >>> 2 & 3;

				for (p = 0; p < n; ++p) {
					y = v[p + 1];
					arg9 = v[p] += (arg9 >>> 5 ^ y << 2) + (y >>> 3 ^ arg9 << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ arg9);
				}

				y = v[0];
			}

			return v;
		}
	}
	
	private static int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (data.length & 3) == 0 ? data.length >>> 2 : (data.length >>> 2) + 1;
		int[] result;
		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}

		n = data.length;

		for (int i = 0; i < n; ++i) {
			result[i >>> 2] |= (255 & data[i]) << ((i & 3) << 3);
		}

		return result;
	}


	/**
	 * Base64解密
	 * @param data
	 * @param key
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static byte[] decryptWithBase64(String data, byte[] key, String encoding) throws IOException {
		return null != data && data.length() != 0 ? decrypt(base64Decode(data), key) : new byte[0];
	}
	
	public static byte[] decrypt(byte[] data, byte[] key) {
		return data.length == 0 ? data : toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
	}
	
	private static byte[] base64Decode(String data) throws IOException {
		return Base64Utils.decode(data.getBytes());
	}
	
	public static int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;
		if (n < 1) {
			return v;
		} else {
			if (k.length < 4) {
				int[] z = new int[4];
				System.arraycopy(k, 0, z, 0, k.length);
				k = z;
			}

			int y = v[0];
			int delta = -1640531527;
			int q = 6 + 52 / (n + 1);

			for (int sum = q * delta; sum != 0; sum -= delta) {
				int e = sum >>> 2 & 3;

				int p;
				int arg9;
				for (p = n; p > 0; --p) {
					arg9 = v[p - 1];
					y = v[p] -= (arg9 >>> 5 ^ y << 2) + (y >>> 3 ^ arg9 << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ arg9);
				}

				arg9 = v[n];
				y = v[0] -= (arg9 >>> 5 ^ y << 2) + (y >>> 3 ^ arg9 << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ arg9);
			}

			return v;
		}
	}
	
	
	public static String encryptWithBase64(byte[] data, byte[] key) {
		return base64Encode(encrypt(data, key));
	}

	public static byte[] decryptWithBase64(byte[] data, byte[] key) throws IOException {
		return data.length == 0 ? data : decrypt(base64Decode(data), key);
	}

	private static byte[] base64Decode(byte[] data) throws IOException {
		return Base64Utils.decode(data);
	}
	

	public static void main(String[] args) throws IOException {
		String pwd = "root";
		System.out.println("明文密码："+pwd);
		String encriptPwd = encryptWithBase64(pwd, XXTEA_KEY.getBytes(), null);
		System.out.println("加密后密码："+encriptPwd);
		String decryptPwd = new String(decryptWithBase64(encriptPwd, XXTEA_KEY.getBytes(), null));
		System.out.println("解密后密码："+decryptPwd);
	}
	
	

}
