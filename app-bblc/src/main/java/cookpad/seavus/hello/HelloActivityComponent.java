package cookpad.seavus.hello;

import cookpad.android.PerActivity;
import cookpad.seavus.AppComponent;
import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = HelloActivityModule.class)
public interface HelloActivityComponent {
  void inject(HelloActivity activity);
}
