package br.com.apollomusic.app.application;

import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.payload.request.AuthorizeThirdPartyRequest;
import br.com.apollomusic.app.domain.payload.request.ThirdPartyAccessRequest;
import br.com.apollomusic.app.domain.payload.response.ThirdPartyAccessResponse;
import br.com.apollomusic.app.infra.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ApiAuthService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "https://accounts.spotify.com/api/token";
    private final ThirdPartyAccessRequest thirdPartyAccessRequest;
    private final OwnerRepository ownerRepository;

    @Autowired
    public ApiAuthService(ThirdPartyAccessRequest thirdPartyAccessRequest, OwnerRepository ownerRepository) {
        this.thirdPartyAccessRequest = thirdPartyAccessRequest;
        this.restTemplate = new RestTemplate();
        this.ownerRepository = ownerRepository;
    }

    public ResponseEntity<?> getAccessTokenFromApi(AuthorizeThirdPartyRequest authorizeThirdPartyRequest, String email) {
        thirdPartyAccessRequest.setCode(authorizeThirdPartyRequest.code());

        MultiValueMap<String, String> paramsApi = new LinkedMultiValueMap<>();
        paramsApi.add("code", thirdPartyAccessRequest.getCode());
        paramsApi.add("redirect_uri", thirdPartyAccessRequest.getRedirect_uri());
        paramsApi.add("grant_type", thirdPartyAccessRequest.getGrant_type());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(thirdPartyAccessRequest.getClientId(), thirdPartyAccessRequest.getClientSecret());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(paramsApi, headers);

        ResponseEntity<ThirdPartyAccessResponse> responseEntity;

        Owner owner = ownerRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        responseEntity = restTemplate.postForEntity(baseUrl, request, ThirdPartyAccessResponse.class);

        if (responseEntity.getBody() != null) {
            owner.setRefreshToken(responseEntity.getBody().refresh_token());
            owner.setAccessToken(responseEntity.getBody().access_token());
            owner.setTokenExpiresIn(System.currentTimeMillis() + responseEntity.getBody().expires_in() * 1000);
            ownerRepository.save(owner);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void renewAccessToken(Owner ownerInfo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(thirdPartyAccessRequest.getClientId(), thirdPartyAccessRequest.getClientSecret());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refresh_token", ownerInfo.getRefreshToken());
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Owner owner = ownerRepository.findByEmail(ownerInfo.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        ResponseEntity<ThirdPartyAccessResponse> responseEntity = restTemplate.postForEntity(baseUrl, request, ThirdPartyAccessResponse.class);

        if (responseEntity.getBody() != null) {
            owner.setAccessToken(responseEntity.getBody().access_token());
            owner.setTokenExpiresIn(System.currentTimeMillis() + responseEntity.getBody().expires_in() * 1000);
            ownerRepository.save(owner);
        }
    }

    public boolean isTokenExpired(Long expiresIn) {
        return System.currentTimeMillis() > expiresIn;
    }

    public void renewTokenIfNeeded(String email) {
        Owner ownerInfo = ownerRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (ownerInfo.getRefreshToken() == null) return;

        if (ownerInfo.getAccessToken() == null || isTokenExpired(ownerInfo.getTokenExpiresIn())) {
            renewAccessToken(ownerInfo);
        }
    }
}
