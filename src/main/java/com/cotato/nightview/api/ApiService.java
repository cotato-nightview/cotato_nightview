package com.cotato.nightview.api;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public interface ApiService {

    public RequestEntity<Void> buildRequestEntity(URI uri);

    public ResponseEntity<String> callApi(RequestEntity<Void> requestEntity);

    public URI buildUri(Object o1, Object o2);
}
