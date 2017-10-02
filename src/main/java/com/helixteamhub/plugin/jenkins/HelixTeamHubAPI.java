package com.helixteamhub.plugin.jenkins;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class HelixTeamHubAPI {

    private String hostname;

    private HelixTeamHubAPIKeys helixTeamHubAPIKeys;

    public HelixTeamHubAPI(String hostname, HelixTeamHubAPIKeys helixTeamHubAPIKeys) {
        this.hostname = hostname;
        this.helixTeamHubAPIKeys = helixTeamHubAPIKeys;
    }

    private static HttpURLConnection getConnection(URL url) throws IOException {
        return getConnection(url, false);
    }

    private static HttpURLConnection getConnection(URL url, boolean acceptInvalidSSLCertificate) throws IOException {
        if (acceptInvalidSSLCertificate) { // This is here for testing in environments with invalid certs
            try {
                final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }};

                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection instanceof HttpsURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                    ((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {
                        public boolean verify(String string, SSLSession ssls) {
                            return true;
                        }
                    });
                    return connection;
                }
                return connection;
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (KeyManagementException ex) {
                ex.printStackTrace();
            }
            return null;
        } else {
            return (HttpURLConnection) url.openConnection();
        }
    }

    public void create(String endpoint, String content) throws HelixTeamHubException {
        URL url;
        HttpURLConnection connection;
        try {
            url = new URL(String.format("%s/api/%s", hostname, endpoint));

            connection = getConnection(url);
            connection.setRequestProperty("Accept", "application/vnd.hth.v1");
            connection.setRequestProperty("Authorization", helixTeamHubAPIKeys.toString());
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes().length));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(content);
            wr.flush();
            wr.close();

            BufferedReader in;
            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                StringBuffer responseContent = new StringBuffer();
                try {
                    String responseLine;
                    while ((responseLine = in.readLine()) != null) {
                        responseContent.append(responseLine);
                    }
                } catch (Exception ex) {
                    // Ignore
                } finally {
                    in.close();
                    throw new HelixTeamHubException(String.format("%s %s - %s", connection.getResponseCode(), connection.getResponseMessage(), responseContent.toString()));
                }
            }
        } catch (MalformedURLException e) {
            throw new HelixTeamHubException(String.format("Helix TeamHub API hostname could not be parsed: %s", hostname));
        } catch (ProtocolException e) {
            throw new HelixTeamHubException(String.format("Helix TeamHub connection could not be established due to ProtocolException: %s", e.getMessage()));
        } catch (IOException e) {
            throw new HelixTeamHubException(String.format("Helix TeamHub connection could not be established due to IOException: %s", e.getMessage()));
        }
    }

}
