package cookpad.recipe;

import io.reactivex.Observable;

public interface RemoteService {
  Observable<Recipe> recipeUpdates();
}
