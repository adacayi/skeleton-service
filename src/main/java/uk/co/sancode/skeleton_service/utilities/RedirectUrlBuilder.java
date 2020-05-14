package uk.co.sancode.skeleton_service.utilities;

import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class RedirectUrlBuilder {
    private Charset charsetEncoding;
    private String scheme;
    private String host;
    private int port;
    private String path;

    public RedirectUrlBuilder(final String scheme, final String host, final int port, final String path) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public RedirectUrlBuilder withEncoding(final Charset charsetEncoding) {
        this.charsetEncoding = charsetEncoding;
        return this;
    }

    public String build() throws URISyntaxException {
        URIBuilder builder = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port)
                .setPath(path);

        if (charsetEncoding != null) {
            return URLEncoder.encode(builder.build().toString(), charsetEncoding);
        }
        return builder.build().toString();
    }
}
