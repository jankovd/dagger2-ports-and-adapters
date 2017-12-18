package cookpad.seavus;

import cookpad.recipe.RecipeService;

public interface AppComponentSpec {
  void inject(App app);
  RecipeService recipeService();
}
