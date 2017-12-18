package cookpad.recipe;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class InMemoryRecipeModule {
  @Binds abstract RecipeRepository bindRecipeRepository(InMemoryRecipeRepository repository);
}
