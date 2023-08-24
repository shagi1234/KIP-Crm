package tm.payhas.crm.presentation.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityUserInfo;
import tm.payhas.crm.domain.useCases.UseCaseUsers;

public class ViewModelUser extends AndroidViewModel {
    UseCaseUsers useCaseUsers;

    public ViewModelUser(@NonNull Application application) {
        super(application);
        useCaseUsers = new UseCaseUsers(application.getApplicationContext());
    }

    public LiveData<List<EntityUserInfo>> getAllUsers() {
        return useCaseUsers.getAllUsers();
    }

    public void getConnectedAndUpdate() {
        useCaseUsers.connectAndUpdate();
    }

    public void resetMessageCountForUser(int userId) {
        useCaseUsers.resetMessageCountForUser(userId);
    }
}
