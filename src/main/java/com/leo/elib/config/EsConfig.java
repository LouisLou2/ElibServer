//package com.leo.elib.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
//
//import javax.net.ssl.SSLContext;
//
//@Configuration
//public class EsConfig extends ElasticsearchConfiguration {
//
//  @Value("${es.host}")
//  private String host;
//
//  @Value("${es.port}")
//  private int port;
//
//  @Value("${es.username}")
//  private String username;
//
//  @Value("${es.password}")
//  private String password;
//
//  @Value("${es.https.enabled}")
//  private boolean https;
//
//  @Value("${es.https.insecure}")
//  private boolean insecure;
//
//  @Override
//  public ClientConfiguration clientConfiguration() {
//    var builder = ClientConfiguration.builder()
//      .connectedTo(host + ":" + port);
//
//    // 只在使用 HTTPS 时设置 SSL
//    if (https) {
//      // 如果需要 HTTPS，添加 SSL 上下文
//      builder.usingSsl(getSSLContext());
//    }
//
//    // 设置基本认证
//    builder.withBasicAuth(username, password);
//    return builder.build();
//  }
//
//  private SSLContext getSSLContext() {
//    // 这里的 SSL 上下文创建代码可以保留，或在不需要 HTTPS 时完全移除
//    // 如果您确定使用 HTTP，这个方法可以直接删除
//    return null; // 如果不需要 SSL，返回 null
//  }
//}

package com.leo.elib.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class EsConfig extends ElasticsearchConfiguration {

  @Value("${es.host}")
  private String host;

  @Value("${es.port}")
  private int port;

  @Value("${es.username}")
  private String username;

  @Value("${es.password}")
  private String password;

  @Value("${es.https.enabled}")
  private boolean https;

  @Value("${es.https.insecure}")
  private boolean insecure;


  @Override
  public ClientConfiguration clientConfiguration() {
    assert !https || insecure : "Insecure HTTPS is enabled, but HTTPS is not enabled";
    var builder = ClientConfiguration.builder()
      .connectedTo(host + ":" + port);
    if (https) {
      if (insecure) {
        // 设置不安全的 SSL 上下文
        builder.usingSsl(getInsecureSSLContext(), NoopHostnameVerifier.INSTANCE);
      } else {
        builder.usingSsl(getSSLContext());
      }
    }
    builder.withBasicAuth(username, password);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    // 设置内容类型为 application/json
    builder.withDefaultHeaders(headers);
    return builder.build();
  }

  private SSLContext getSSLContext() {
    return null;
  }

  private SSLContext getInsecureSSLContext() {
    try {
      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      return sslContext;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create an insecure SSL context", e);
    }
  }

  // 信任所有证书的 TrustManager
  private static final TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
      @Override
      public void checkClientTrusted(X509Certificate[] chain, String authType) {
      }
      @Override
      public void checkServerTrusted(X509Certificate[] chain, String authType) {
      }
      @Override
      public X509Certificate[] getAcceptedIssuers() {
        return null;
      }
    }
  };
}