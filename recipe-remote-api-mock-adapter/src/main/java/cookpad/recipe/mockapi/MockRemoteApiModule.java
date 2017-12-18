package cookpad.recipe.mockapi;

import cookpad.recipe.RemoteService;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class MockRemoteApiModule {
  @Binds abstract RemoteService bindRemoteService(MockRemoteService remoteService);
}
