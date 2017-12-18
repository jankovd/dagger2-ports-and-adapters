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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bluelinelabs.conductor.Controller;
import com.bluelinelabs.conductor.ControllerChangeHandler;
import com.bluelinelabs.conductor.ControllerChangeType;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseController extends Controller {
  private Unbinder unbinder;
  private boolean hasExited;
  private boolean isInitInContext;
  private Context viewContext;

  protected BaseController() {}

  protected BaseController(Bundle args) {
    super(args);
  }

  protected abstract View inflateView(@NonNull LayoutInflater inflater,
    @NonNull ViewGroup container);

  protected final Context getContext() {
    if (viewContext == null) {
      return getApplicationContext();
    }
    return viewContext;
  }

  /**
   * Called right before the first time this Controller is attached to its host ViewGroup.
   */
  protected void initInContext(Context context) {}

  @NonNull @Override protected final View onCreateView(@NonNull LayoutInflater inflater, @NonNull
    ViewGroup container) {
    View view = inflateView(inflater, container);
    unbinder = ButterKnife.bind(this, view);
    viewContext = view.getContext();
    if (!isInitInContext) {
      isInitInContext = true;
      initInContext(viewContext);
    }
    onViewBound(view);
    onAfterViewBound();
    return view;
  }

  protected void onViewBound(@NonNull View view) {}

  protected void onAfterViewBound() {}

  @Override protected void onDestroyView(@NonNull View view) {
    super.onDestroyView(view);
    viewContext = null;
    unbinder.unbind();
    unbinder = null;
  }

  @Override public void onDestroy() {
    super.onDestroy();
    if (hasExited) {
      BaseApp.from(getApplicationContext()).refWatcher().watch(this);
    }
  }

  @Override protected void onChangeEnded(@NonNull ControllerChangeHandler changeHandler,
    @NonNull ControllerChangeType changeType) {
    super.onChangeEnded(changeHandler, changeType);
    hasExited = !changeType.isEnter;
    if (isDestroyed()) {
      BaseApp.from(getApplicationContext()).refWatcher().watch(this);
    }
  }
}
