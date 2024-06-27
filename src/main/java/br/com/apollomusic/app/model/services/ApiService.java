package br.com.apollomusic.app.model.services;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ApiService {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public ApiService() {
        this.baseUrl = "https://api.spotify.com/v1";
        this.restTemplate = new RestTemplate();
    }


    public String get(String endpoint, Map<String, String> queryParams, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("/").append(endpoint);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, request, String.class);

        return responseEntity.getBody();
    }

    public <T> void post(String endpoint, T requestBody, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange((baseUrl + endpoint), HttpMethod.POST, request, String.class);
            responseEntity.getBody();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    public <T> String postWithResponse(String endpoint, T requestBody, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange((baseUrl + endpoint), HttpMethod.POST, request, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return "Error in postWithResponse";
        }

    }
}
