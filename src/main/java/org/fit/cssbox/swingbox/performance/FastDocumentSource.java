package org.fit.cssbox.swingbox.performance;

import org.apache.hc.client5.http.impl.cache.BasicHttpCacheStorage;
import org.apache.hc.client5.http.impl.cache.CacheConfig;
import org.apache.hc.client5.http.impl.cache.CachingHttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.util.TimeValue;
import org.fit.cssbox.io.DocumentSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class FastDocumentSource extends DocumentSource {
  private final CloseableHttpClient mClient;

  private URL mUrl;
  private InputStream mInputStream;
  private String mContentType = "";

  public FastDocumentSource(final URL url) throws IOException {
    super(url);
    assert url != null;

    mClient = createHttpClient();
    mUrl = url;
  }

  public FastDocumentSource setURL(final URL url) {
    mUrl = url;
    return this;
  }

  private CloseableHttpClient createHttpClient() {
    final TimeValue cacheLifetime = TimeValue.ofHours(1);

    final CacheConfig cacheConfig = CacheConfig.custom()
        .setMaxCacheEntries(1000)
        .setMaxObjectSize(120 * 1024)
        .setHeuristicCachingEnabled(true)
        .setHeuristicDefaultLifetime(cacheLifetime)
        .build();

    final BasicHttpCacheStorage cacheStore = new BasicHttpCacheStorage(cacheConfig);

    return CachingHttpClients.custom()
        .setCacheConfig(cacheConfig)
        .setHttpCacheStorage(cacheStore)
        .build();
  }

  @Override
  public URL getURL() {
    return mUrl;
  }

  @Override
  public String getContentType() {
    return mContentType;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    final URI uri;
    try {
      uri = getURL().toURI();
    } catch (final Exception e) {
      throw new IOException(e);
    }

    final HttpGet httpGet = new HttpGet(uri);

    ClassicHttpResponse response = null;
    try {
      response = mClient.executeOpen(null, httpGet, null);

      final HttpEntity entity = response.getEntity();
      if (entity == null) {
        throw new IOException("No response entity for: " + uri);
      }

      final ContentType contentType = ContentType.parse(entity.getContentType());
      mContentType = contentType.getMimeType();

      return mInputStream = entity.getContent();
    } catch (Exception e) {
      EntityUtils.consumeQuietly(response != null ? response.getEntity() : null);
      throw new IOException("Error fetching: " + uri, e);
    }
  }

  @Override
  public void close() throws IOException {
    if (mInputStream != null) {
      mInputStream.close();
    }
  }
}
