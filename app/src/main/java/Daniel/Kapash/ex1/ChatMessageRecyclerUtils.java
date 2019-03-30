package Daniel.Kapash.ex1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMessageRecyclerUtils {

    static class ChatMessageCallback extends DiffUtil.ItemCallback<ChatMessage> {

        @Override
        public boolean areItemsTheSame(@NonNull ChatMessage message1, @NonNull ChatMessage message2) {
            return message1.equals(message2);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ChatMessage message1, @NonNull ChatMessage message2) {
            return message1.getText().equals(message2.getText());
        }
    }


//    static class ChatMessageAdapter extends List



}
