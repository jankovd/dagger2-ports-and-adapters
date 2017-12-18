package cookpad.seavus;

public class ReleaseApp extends App {

  @Override protected AppComponent createComponent() {
    return DaggerAppComponent.create();
  }
}
