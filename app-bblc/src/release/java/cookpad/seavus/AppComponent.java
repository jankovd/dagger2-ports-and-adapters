package cookpad.seavus;

import javax.inject.Singleton;
import cookpad.recipe.InMemoryRecipeModule;
import cookpad.recipe.firebase.FirebaseRemoteApiModule;
import cookpad.recipe.sync.RecipeSyncModule;
import dagger.Component;

@Singleton
@Component(modules = {
  RecipeSyncModule.class,
  InMemoryRecipeModule.class,
  FirebaseRemoteApiModule.class,
})
public interface AppComponent extends AppComponentSpec {
}
