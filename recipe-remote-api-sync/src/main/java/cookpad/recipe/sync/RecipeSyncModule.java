package cookpad.recipe.sync;

import cookpad.OnAppStart;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public abstract class RecipeSyncModule {
  @Binds @IntoSet abstract OnAppStart bindOnAppStart(
    OnRemoteApiUpdatesWriteToRepository onAppStart);
}
