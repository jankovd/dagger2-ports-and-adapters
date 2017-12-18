package cookpad.seavus;

public class DebugApp extends App {

  @Override protected AppComponent createComponent() {
    return DaggerAppComponent.create();
  }
}
