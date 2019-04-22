package Daniel.Kapash.ex1;
import java.util.*;


public class ChatMessage {

    private final String text;
    private final int id;
    private final long timeStamp;

    ChatMessage(){
        text = "";
        id = -1;
        timeStamp = 0;
    }

    ChatMessage(String text, int id) {
        this.text = text;
        this.id = id;
        timeStamp = Calendar.getInstance().getTime().getTime();
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage message = (ChatMessage) o;
        return id == message.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
