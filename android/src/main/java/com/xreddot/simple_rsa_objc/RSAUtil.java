package com.xreddot.simple_rsa_objc;

import android.util.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加解密工具类
 */
public class RSAUtil {
	private static final String CHARSET			= "UTF-8";
	private static final String RSA_ALGORITHM	= "RSA";
	public  static final String KEY_ALGORITHM	= "RSA/ECB/PKCS1Padding";

	/**
	 * 得到公钥
	 * @param publicKey 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private static RSAPublicKey getPublicKey(String publicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		//通过X509编码的Key指令获得公钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.DEFAULT));
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		return key;
	}

	/**
	 * 得到私钥
	 * @param privateKey 密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	private static RSAPrivateKey getPrivateKey(String privateKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		//通过PKCS#8编码的Key指令获得私钥对象
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		return key;
	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
		int maxBlock = 0;
		if (opmode == Cipher.DECRYPT_MODE) {
			maxBlock = keySize / 8;
		} else {
			maxBlock = keySize / 8 - 11;
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] buff;
			int i = 0;
			try {
				while (datas.length > offSet) {
					if (datas.length - offSet > maxBlock) {
						buff = cipher.doFinal(datas, offSet, maxBlock);
					} else {
						buff = cipher.doFinal(datas, offSet, datas.length - offSet);
					}
					out.write(buff, 0, buff.length);
					i++;
					offSet = i * maxBlock;
				}
			} catch (Exception e) {
				throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
			}
			byte[] resultDatas = out.toByteArray();
			return resultDatas;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 生成密钥对
	 * @return
	 */
	public static Map<String, String> createKeys() {
		//为RSA算法创建一个KeyPairGenerator对象
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
		}

		//初始化KeyPairGenerator对象,密钥长度
		kpg.initialize(1024);

		//生成密钥对
		KeyPair keyPair = kpg.generateKeyPair();

		//得到公钥
		Key publicKey = keyPair.getPublic();
		//		String publicKeyStr = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
		String publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);

		//得到私钥
		Key privateKey = keyPair.getPrivate();
		//		String privateKeyStr = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
		String privateKeyStr = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
		//包装
		Map<String, String> keyPairMap = new HashMap<String, String>();
		keyPairMap.put("publicKey", publicKeyStr);
		keyPairMap.put("privateKey", privateKeyStr);

		return keyPairMap;
	}

	/**
	 * 公钥加密
	 * @param data
	 * @param publicKey
	 * @return
	 */
	public static String publicEncrypt(String data, String publicKey) {
		try{
			RSAPublicKey key = getPublicKey(publicKey);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return Base64.encodeToString(
					rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), key.getModulus().bitLength()),
					Base64.DEFAULT);
		}catch (Exception e) {
			return "";
		}
	}

	/**
	 * 私钥解密
	 * @param data
	 * @param privateKey
	 * @return
	 */
	public static String privateDecrypt(String data, String privateKey) {
		try{
			RSAPrivateKey key = getPrivateKey(privateKey);
			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decode(data, Base64.DEFAULT),
					key.getModulus().bitLength()), CHARSET);
		}catch (Exception e){
			return "";
		}
	}
	
	public static void main(String[] args) {
		Map<String, String> map = createKeys();
		System.err.println("private>>"+map.get("privateKey"));
		System.err.println("public>>"+map.get("publicKey"));
	}
}
