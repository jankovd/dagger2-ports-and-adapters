package cookpad.seavus;

import android.content.Context;
import java.util.Set;
import javax.inject.Inject;
import cookpad.OnAppStart;
import cookpad.android.BaseApp;

public class App extends BaseApp {
  @Inject Set<OnAppStart> onAppStartFunctions;
  private AppComponent appComponent;

  public static App from(Context context) {
    return (App) context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    appComponent = createComponent();
    appComponent.inject(this);
    runAppStartFunctions(onAppStartFunctions);
  }

  protected AppComponent createComponent() {
    throw new AssertionError("Create in flavor");
  }

  public AppComponent component() {
    return appComponent;
  }
}
