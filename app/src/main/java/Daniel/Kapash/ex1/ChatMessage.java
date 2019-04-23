package Daniel.Kapash.ex1;
import java.util.*;


public class ChatMessage {

    private final String text;
    private final Long id;
    private final Long timeStamp;

    ChatMessage(){
        text = "";
        id = 0l;
        timeStamp = 0l;
    }

    ChatMessage(String text, Long id) {
        this.text = text;
        this.id = id;
        timeStamp = Calendar.getInstance().getTime().getTime();
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage message = (ChatMessage) o;
        return id.equals(message.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
