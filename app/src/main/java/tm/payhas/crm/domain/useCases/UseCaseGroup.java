package tm.payhas.crm.domain.useCases;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import tm.payhas.crm.data.localdb.entity.EntityGroup;
import tm.payhas.crm.data.localdb.repository.RepositoryGroup;

public class UseCaseGroup {

    private RepositoryGroup repositoryGroup;
    private Context context;
    private LiveData<List<EntityGroup>> allGroups;

    public UseCaseGroup(Context context) {
        this.context = context;
        repositoryGroup = new RepositoryGroup(context);
        allGroups = repositoryGroup.getGroups();
    }

    public void getConnectedAndUpdated() {
        repositoryGroup.fetchAndInsertGroups();
    }

    public EntityGroup getOneGroupInfo(int groupId) {
        return repositoryGroup.getOneGroup(groupId);
    }

    public LiveData<List<EntityGroup>> getAllGroups() {
        return allGroups;
    }

    public void deleteGroup(EntityGroup group) {
        repositoryGroup.deleteGroup(group);
    }

    public void updateGroup(EntityGroup group) {
        repositoryGroup.updateGroup(group);
    }

    public void deleteAllGroups() {
        repositoryGroup.deleteAllGroups();
    }
}