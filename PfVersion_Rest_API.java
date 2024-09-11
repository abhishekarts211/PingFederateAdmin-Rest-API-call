package com.pfadmin.version; // Package declaration

import javax.net.ssl.*; // Import SSL/TLS classes for handling secure connections
import java.io.BufferedReader; // Import BufferedReader for reading text from an input stream
import java.io.InputStreamReader; // Import InputStreamReader for converting byte streams to character streams
import java.net.HttpURLConnection; // Import HttpURLConnection for handling HTTP connections
import java.net.URL; // Import URL class for URL handling
import java.security.cert.X509Certificate; // Import X509Certificate for handling certificates
import java.util.Base64; // Import Base64 for encoding credentials

public class PingFederateApiVersion { // Define the public class

    private static final String API_URL = "https://localhost:9999/pf-admin-api/v1/version"; // API endpoint URL
    private static final String USERNAME = "Administrator"; // Username for HTTP Basic Authentication
    private static final String PASSWORD = "*********"; // Password for HTTP Basic Authentication

    public static void main(String[] args) { // Main method to execute the program
        try {
            // Bypass SSL certificate validation (for development purposes only)
            TrustManager[] trustAll = new TrustManager[]{ // Create an array of TrustManager
                new X509TrustManager() { // Implement a custom TrustManager
                    public X509Certificate[] getAcceptedIssuers() { return null; } // No accepted issuers (ignore certificate chain)
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { } // No client certificate checking
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { } // No server certificate checking
                }
            };

            SSLContext sc = SSLContext.getInstance("TLS"); // Get an instance of SSLContext for TLS
            sc.init(null, trustAll, new java.security.SecureRandom()); // Initialize SSLContext with the custom TrustManager
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); // Set the default SSLSocketFactory
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true); // Set the default HostnameVerifier to bypass hostname checks

            // Create a URL object with the API endpoint
            URL url = new URL(API_URL); // Create a URL object with the API endpoint URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open an HTTP connection to the URL

            // Set the request method to GET
            connection.setRequestMethod("GET"); // Set the HTTP request method to GET

            // Set the required headers
            connection.setRequestProperty("Accept", "application/json"); // Set the Accept header to request JSON response
            connection.setRequestProperty("X-XSRF-Header", "PingFederate"); // Set the X-XSRF-Header for anti-CSRF token

            // Set up HTTP Basic Authentication
            String auth = USERNAME + ":" + PASSWORD; // Combine username and password
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes()); // Encode the credentials to Base64
            String authHeader = "Basic " + encodedAuth; // Prefix with "Basic" for the Authorization header
            connection.setRequestProperty("Authorization", authHeader); // Set the Authorization header with encoded credentials

            // Get the response code
            int responseCode = connection.getResponseCode(); // Get the HTTP response code
            System.out.println("Response Code: " + responseCode); // Print the response code

            // Read the response from the input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Create a BufferedReader to read the input stream
            String inputLine; // String to hold each line of the response
            StringBuilder response = new StringBuilder(); // StringBuilder to accumulate response lines
            while ((inputLine = in.readLine()) != null) { // Read each line of the response
                response.append(inputLine); // Append the line to the StringBuilder
            }
            in.close(); // Close the BufferedReader

            // Print the response
            System.out.println("Response: " + response.toString()); // Print the accumulated response

        } catch (Exception e) { // Catch any exceptions
            e.printStackTrace(); // Print the stack trace of the exception
        }
    }
}
