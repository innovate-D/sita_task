package org.sita.service;

import lombok.extern.slf4j.Slf4j;
import org.sita.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
@Slf4j
public class OrderClient {

    private final RestClient restClient;
    private static final String USER_SERVICE_URL = "http://localhost:8083/users";

    public OrderClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public HttpStatusCode userExists(UUID id){

        log.info("Checking if user exists for id {} ",id);
        HttpStatusCode code = null;
        try {
            code = restClient.get()
                    .uri(USER_SERVICE_URL + "/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toBodilessEntity()
                    .getStatusCode();
        }catch (HttpClientErrorException e){
            if(e.getStatusCode()== HttpStatus.BAD_REQUEST){
                throw new CustomException("Input is invalid");
            }
            if(e.getStatusCode()==HttpStatus.NOT_FOUND){
                throw new CustomException("User not found");
            }
        }catch (HttpServerErrorException e){
            if(e.getStatusCode()==HttpStatus.INTERNAL_SERVER_ERROR){
                throw new CustomException("Something unexpected occurred, please try again later.");
            }
        }
        return code;
    }
}
