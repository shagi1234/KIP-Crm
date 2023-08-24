package tm.payhas.crm.presentation.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelChatRoomFactory implements ViewModelProvider.Factory {
    private Application application;
    private int roomId;

    public ViewModelChatRoomFactory(Application application, int roomId) {
        this.application = application;
        this.roomId = roomId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelChatRoom.class)) {
            return (T) new ViewModelChatRoom(application, roomId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}