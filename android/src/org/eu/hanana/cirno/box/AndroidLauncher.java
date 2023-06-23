package org.eu.hanana.cirno.box;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
	public ExecResult executeShellCmd(String base, String cmd) {
		return ShellCommandExecutor.executeCommand(base,cmd);
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
	public void log(int priority, String tag, String msg) {
		Log.println(priority,tag,msg);
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
				!Settings.System.canWrite(getApplicationContext())) {
			// 如果没有WRITE_SETTINGS权限，请求用户授权
			// 这将启动系统设置界面，允许用户授予WRITE_SETTINGS权限
			// 在设置界面返回后，您需要再次检查权限
			// 如果授权成功，您可以执行wm命令
			// 如果授权失败，您应该根据您的应用程序逻辑进行处理
			// 在授权失败时，不要执行敏感的wm命令
			requestWriteSettingsPermission(getApplicationContext());
		}
		Toast.makeText(getApplicationContext(),R.string.loading,Toast.LENGTH_SHORT).show();
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new CirnoMain(this), config);
	}
	private static void requestWriteSettingsPermission(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (context.checkSelfPermission(Manifest.permission.WRITE_SETTINGS)
					!= PackageManager.PERMISSION_GRANTED) {
				// 请求WRITE_SETTINGS权限
				// 这将弹出一个对话框，允许用户授权WRITE_SETTINGS权限
				// 在用户作出选择后，授权结果将在onActivityResult()方法中返回
				// 您需要在onActivityResult()方法中处理授权结果
				Settings.System.canWrite(context);
			}
		}
	}
	@Override
	public AndroidAudio createAudio(Context context, AndroidApplicationConfiguration config) {
		return new AsynchronousAndroidAudio(context, config);
	}
	@Override
	protected void onDestroy() {
		((CirnoMain) Gdx.app.getApplicationListener()).disposed=true;
		Gdx.app.getApplicationListener().dispose();
		super.onDestroy();
	}

}
