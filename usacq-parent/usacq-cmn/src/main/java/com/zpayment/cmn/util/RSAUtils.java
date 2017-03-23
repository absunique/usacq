package com.zpayment.cmn.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class RSAUtils {
	static {
		// Security.addProvider(new BouncyCastleProvider());
	}

	public static final String KEY_ALGORITHM_RSA = "RSA";

	public static KeyPair genRSAKey() throws Exception {
		SecureRandom random = new SecureRandom();
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");

		generator.initialize(2048, random);

		KeyPair pair = generator.generateKeyPair();
		return pair;
	}

	public static KeyStore loadKeyStore(String keyStore, String password)
			throws Exception {

		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(new FileInputStream(keyStore), password.toCharArray());
		return ks;
	}

	public static PrivateKey getPrivateKey(String keyStore, String alias,
			String password) throws Exception {
		KeyStore ks = loadKeyStore(keyStore, password);
		Key privateKey = ks.getKey(alias, password.toCharArray());
		return (PrivateKey) privateKey;
	}

	public static PrivateKey getPrivateKey(InputStream ins, String algorithm)
			throws Exception {
		if (ins == null || StringUtils.isEmpty(algorithm)) {
			return null;
		}
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		Base64Encoder encoder = new Base64Encoder();
		byte[] encodedKey = encoder.decode(getStreamAsString(ins, null));
		return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}

	public static PublicKey getPublicKey(String keyStore, String alias,
			String password) throws Exception {
		KeyStore ks = loadKeyStore(keyStore, password);
		Certificate cert = ks.getCertificate(alias);
		RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();
		return publicKey;
	}

	public static PublicKey getPublicKey(InputStream ins, String algorithm)
			throws Exception {
		if (ins == null || StringUtils.isEmpty(algorithm)) {
			return null;
		}
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		Base64Encoder encoder = new Base64Encoder();
		byte[] encodedKey = encoder.decode(getStreamAsString(ins, null));
		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

	}

	public static byte[] encRSA(byte[] input, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(input);
	}

	public static byte[] decRSA(byte[] input, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
		cipher.init(Cipher.DECRYPT_MODE, key, new SecureRandom());
		return cipher.doFinal(input);
	}

	// public static X509Certificate generateV1Certificate(KeyPair pair)
	// throws Exception {
	// generate the certificate
	// X509v3CertificateBuilder
	// X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
	//
	// certGen.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
	// certGen.setIssuerDN(new X500Principal("CN=Test Certificate"));
	// certGen.setNotBefore(new Date(System.currentTimeMillis() - 50000));
	// certGen.setNotAfter(new Date(System.currentTimeMillis() + 50000));
	// certGen.setSubjectDN(new X500Principal("CN=Test Certificate"));
	// certGen.setPublicKey(pair.getPublic());
	// certGen.setSignatureAlgorithm("SHA256WithRSAEncryption");
	//
	// return certGen.generateX509Certificate(pair.getPrivate(), "BC");
	// }

	public static void saveCert(KeyStore ks, String alias, Certificate cert,
			Key privateKey, String password) throws Exception {
		ks.setCertificateEntry(alias, cert);
		ks.setKeyEntry(alias, privateKey, password.toCharArray(),
				new Certificate[] { cert });
	}

	public static Certificate loadCert(String certPath) throws Exception {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		FileInputStream in = new FileInputStream(certPath);
		Certificate cert = cf.generateCertificate(in);
		in.close();
		X509Certificate t = (X509Certificate) cert;
		return t;
	}

	public static byte[] sign(byte[] input, PrivateKey privateKey,
			String algorithm) throws Exception {
		Signature s = Signature.getInstance(algorithm);
		s.initSign(privateKey);
		s.update(input);
		return s.sign();
	}

	public static boolean verifySign(byte[] input, byte[] sign,
			PublicKey publicKey, String algorithm) throws Exception {

		Signature s = Signature.getInstance(algorithm);
		s.initVerify(publicKey);
		s.update(input);
		return s.verify(sign);
	}

	public static void main(String[] args) throws Exception {
		//
		// Certificate c = loadCert("D:/develop/key/key3perm.cer");
		// System.out.println(StringUtils.convHexByteArrayToStr(c.getPublicKey()
		// .getEncoded()));
		// KeyStore ks = loadKeyStore("D:/develop/key/main.keystore", "123456");
		// // Key privateKey = getPrivateKey("D:/develop/key/main.keystore",
		// // "key3",
		// // "123456");
		// X509Certificate cert = (X509Certificate) ks.getCertificate("key3");
		// System.out.println(StringUtils.convHexByteArrayToStr(cert.getPublicKey()
		// .getEncoded()));
		// saveCert(ks, "key4", cert, privateKey, "123456");
		// KeyPair kp = genRSAKey();
		// System.out.println(kp.getPrivate().getEncoded().length);
		// System.out.println(kp.getPublic().getEncoded().length);
		// byte[] in = { 0x01, 0x02, 0x03, 0x01, 0x02, 0x03 };
		// byte[] encout = null;
		// // encout = encRSA(in, kp.getPublic());
		// byte[] decout = null;
		// = decRSA(encout, kp.getPrivate());
		// // System.out.println(StringUtils.convHexByteArrayToStr(encout));
		// System.out.println(StringUtils.convHexByteArrayToStr(decout));
		//
		// encout = encRSA(in, kp.getPrivate());
		// decout = decRSA(encout, kp.getPublic());
		// // System.out.println(StringUtils.convHexByteArrayToStr(encout));
		// System.out.println(StringUtils.convHexByteArrayToStr(decout));

		// PrivateKey privateKey = getPrivateKey("D:/develop/key/main.keystore",
		// "key3", "123456");
		// System.out.println(StringUtils.convHexByteArrayToStr(privateKey
		// .getEncoded()));
		// PublicKey publicKey = getPublicKey("D:/develop/key/main.keystore",
		// "key1", "123456");
		// System.out.println(StringUtils.convHexByteArrayToStr(publicKey
		// .getEncoded()));
		// publicKey = getPublicKey("D:/develop/key/main.keystore", "key2",
		// "123456");
		// System.out.println(StringUtils.convHexByteArrayToStr(publicKey
		// .getEncoded()));
		// publicKey = getPublicKey("D:/develop/key/main.keystore", "key3",
		// "123456");
		// System.out.println(StringUtils.convHexByteArrayToStr(publicKey
		// .getEncoded()));
		// byte[] sign = sign(new byte[] { 0x01, 0x02 }, privateKey,
		// "SHA1WithRSA");
		// boolean re = verifySign(new byte[] { 0x01, 0x02 }, sign, publicKey,
		// "SHA1WithRSA");
		// System.out.println(sign.length);
		// System.out.println(re);
		// KeyFactory kf = KeyFactory.getInstance("RSA");
		// PrivateKey privateKey = kf.generatePrivate(new
		// PKCS8EncodedKeySpec());
		// publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKey
		// .getEncoded()));
		// sign = sign(new byte[] { 0x01, 0x02 }, privateKey, "SHA1WithRSA");
		// re = verifySign(new byte[] { 0x01, 0x02 }, sign, publicKey,
		// "SHA1WithRSA");
		// System.out.println(sign.length);
		// System.out.println(re);
		// System.out.println(StringUtils.convHexByteArrayToStr(privateKey
		// .getEncoded()));
		// encout = encRSA(in, publicKey);
		//
		// decout = decRSA(encout, privateKey);
		// System.out.println(StringUtils.convHexByteArrayToStr(decout));
		// String publicKeyBase64 =
		// "AAAAB3NzaC1yc2EAAAADAQABAAACAQDeSxHs1ghLGGNwQwzHzCz7wYZb2/lICLKLoAaGKaxP7yNP5/746Ulw2C6Ks2yRCAvkcQYt3Lt6luhx6MxQLOguIcAkKuseuhYkOhw0TduPjLcHnFLzJ+8AUzfosavGFeQfN87g7JN5Bb7BdzDNPHzMy9xT1qrdYP0Z55wUIliJ0tIdZ2glUu9RlWu/tOqa9d3gAFHyLh8idbp5DBRw2HbfHaTrbbSSqFgYyGhPP4T72Qpu+TqNgzd36p3XGvmxTRK4fJlXmiGy6RwMrr2pnxn7gc35f2oOSMq2v3iNjqerG+Rd3Vcudh7ycfIBEAsx2r449d/8d7AqKzk2sf5ObTP0t24JqR/FKbBNBtZn/sIvdPdEkj86lxF5sTdXPUNO+K1YbGfi7zU0JacQ0LIbn7MTVIRuLZ3VBZHNO5FQDc5hQWZgs3oggiuhUtVhb03ZW2fW8Zs+NgjprQ9FRvMqG69IdWWdarbvgP+kXsrjT5/yDUnh7UobV1+A1A6HGMBftmIqTmEKVJ7AsEyEzu0FeuQzQ2C+TqOf3yKAsIN0VJ9ACJA8Wv+CrTqR6KCXDU2LETO5jb+Lw+19FVoDNS/dN64LRg3Qc+PfEsET3fG7TCvlMUQ9rPbDH6BtdITLadaIdzaErje0lRN4vcvp+hRzQA1yd38XZHp+ez6BZzc2VRdfKQ==";
		// sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		// // byte[] publicKeyByte = publicKeyBase64.getBytes();
		// byte[] publicKeyByte = decoder.decodeBuffer(publicKeyBase64);
		// System.out.println(StringUtils.convHexByteArrayToStr(publicKeyByte));
		// String privateKeyBase64 =
		// "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";
		// byte[] privateKeyByte = decoder.decodeBuffer(privateKeyBase64);
		// System.out.println(StringUtils.convHexByteArrayToStr(privateKeyByte));
		// testByteKey(publicKeyByte, privateKeyByte);
		// String ss = HttpUtils.doGetSSL("https://www.alipay.com/", null,
		// new HttpCfg(50000, 50000, 50000));
		// System.out.println(ss);
		// URL u = new URL("https://www.alipay.com/");
		// HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		// SSLContext context = getSSLContext(null, null, null);
		// conn.setSSLSocketFactory(context.getSocketFactory());
		//
		// conn.setRequestMethod("GET");
		// conn.setDoInput(true);
		// conn.setDoOutput(true);
		// conn.setRequestProperty("Accept",
		// "text/xml,text/javascript,text/html");
		// conn.setRequestProperty("User-Agent", "aop-sdk-java");
		// conn.setRequestProperty("Content-Type", "charset=utf-8");
		//
		// InputStream es = conn.getErrorStream();
		// if (es == null) {
		// String ss = getStreamAsString(conn.getInputStream(), "utf-8");
		// System.out.println(ss);
		// } else {
		// String msg = getStreamAsString(es, "utf-8");
		// if (StringUtils.isEmpty(msg)) {
		// throw new IOException(conn.getResponseCode() + ":"
		// + conn.getResponseMessage());
		// } else {
		// throw new IOException(msg);
		// }
		// }
	}

	private static String getStreamAsString(InputStream stream, String charset)
			throws IOException {
		BufferedReader reader = null;
		try {
			reader = charset == null ? new BufferedReader(
					new InputStreamReader(stream)) : new BufferedReader(
					new InputStreamReader(stream, charset));
			StringWriter writer = new StringWriter();

			char[] chars = new char[8192];
			int count = 0;
			while ((count = reader.read(chars)) > 0) {
				writer.write(chars, 0, count);
			}

			return writer.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

	public static void testByteKey(byte[] publicKeyByte, byte[] privateKeyByte)
			throws Exception {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(
				privateKeyByte));
		PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(
				publicKeyByte));
		byte[] input = new byte[] { 0x01, 0x02 };
		byte[] enc = encRSA(input, publicKey);
		byte[] dec = decRSA(enc, privateKey);
		System.out.println(StringUtils.convHexByteArrayToStr(input));
		System.out.println(StringUtils.convHexByteArrayToStr(enc));
		System.out.println(StringUtils.convHexByteArrayToStr(dec));
	}

	public static SSLContext getSSLContext(String password,
			String keyStorePath, String trustStorePath) throws Exception {
		// 实例化SSL上下文
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.getClientSessionContext().setSessionTimeout(15);
		ctx.getClientSessionContext().setSessionCacheSize(1000);
		if (password == null) {

			ctx.init(new KeyManager[0],
					new TrustManager[] { new X509TrustManager() {

						@Override
						public void checkClientTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							// TODO Auto-generated method stub

						}

						@Override
						public void checkServerTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							// TODO Auto-generated method stub

						}

						@Override
						public X509Certificate[] getAcceptedIssuers() {
							// TODO Auto-generated method stub
							return null;
						}
					} }, new SecureRandom());

		} else {
			// 实例化密钥库
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
					.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			// 获得密钥库
			KeyStore keyStore = loadKeyStore(keyStorePath, password);
			// 初始化密钥工厂
			keyManagerFactory.init(keyStore, password.toCharArray());

			// 实例化信任库
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			// 获得信任库
			KeyStore trustStore = loadKeyStore(trustStorePath, password);
			// 初始化信任库
			trustManagerFactory.init(trustStore);
			// 初始化SSL上下文
			ctx.init(keyManagerFactory.getKeyManagers(),
					trustManagerFactory.getTrustManagers(), new SecureRandom());
		}
		// 获得SSLSocketFactory
		return ctx;
	}
}
