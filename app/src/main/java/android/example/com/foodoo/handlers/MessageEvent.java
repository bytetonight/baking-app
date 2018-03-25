package android.example.com.foodoo.handlers;

/**
 * Created by ByteTonight on 11.06.2017.
 */

public class MessageEvent {
    private final String message;
    private final String messageType;

    public String getMessage() {
        return message;
    }

    public String getMessageType() {
        return messageType;
    }

    public MessageEvent(String messageType, String message) {
        this.message = message;
        this.messageType = messageType;
    }
}
