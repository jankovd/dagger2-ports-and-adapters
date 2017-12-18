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
import android.support.v7.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {
  private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
  private static final AtomicLong NEXT_ID = new AtomicLong(0);

  private long activityId;
  private Object activityComponent;

  public static <T> T activityComponent(Context context, Class<T> component) {
    //noinspection unchecked
    return (T) context.getSystemService(component.getName());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityId = savedInstanceState != null
      ? savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
    if (usesConfigPersistentComponent()) {
      activityComponent = createComponentWithConfigPersistentComponent();
    } else {
      activityComponent = createComponent();
    }
  }

  protected final /*code not copied*/ boolean usesConfigPersistentComponent() {
    return false;
  }

  private Object createComponentWithConfigPersistentComponent() {
    throw new AssertionError();
  }

  protected Object createComponent() { return null; }

  protected Class componentType() { return null; }

  protected <T> T activityComponent() {
    //noinspection unchecked
    return (T) activityComponent;
  }

  @Override public Object getSystemService(@NonNull String name) {
    if (activityComponent != null) {
      Class componentType = componentType();
      if (componentType != null && componentType.getName().equals(name)) {
        return activityComponent;
      }
    }
    return super.getSystemService(name);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putLong(KEY_ACTIVITY_ID, activityId);
  }

  @Override
  protected void onDestroy() {
    BaseApp.from(this).refWatcher().watch(this);
    super.onDestroy();
  }
}
