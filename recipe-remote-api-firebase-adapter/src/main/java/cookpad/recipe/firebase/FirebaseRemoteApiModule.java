package cookpad.recipe.firebase;

import cookpad.OnAppStart;
import cookpad.recipe.RemoteService;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public abstract class FirebaseRemoteApiModule {
  @Binds abstract RemoteService bindRemoteService(FirebaseRemoteService remoteService);
  @Binds @IntoSet abstract OnAppStart bindOnAppStart(ActivateFirebaseOnAppStart onAppStart);
}
