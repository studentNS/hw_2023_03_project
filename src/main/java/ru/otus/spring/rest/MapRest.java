package ru.otus.spring.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MapRest {

    private static final Logger LOG = LoggerFactory.getLogger(MapRest.class);

    @Value("${yandex.map}")
    private String yandexMapUrl;

    public String getOrganization(String coord) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.getForObject(yandexMapUrl.replace("lan,lon", coord), String.class);
        } catch (RuntimeException e) {
            LOG.error("Ошибка получения геопозиции", e.getMessage());
            return null;
        }
    }
}
