package tm.payhas.crm.presentation.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.domain.useCases.UseCaseGroup;

public class ViewModelGroup extends AndroidViewModel {
    private UseCaseGroup useCaseGroup;

    public ViewModelGroup(Application application) {
        super(application);
        useCaseGroup = new UseCaseGroup(application.getApplicationContext());
    }

    public void connectAndUpdate() {
        useCaseGroup.getConnectedAndUpdated();
    }

    public LiveData<List<EntityGroup>> getAllGroups() {
        return useCaseGroup.getAllGroups();
    }


}
