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

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import cookpad.OnAppStart;
import cookpad.rx.RxConsumers;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseApp extends Application {
  private RefWatcher refWatcher;

  public static BaseApp from(Context context) {
    return (BaseApp) context.getApplicationContext();
  }

  protected final void runAppStartFunctions(Iterable<OnAppStart> appStarts) {
    Observable.fromIterable(appStarts)
      .flatMap(OnAppStart::onStart)
      .subscribeOn(Schedulers.io())
      .subscribe(RxConsumers.swallow(), RxConsumers.error("App.init"));
  }

  protected RefWatcher setUpLeakCanary() {
    if (BuildConfig.DEBUG) {
      return RefWatcher.DISABLED;
    }
    return LeakCanary.install(this);
  }

  public final RefWatcher refWatcher() {
    if (refWatcher == null) {
      refWatcher = setUpLeakCanary();
    }
    return refWatcher;
  }
}
