package cookpad.seavus.hello;

import android.app.Activity;
import cookpad.android.PerActivity;
import cookpad.android.design.SnackbarManager;
import dagger.Module;
import dagger.Provides;

@Module
public final class HelloActivityModule {
  private final Activity activity;

  public HelloActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @PerActivity SnackbarManager provideSnackbarManager() {
    return new SnackbarManager(activity.findViewById(android.R.id.content));
  }
}
