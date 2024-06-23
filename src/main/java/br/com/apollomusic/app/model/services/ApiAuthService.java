package br.com.apollomusic.app.model.services;


import br.com.apollomusic.app.model.dto.OwnerApiAuthReqDto;
import br.com.apollomusic.app.model.dto.RequestTokenDto;
import br.com.apollomusic.app.model.dto.ResponseTokenDto;
import br.com.apollomusic.app.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ApiAuthService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://accounts.spotify.com/api/token";
    private final RequestTokenDto requestTokenDto;
    private final OwnerRepository ownerRepository;

    @Autowired
    public ApiAuthService(RequestTokenDto requestTokenDto, OwnerRepository ownerRepository) {
        this.requestTokenDto = requestTokenDto;
        this.restTemplate = new RestTemplate();
        this.ownerRepository = ownerRepository;
    }

    public ResponseEntity<ResponseTokenDto> getAccessTokenFromApi(OwnerApiAuthReqDto reqDto) {
        requestTokenDto.setCode(reqDto.code());

        MultiValueMap<String, String> paramsApi = new LinkedMultiValueMap<>();
        paramsApi.add("code", requestTokenDto.getCode());
        paramsApi.add("redirect_uri", requestTokenDto.getRedirect_uri());
        paramsApi.add("grant_type", requestTokenDto.getGrant_type());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestTokenDto.getClientId(), requestTokenDto.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramsApi, headers);

        ResponseEntity<ResponseTokenDto> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(baseUrl, request, ResponseTokenDto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        if (responseEntity.getBody() != null) {
            ownerRepository.updateRefreshToken(reqDto.email(), responseEntity.getBody().refresh_token());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(responseEntity.getBody());
    }


    public ResponseEntity<ResponseTokenDto> renewAccessToken(OwnerApiAuthReqDto reqDto) {
        var owner = ownerRepository.findByEmail(reqDto.email());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(requestTokenDto.getClientId(), requestTokenDto.getClientSecret());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", owner.get().getRefreshToken());
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<ResponseTokenDto> responseEntity = restTemplate.postForEntity(baseUrl, request, ResponseTokenDto.class);

        return ResponseEntity.ok(responseEntity.getBody());

    }
}
