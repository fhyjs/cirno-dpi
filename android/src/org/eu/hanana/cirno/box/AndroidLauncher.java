package org.eu.hanana.cirno.box;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AsynchronousAndroidAudio;

public class AndroidLauncher extends AndroidApplication implements PlatformSpecificCode{
	@Override
	public String getStringResource(String resourceId) throws NoSuchFieldException, IllegalAccessException {
		Resources resources = getApplicationContext().getResources();
		return resources.getString((Integer) R.string.class.getField(resourceId).get(null));
	}

	@Override
	public ExecResult executeShellCmd(String cmd) {
		return ShellCommandExecutor.executeCommand(cmd);
	}

	@Override
	public int getDeviceDpi() {
		Context context = getApplicationContext();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		DisplayMetrics displayMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displayMetrics);

		return displayMetrics.densityDpi;
	}
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new CirnoMain(this), config);
	}
	@Override
	public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
		return new AsynchronousAndroidAudio(context, config);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
}
