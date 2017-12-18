package cookpad.recipe;

import java.util.List;
import io.reactivex.Observable;

public interface RecipeRepository {
  Observable<Recipe> save(Recipe recipe);
  Observable<List<? extends Recipe>> findAll();
}
