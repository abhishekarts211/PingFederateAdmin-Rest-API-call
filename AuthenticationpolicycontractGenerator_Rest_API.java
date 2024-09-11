package com.pfadmin.apcgen; // Define the package name

import javax.net.ssl.*; // Import SSL/TLS classes for handling secure connections
import java.io.BufferedReader; // Import BufferedReader for reading text from an input stream
import java.io.OutputStream; // Import OutputStream for sending data to the server
import java.io.InputStreamReader; // Import InputStreamReader for converting byte streams to character streams
import java.net.HttpURLConnection; // Import HttpURLConnection for handling HTTP connections
import java.net.URL; // Import URL class for URL handling
import java.security.cert.X509Certificate; // Import X509Certificate for handling certificates
import java.util.Base64; // Import Base64 for encoding credentials

public class AuthenticationpolicycontractGenerator { // Define the public class

    // API endpoint URL for generating SSL certificates
    private static final String API_URL = "https://localhost:9999/pf-admin-api/v1/authenticationPolicyContracts";
    
    // Username and password for HTTP Basic Authentication
    private static final String USERNAME = "Administrator";
    private static final String PASSWORD = "***********";

    public static void main(String[] args) { // Main method to execute the program
        try {
            // Bypass SSL certificate validation (for development purposes only)
            TrustManager[] trustAll = new TrustManager[]{ // Create an array of TrustManager
                new X509TrustManager() { // Implement a custom TrustManager to bypass certificate validation
                    public X509Certificate[] getAcceptedIssuers() { return null; } // No accepted issuers (ignore certificate chain)
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { } // No client certificate checking
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { } // No server certificate checking
                }
            };

            // Initialize SSLContext with the custom TrustManager
            SSLContext sc = SSLContext.getInstance("TLS"); // Get an instance of SSLContext for TLS
            sc.init(null, trustAll, new java.security.SecureRandom()); 
			// Initialize SSLContext with the custom TrustManager
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 
			// Set the default SSLSocketFactory for secure connections
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true); 
			// Set the default HostnameVerifier to bypass hostname checks

			
            // Create a URL object with the API endpoint
            URL url = new URL(API_URL); // Create a URL object with the API endpoint URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open an HTTP connection to the URL

            // Set the request method to POST
            connection.setRequestMethod("POST"); // Set the HTTP request method to POST

            // Set the required headers
            connection.setRequestProperty("Accept", "application/json"); // Set the Accept header to request JSON response
            connection.setRequestProperty("Content-Type", "application/json"); // Set the Content-Type header to JSON
            connection.setRequestProperty("X-XSRF-Header", "PingFederate"); // Set the X-XSRF-Header for anti-CSRF token

            // Set up HTTP Basic Authentication
            String auth = USERNAME + ":" + PASSWORD; // Combine username and password
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes()); // Encode the credentials to Base64
            String authHeader = "Basic " + encodedAuth; // Prefix with "Basic" for the Authorization header
            connection.setRequestProperty("Authorization", authHeader); // Set the Authorization header with encoded credentials

            // Enable input/output streams for POST request
            connection.setDoOutput(true); // Enable output stream for sending request body

            // Create the JSON payload
            String jsonPayload = "{\n" +
                   "  \"id\": \"uOdpxdkXlz5tsLzK\",\n" + // JSON field for ID
                   "  \"name\": \"pflearningtest1\",\n" + // JSON field for name
                   "  \"coreAttributes\": [\n" + // JSON field for core attributes
                   "    {\n" +
                   "      \"name\": \"subject\"\n" + // JSON value for core attribute name
                   "    }\n" +
                   "  ],\n" +
                   "  \"extendedAttributes\": [\n" + // JSON field for extended attributes
                   "    {\n" +
                   "      \"name\": \"firstname\"\n" + // JSON value for extended attribute name
                   "    },\n" +
                   "    {\n" +
                   "      \"name\": \"roles\"\n" + // JSON value for extended attribute name
                   "    },\n" +
                   "    {\n" +
                   "      \"name\": \"TelephoneNumber\"\n" + // JSON value for extended attribute name
                   "    },\n" +
                   "    {\n" +
                   "      \"name\": \"emailAddress\"\n" + // JSON value for extended attribute name
                   "    },\n" +
                   "    {\n" +
                   "      \"name\": \"uid\"\n" + // JSON value for extended attribute name
                   "    },\n" +
                   "    {\n" +
                   "      \"name\": \"lastname\"\n" + // JSON value for extended attribute name
                   "    }\n" +
                   "  ]\n" +
                   "}";


            // Send the JSON payload
            try (OutputStream os = connection.getOutputStream()) { // Create OutputStream to send the request body
                byte[] input = jsonPayload.getBytes("utf-8"); // Convert JSON payload to bytes
                os.write(input, 0, input.length); // Write bytes to the output stream
            }

            // Get the response code from the server
            int responseCode = connection.getResponseCode(); // Get the HTTP response code
            System.out.println("Response Code: " + responseCode); // Print the response code

            // Read the response from the input stream
            BufferedReader in;
            if (responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) { // Check if response code indicates an error
                // Read the error response if the status code indicates an error
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream())); 
				// Create BufferedReader to read the error input stream
                StringBuilder errorResponse = new StringBuilder(); // StringBuilder to accumulate error response lines
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) { // Read each line of the error response
                    errorResponse.append(errorLine); // Append the line to the StringBuilder
                }
                errorReader.close(); // Close the BufferedReader
                System.out.println("Error Response: " + errorResponse.toString()); // Print the error response
				
            } else {
				
                // Read the successful response
                in = new BufferedReader(new InputStreamReader(connection.getInputStream())); // Create BufferedReader to read the input stream
                String inputLine; // String to hold each line of the response
                StringBuilder response = new StringBuilder(); // StringBuilder to accumulate response lines
                while ((inputLine = in.readLine()) != null) { // Read each line of the response
                    response.append(inputLine); // Append the line to the StringBuilder
                }
                in.close(); // Close the BufferedReader
                System.out.println("Response: " + response.toString()); // Print the accumulated response
            }

        } catch (Exception e) { // Catch any exceptions
            e.printStackTrace(); // Print the stack trace of the exception
        }
    }
}
