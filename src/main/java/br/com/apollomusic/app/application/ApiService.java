package br.com.apollomusic.app.application;

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

        String url = mappingQueryParameters(endpoint, queryParams);

        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        return responseEntity.getBody();
    }

    public <T> String post(String endpoint, T requestBody, String accessToken) {
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

    public <T> String put(String endpoint, Map<String, String> queryParams, T requestBody, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String url = mappingQueryParameters(endpoint, queryParams);

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, request, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return "Error in put";
        }
    }

    public <T> String delete(String endpoint, T requestBody, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<T> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange((baseUrl + endpoint), HttpMethod.DELETE, request, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            return "Error in delete";
        }
    }

    private String mappingQueryParameters(String endpoint, Map<String, String> queryParams){
        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("/").append(endpoint);
        if (queryParams != null && !queryParams.isEmpty()) {
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        return urlBuilder.toString();
    }

}
