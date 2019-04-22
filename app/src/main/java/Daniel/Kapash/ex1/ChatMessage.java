package Daniel.Kapash.ex1;
import java.util.*;


public class ChatMessage implements Comparable<ChatMessage> {

    private final String text;
    private final int id;
    private final Date timeStamp;

    ChatMessage(String text, int id) {
        this.text = text;
        this.id = id;
        timeStamp = Calendar.getInstance().getTime();
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(ChatMessage o) {
        return timeStamp.compareTo(o.getTimeStamp());
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
