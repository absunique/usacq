/*
 *  Copyright 2017, ZPayment Co., Ltd.  All right reserved.
 *
 *  THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO.,
 *  LTD.  THE CONTENTS OF THIS FILE MAY NOT BE DISCLOSED TO THIRD
 *  PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART,
 *  WITHOUT THE PRIOR WRITTEN PERMISSION OF ZPayment CO., LTD.
 *     2017-3-12 - Create By szwang
 */
package com.zpayment.cmn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * HTTP工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class HttpUtils {

	/** 缺省字符集 */
	public static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * HTTP连接请求配置
	 */
	private static Map<String, RequestConfig> requestConfigs;

	private static PoolingHttpClientConnectionManager cm;

	private static HttpClientBuilder builder;

	static {
		RegistryBuilder<ConnectionSocketFactory> r;
		try {
			r = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", new PlainConnectionSocketFactory())
					.register("https", createSSLConnSocketFactory());
			requestConfigs = new HashMap<String, RequestConfig>();
			cm = new PoolingHttpClientConnectionManager(r.build());
			cm.setMaxTotal(200);
			cm.setDefaultMaxPerRoute(5);
			builder = HttpClients
					.custom()
					.setConnectionManager(cm)
					.setKeepAliveStrategy(
							new InnerConnectionKeepAliveStrategy());
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.FAIL, e);
		}

	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params,
			HttpCfg cfg) throws BaseException {
		return doGet(url, params, DEFAULT_CHARSET, cfg);
	}

	/**
	 * 发送 GET 请求（HTTP），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params,
			String charset, HttpCfg cfg) throws BaseException {
		try {
			HttpGet httpGet = buildHttpGet(url, params, cfg);
			CloseableHttpClient httpClient = buildHttpClient();
			CloseableHttpResponse response = httpClient.execute(httpGet);

			return handleResponse(response, charset);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_GET_FAILED,
					new Object[] { url }, e);
		}
	}

	/**
	 * 发送 GET 请求（HTTPS），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doGetSSL(String url, Map<String, Object> params,
			HttpCfg cfg) throws BaseException {
		return doGetSSL(url, params, DEFAULT_CHARSET, cfg);
	}

	/**
	 * 发送 GET 请求（HTTPS），K-V形式
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doGetSSL(String url, Map<String, Object> params,
			String charset, HttpCfg cfg) throws BaseException {
		try {
			HttpGet httpGet = buildHttpGet(url, params, cfg);

			CloseableHttpClient httpClient = buildHttpsClient();
			CloseableHttpResponse response = httpClient.execute(httpGet);

			return handleResponse(response, charset);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_GET_FAILED,
					new Object[] { url }, e);
		}
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式，字符集为 @see {@link #DEFAULT_CHARSET}
	 * 
	 * @param apiUrl
	 *            API接口URL
	 * @param params
	 *            参数map，无参数则为null
	 * @return
	 * @throws BaseException
	 */
	public static String doPostSSL(String apiUrl, Map<String, Object> params,
			HttpCfg cfg) throws BaseException {
		return doPostSSL(apiUrl, params, DEFAULT_CHARSET, cfg);
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param url
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @param charset
	 *            字符集
	 * @return
	 */
	public static String doPost(String url, Map<String, Object> params,
			String charset, HttpCfg cfg) throws BaseException {
		try {
			HttpPost httpPost = buildHttpPost(url, params, charset, cfg);

			CloseableHttpClient httpClient = buildHttpClient();
			CloseableHttpResponse response = httpClient.execute(httpPost);

			return handleResponse(response, charset);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED,
					new Object[] { url }, e);
		}
	}

	/**
	 * 发送 POST 请求（HTTPS），K-V形式，字符集为 @see {@link #DEFAULT_CHARSET}
	 * 
	 * @param url
	 *            API接口URL
	 * @param params
	 *            参数map，无参数则为null
	 * @return
	 * @throws BaseException
	 */
	public static String doPost(String url, Map<String, Object> params,
			HttpCfg cfg) throws BaseException {
		return doPost(url, params, DEFAULT_CHARSET, cfg);
	}

	/**
	 * 发送 POST 请求（HTTP），K-V形式
	 * 
	 * @param url
	 *            API接口URL
	 * @param params
	 *            参数map
	 * @param charset
	 *            字符集
	 * @return
	 */
	public static String doPostSSL(String url, Map<String, Object> params,
			String charset, HttpCfg cfg) throws BaseException {
		try {
			HttpPost httpPost = buildHttpPost(url, params, charset, cfg);

			CloseableHttpClient httpClient = buildHttpsClient();
			CloseableHttpResponse response = httpClient.execute(httpPost);

			return handleResponse(response, charset);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED,
					new Object[] { url }, e);
		}
	}

	private static RequestConfig getConfigByUrl(String url, HttpCfg cfg) {
		synchronized (requestConfigs) {
			if (requestConfigs.containsKey(url)) {
				return requestConfigs.get(url);
			} else {
				int connectTimeout = cfg.getConnectTo();
				int socketTimeout = cfg.getSocketTo();
				int connectRequestTimeout = cfg.getRequestTo();
				RequestConfig r = RequestConfig
						.custom()
						// .setProxy(new HttpHost("localhost", 8888))// 测试抓包使用
						.setConnectTimeout(connectTimeout)
						.setSocketTimeout(socketTimeout)
						.setConnectionRequestTimeout(connectRequestTimeout)
						.build();
				requestConfigs.put(url, r);
				return r;
			}
		}
	}

	private static List<NameValuePair> getNvps(Map<String, Object> params) {
		List<NameValuePair> pairList = null;
		if (params != null && !params.isEmpty()) {
			pairList = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				NameValuePair pair = new BasicNameValuePair(entry.getKey(),
						entry.getValue().toString());
				pairList.add(pair);
			}
		} else {
			pairList = new ArrayList<NameValuePair>(0);
		}
		return pairList;
	}

	/**
	 * 处理响应，对于非正常返回码直接抛出异常
	 * 
	 * @since
	 * @param response
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	private static String handleResponse(CloseableHttpResponse response,
			String charset) throws Exception {
		try {

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode < 200 || statusCode >= 300) {
				throw new BaseException(BaseErrorCode.COMM_HTTP_STATUS_ERROR,
						new Object[] { statusCode });
			}

			HttpEntity entity = response.getEntity();
			String httpStr = EntityUtils.toString(entity, charset);

			return httpStr;
		} finally {
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class InnerConnectionKeepAliveStrategy extends
			DefaultConnectionKeepAliveStrategy {
		@Override
		public long getKeepAliveDuration(HttpResponse response,
				HttpContext context) {

			long orig = super.getKeepAliveDuration(response, context);
			return orig == -1 ? 30 * 1000 : orig;
		}
	}

	/**
	 * 构建HttpClient
	 * 
	 * @since
	 * @return
	 */
	private static CloseableHttpClient buildHttpClient() {

		// CloseableHttpClient httpClient = HttpClients.custom()
		// .setConnectionManager(cm)
		// .setKeepAliveStrategy(new InnerConnectionKeepAliveStrategy())
		// .build();
		//
		// return httpClient;
		return builder.build();

	}

	/**
	 * 构建HttpsClient
	 * 
	 * @since
	 * @return
	 */
	private static CloseableHttpClient buildHttpsClient()
			throws GeneralSecurityException {
		// CloseableHttpClient httpClient = HttpClients.custom()
		// .setConnectionManager(cm)
		// .setKeepAliveStrategy(new InnerConnectionKeepAliveStrategy())
		// .setSSLSocketFactory(createSSLConnSocketFactory()).build();
		// return httpClient;
		return builder.build();
	}

	private static KeyStore loadKeyStore(String pathKey, String passwordKey)
			throws Exception {
		String path = System.getProperty(pathKey);
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		String password = System.getProperty(passwordKey);
		if (StringUtils.isEmpty(password)) {
			return null;
		}

		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		keyStore.load(new FileInputStream(new File(path)),
				password.toCharArray());
		return keyStore;

	}

	/**
	 * 创建SSLConnectionSocketFactory
	 * 
	 * @since
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 */
	private static SSLConnectionSocketFactory createSSLConnSocketFactory()
			throws Exception {
		KeyStore keyStore = loadKeyStore("http.keyStore", "http.keyStore.pwd");
		KeyStore trustStore = loadKeyStore("http.trustStore",
				"http.trustStore.pwd");

		SSLContext sslContext = SSLContexts
				.custom()
				.loadTrustMaterial(trustStore, new TrustStrategy() {

					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						return false;
					}
				})
				.loadKeyMaterial(
						keyStore,
						keyStore != null ? System.getProperty(
								"http.keyStore.pwd").toCharArray() : null)
				.build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext, new HostnameVerifier() {

					public boolean verify(String arg0, SSLSession arg1) {
						return true;
					}
				});
		return sslsf;
	}

	/**
	 * 构建HttpGet
	 * 
	 * @since
	 * @param url
	 * @param params
	 * @return
	 * @throws URISyntaxException
	 */
	private static HttpGet buildHttpGet(String url, Map<String, Object> params,
			HttpCfg cfg) throws URISyntaxException {
		URI uri = new URIBuilder(url).setParameters(getNvps(params)).build();

		String aUrl = uri.toString();

		HttpGet httpGet = new HttpGet(aUrl);
		httpGet.setConfig(getConfigByUrl(aUrl, cfg));
		return httpGet;
	}

	/**
	 * 构建HttpPost
	 * 
	 * @since
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	private static HttpPost buildHttpPost(String url,
			Map<String, Object> params, String charset, HttpCfg cfg) {
		try {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setConfig(getConfigByUrl(url, cfg));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(
					getNvps(params), charset);
			httpPost.setEntity(entity);

			return httpPost;
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED, e);
		}
	}

	/**
	 * xml 形式数据提交
	 * 
	 * @since
	 * @param url
	 * @param xmlInput
	 * @return
	 */
	public static HttpResponse postXml(String url, String xmlInput) {

		HttpResponse response;
		try {
			HttpClient httpclient = buildHttpClient();

			HttpPost httppost = new HttpPost(url);
			StringEntity myEntity = new StringEntity(xmlInput, "UTF-8");
			httppost.addHeader("Content-Type", "application/xml");
			httppost.setEntity(myEntity);
			response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED, e);
		} catch (IOException e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED, e);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMM_HTTP_POST_FAILED, e);
		}
		return response;
	}

}
