package cookpad.recipe.firebase;

import javax.inject.Inject;
import javax.inject.Singleton;
import cookpad.recipe.Recipe;
import cookpad.recipe.RemoteService;
import io.reactivex.Observable;

@Singleton
public class FirebaseRemoteService implements RemoteService {
  private final Firebase firebase;

  @Inject public FirebaseRemoteService(Firebase firebase) {
    this.firebase = firebase;
  }

  @Override public Observable<Recipe> recipeUpdates() {
    return firebase.updates().map(recipe -> new Recipe(recipe.name));
  }
}
