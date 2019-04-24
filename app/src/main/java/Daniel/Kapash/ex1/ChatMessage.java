package Daniel.Kapash.ex1;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.*;


public class ChatMessage {

    private final String text;
    private final Long id;
    private final String timeStamp;
    private final String instanceID;

    ChatMessage(){
        text = "";
        id = 0l;
        timeStamp = "";
        instanceID = "";
    }

    ChatMessage(String text, Long id) {
        this.text = text;
        this.id = id;
        timeStamp = Calendar.getInstance().getTime().toString();
        instanceID = FirebaseInstanceId.getInstance().getId();
    }

    public String getText() {
        return text;
    }

    public Long getId() {
        return id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getInstanceID() {
        return instanceID;
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
