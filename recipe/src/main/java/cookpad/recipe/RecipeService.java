package cookpad.recipe;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

@Singleton
public class RecipeService {
  private final RecipeRepository recipeRepository;

  @Inject RecipeService(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  public Observable<List<? extends Recipe>> findAll() {
    return recipeRepository.findAll();
  }

  public Observable<Recipe> create(String recipeName) {
    return recipeRepository.save(new Recipe(recipeName));
  }

  public Observable<Recipe> create(Recipe recipe) {
    return recipeRepository.save(recipe);
  }
}
