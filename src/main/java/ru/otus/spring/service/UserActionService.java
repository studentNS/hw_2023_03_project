package ru.otus.spring.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UserActionService {

    Long countUserActionByUserId (Long userId);

    void insertUserAction (Update update);
}
