package cookpad.recipe.mockapi;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import cookpad.recipe.Recipe;
import cookpad.recipe.RemoteService;
import io.reactivex.Observable;

@Singleton
public class MockRemoteService implements RemoteService {
  @Inject public MockRemoteService() {
  }

  @Override public Observable<Recipe> recipeUpdates() {
    return Observable.just(new Recipe("Single added recipe"))
      .delay(8, TimeUnit.SECONDS);
  }
}
