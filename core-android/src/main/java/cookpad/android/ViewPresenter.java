/*
 * Copyright (c) 2017 Dejan Jankov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cookpad.android;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Every interaction with the presenter must be on the main thread, in both directions.
 */
@UiThread
public abstract class ViewPresenter<View> {

  private Function<Object, Boolean> hasViewPredicate;
  private boolean inViewPresenterScope;
  private boolean destroyAfterDrop;
  private View view;
  private CompositeDisposable dropViewSubscriptions;
  private CompositeDisposable destroySubscriptions;

  public final void onEnterScope() {
    inViewPresenterScope = true;
    destroyAfterDrop = false;
  }

  public final void takeView(View view) {
    if (view == null) {
      throw new NullPointerException("view == null");
    }
    if (this.view != null) {
      throw new IllegalStateException("takeView before previous view is dropped.");
    }
    this.view = view;
    onTakeView(view);
  }

  protected void onTakeView(View v) {}

  public final void dropView(View view) {
    if (view == null) {
      throw new NullPointerException("null dropped view.");
    }
    if (this.view != view) {
      return;
    }
    onDropView();
    if (dropViewSubscriptions != null) {
      dropViewSubscriptions.dispose();
      dropViewSubscriptions = null;
    }
    this.view = null;
    if (!inViewPresenterScope || destroyAfterDrop) {
      destroyAfterDrop = false;
      onDestroy();
    }
  }

  /**
   * Called to abandon control of the view, e.g. when the view is detached.
   * A call to {@linkplain #onDestroy()} will be followed if the view is not expected
   * to be re-taken, otherwise {@linkplain #onTakeView} might be called in the near future.
   */
  protected void onDropView() {}

  public final void onExitScope() {
    if (view != null) {
      destroyAfterDrop = true;
    } else {
      onDestroy();
    }
  }

  /**
   * The presenter is going away and will never again regain control of a view.
   */
  @CallSuper protected void onDestroy() {
    if (destroySubscriptions != null) {
      destroySubscriptions.dispose();
      destroySubscriptions = null;
    }
  }

  protected void untilDropView(Disposable subscription) {
    if (subscription == null) {
      return;
    }
    if (dropViewSubscriptions == null) {
      dropViewSubscriptions = new CompositeDisposable();
    }
    dropViewSubscriptions.add(subscription);
  }

  protected void untilDestroy(Disposable subscription) {
    if (subscription == null) {
      return;
    }
    if (destroySubscriptions == null) {
      destroySubscriptions = new CompositeDisposable();
    }
    destroySubscriptions.add(subscription);
  }

  protected final View getView() {
    return view;
  }

  public final boolean hasView() {
    return view != null;
  }

  protected final <E> Function<E, Boolean> hasViewPredicate() {
    if (hasViewPredicate == null) {
      hasViewPredicate = o -> hasView();
    }
    //noinspection unchecked
    return (Function<E, Boolean>) hasViewPredicate;
  }
}
