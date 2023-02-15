package com.cotato.nightview.api;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface ExteranlApi {

    public RequestEntity<Void> buildRequestEntity(URI uri);

    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity);

}
