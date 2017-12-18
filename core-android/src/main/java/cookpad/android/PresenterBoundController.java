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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

public abstract class PresenterBoundController extends BaseController {
  private ViewPresenter viewPresenter;

  public PresenterBoundController() {
  }

  public PresenterBoundController(Bundle args) {
    super(args);
  }

  protected final void bindTo(ViewPresenter presenter) {
    viewPresenter = presenter;
    presenter.onEnterScope();
  }

  @Override protected void onAfterViewBound() {
    super.onAfterViewBound();
    //noinspection unchecked
    viewPresenter.takeView(this);
  }

  @Override protected void onDestroyView(@NonNull View view) {
    super.onDestroyView(view);
    //noinspection unchecked
    viewPresenter.dropView(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    viewPresenter.onExitScope();
  }
}
