package ru.otus.spring;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.domain.UserAction;
import ru.otus.spring.service.UserActionService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("Сервис для работы с действиями пользователей должен")
@SpringBootTest
public class UserActionServiceTest {

    @MockBean
    private UserActionService userActionService;

    @DisplayName("возвращать количество действий пользователей")
    @Test
    public void showQuestionsAll() {
        List<UserAction> userActionList = Arrays.asList(new UserAction());
        when(userActionService.countUserActionByUserId(anyLong())).thenReturn((long) userActionList.size());
        Long userActionCount = userActionService.countUserActionByUserId(anyLong());
        assertThat(userActionCount).isEqualTo(1L);
    }
}
