package cookpad.recipe.sync;

import javax.inject.Inject;
import javax.inject.Singleton;
import cookpad.OnAppStart;
import cookpad.recipe.RecipeService;
import cookpad.recipe.RemoteService;
import cookpad.rx.RxNotification;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class OnRemoteApiUpdatesWriteToRepository implements OnAppStart {
  private final RecipeService recipeService;
  private final RemoteService remoteApi;

  @Inject public OnRemoteApiUpdatesWriteToRepository(RecipeService repository,
    RemoteService remoteApi) {
    this.recipeService = repository;
    this.remoteApi = remoteApi;
  }

  @Override public Observable<Object> onStart() {
    return Observable.fromCallable(() -> {
      activate();
      return RxNotification.COMPLETE;
    });
  }

  private void activate() {
    remoteApi.recipeUpdates()
      .subscribeOn(Schedulers.io())
      .flatMap(recipeService::create)
      .subscribe();
  }
}
