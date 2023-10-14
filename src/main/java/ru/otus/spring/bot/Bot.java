package ru.otus.spring.bot;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.otus.spring.rest.MapRest;
import ru.otus.spring.service.MapService;
import ru.otus.spring.service.UserActionService;

@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    private static final String START = "/start";

    private static final String LOCATION_INPUT = "/location_input";

    private static final String STATISTICS = "/statistics";

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    private final MapRest mapRest;

    private final MapService mapService;

    private final UserActionService userActionService;

    /**
     * Обработка входящих сообщений
     * */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().hasLocation()) {
            locationCommand(update);
            return;
        } else if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        processingCommand(update);
    }

    private void processingCommand(Update update) {
        var messageUser = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var userName = update.getMessage().getChat().getFirstName();

        String[] messageArr = messageUser.split(" ");
        String message = messageArr[0];

        switch (message) {
            case START -> {
                startCommand(chatId, userName);
            }
            case LOCATION_INPUT -> {
                String messageGeo = messageArr[1];
                locationInputCommand(chatId, messageGeo, update);
            }
            case STATISTICS -> {
                staticticsCommand(chatId, update.getMessage().getFrom().getId());
            }
            default -> unknownCommand(chatId);
        }
    }

    private void startCommand(Long chatId, String userName) {
        var text = """
                Добро пожаловать в бот, %s!
                
                Здесь можно узнать несколько ближайших к Вам организаций 
                (сейчас в результате отображаются организации из категории кафе).
                
                Для этого воспользуйтесь командами:
                
                1) Задать геолокацию вручную /location_input долгота,широта
                Например, /location_input 59.968916,30.308223
                
                2) Для автоматического определения геолокации отправьте ваше местоположение через вложение.
                
                3) Получение статистики по количеству Ваших запросов /statistics
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void locationInputCommand(Long chatId, String messageGeo, Update update) {
        getGeoJsonSendMessage(chatId, messageGeo, update);
    }

    private void locationCommand(Update update) {
        Long chatId = update.getMessage().getChatId();
        String userLon = update.getMessage().getLocation().getLongitude().toString();
        String userLat = update.getMessage().getLocation().getLatitude().toString();
        getGeoJsonSendMessage(chatId, userLon + "," + userLat, update);
    }

    private void getGeoJsonSendMessage (Long chatId, String messageGeo, Update update) {
        String jsonResult = mapRest.getOrganization(messageGeo);
        String strResult;
        if (jsonResult == null) {
            strResult = "Не удалось получить указанную геолокацию. Попробуйте позже или проверьте вводимые данные.";
        } else {
            strResult = mapService.getResultSearchFromJson(jsonResult);
        }
        sendMessage(chatId, strResult);
        userActionService.insertUserAction(update);
    }

    private void staticticsCommand(Long chatId, Long userId) {
        Long countUserAction = userActionService.countUserActionByUserId(userId);
        String text = "Количество Ваших запросов: " + countUserAction;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду! Узнать доступные команды /start";
        sendMessage(chatId, text);
    }

    private void sendMessage(Long chatId, String text) {
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения", e);
        }
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }
}
