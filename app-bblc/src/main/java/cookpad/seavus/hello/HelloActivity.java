package cookpad.seavus.hello;

import android.os.Bundle;
import android.widget.TextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import cookpad.android.BaseActivity;
import cookpad.android.design.SnackbarManager;
import cookpad.recipe.RecipeService;
import cookpad.seavus.App;
import cookpad.seavus.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static java.lang.String.format;

public class HelloActivity extends BaseActivity {
  @Inject SnackbarManager snackbarManager;
  @Inject RecipeService recipeService;
  @BindView(R.id.recipes_found) TextView recipesFound;
  private Disposable recipesReadScheduledRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    ((HelloActivityComponent) activityComponent()).inject(this);

    recipeService.findAll()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(recipes -> {
        snackbarManager.showWithLongDuration("Reading recipes...");
      });
  }

  @Override protected Class componentType() {
    return HelloActivityComponent.class;
  }

  @Override protected Object createComponent() {
    return DaggerHelloActivityComponent.builder()
      .appComponent(App.from(this).component())
      .helloActivityModule(new HelloActivityModule(this))
      .build();
  }

  @Override protected void onStart() {
    super.onStart();
    recipesReadScheduledRequest = Observable.interval(2, TimeUnit.SECONDS)
      .delay(1, TimeUnit.SECONDS)
      .subscribeOn(Schedulers.io())
      .flatMap(aLong -> recipeService.findAll())
      .map(recipes -> format("Found %d recipes", recipes.size()))
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(RxTextView.text(recipesFound));
  }

  @Override protected void onStop() {
    super.onStop();
    recipesReadScheduledRequest.dispose();
    recipesReadScheduledRequest = null;
  }
}
