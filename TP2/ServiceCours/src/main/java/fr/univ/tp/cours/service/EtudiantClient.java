package fr.univ.tp.cours.service;

import fr.univ.tp.cours.model.EtudiantDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EtudiantClient {

    private final RestTemplate rest;
    private final String baseUrl;

    public EtudiantClient(@Value("${etudiants.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        // Timeouts : on n'attend pas indéfiniment si ServiceEtudiants ne répond pas
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(2000);
        this.rest = new RestTemplate(factory);
    }

    public EtudiantDto getEtudiantById(int id) {
        return rest.getForObject(baseUrl + "/" + id, EtudiantDto.class);
    }
}