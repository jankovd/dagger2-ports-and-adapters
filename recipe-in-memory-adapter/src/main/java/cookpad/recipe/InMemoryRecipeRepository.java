package cookpad.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

@Singleton
public class InMemoryRecipeRepository implements RecipeRepository {
  private final List<Recipe> recipes = new ArrayList<>(Arrays.asList(
    new Recipe("1"),
    new Recipe("2"),
    new Recipe("3")
  ));

  @Inject public InMemoryRecipeRepository() {
  }

  @Override public Observable<Recipe> save(Recipe recipe) {
    return Observable.fromCallable(() -> {
      recipes.add(0, recipe);
      return recipe;
    });
  }

  @Override public Observable<List<? extends Recipe>> findAll() {
    return Observable.fromCallable(() -> {
      return Collections.unmodifiableList(recipes);
    });
  }
}
