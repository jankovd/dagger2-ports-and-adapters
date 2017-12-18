package cookpad.recipe.firebase;

import javax.inject.Inject;
import javax.inject.Singleton;
import cookpad.OnAppStart;
import cookpad.rx.RxNotification;
import io.reactivex.Observable;

@Singleton
public class ActivateFirebaseOnAppStart implements OnAppStart {
  private final Firebase firebase;

  @Inject public ActivateFirebaseOnAppStart(Firebase firebase) {
    this.firebase = firebase;
  }

  @Override public Observable<Object> onStart() {
    return Observable.fromCallable(() -> {
      firebase.activate();
      return RxNotification.COMPLETE;
    });
  }
}
