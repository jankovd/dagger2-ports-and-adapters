package cookpad.recipe.firebase;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class Firebase {
  private final PublishSubject<FirebaseRecipe> updatesStream;

  @Inject public Firebase() {
    updatesStream = PublishSubject.create();
  }

  public void activate() {
    // subscribe to firebase events...
    Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
      .map(time -> new FirebaseRecipe(String.valueOf(time)))
      .subscribe(updatesStream);
  }

  public Observable<FirebaseRecipe> updates() {
    return updatesStream;
  }
}
