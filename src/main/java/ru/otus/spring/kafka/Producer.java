package ru.otus.spring.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.otus.spring.configuration.KafkaConfig;
import ru.otus.spring.domain.UserAction;

@Component
@RequiredArgsConstructor
public class Producer {

    private static final Logger LOG = LoggerFactory.getLogger(Producer.class);

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final KafkaConfig kafkaConfig;

    public void sendMessage(UserAction user) {
        String orderAsMessage = null;
        try {
            orderAsMessage = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            LOG.error("Error create json [user={}]", e.getMessage());
        }
        LOG.info("Producing message [user={}]", user.getUserId());
        kafkaTemplate.send(kafkaConfig.returnTopicName(), "Writing in log -> " + orderAsMessage);
    }
}
