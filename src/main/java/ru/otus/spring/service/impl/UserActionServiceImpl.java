package ru.otus.spring.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.otus.spring.domain.UserAction;
import ru.otus.spring.kafka.Producer;
import ru.otus.spring.repository.UserActionRepository;
import ru.otus.spring.service.UserActionService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserActionServiceImpl implements UserActionService {

    private final UserActionRepository userActionRepository;

    private final Producer producerService;

    @Override
    public Long countUserActionByUserId(Long userId) {
        return userActionRepository.countUserActionByUserId(userId);
    }

    @Override
    public void insertUserAction(Update update) {
        UserAction userAction = new UserAction();
        userAction.setUserId(update.getMessage().getFrom().getId());
        userAction.setUserName(update.getMessage().getFrom().getFirstName());
        userAction.setDateTime(LocalDateTime.now());
        producerService.sendMessage(userAction);
        userActionRepository.save(userAction);
    }
}
