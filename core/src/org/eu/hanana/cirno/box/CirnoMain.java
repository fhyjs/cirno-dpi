package org.eu.hanana.cirno.box;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.ButtonBar;
import com.kotcrab.vis.ui.widget.VisDialog;

import de.tomgrill.gdxdialogs.core.GDXDialogs;
import de.tomgrill.gdxdialogs.core.GDXDialogsSystem;
import de.tomgrill.gdxdialogs.core.dialogs.GDXButtonDialog;
import jdk.internal.org.jline.utils.Log;

public class CirnoMain extends ApplicationAdapter {
	private Stage stage;
	private Slider slider;
	GDXDialogs dialogs;
	private final PlatformSpecificCode platformSpecificCode;
	TextField cdpi;
	Skin skin;
	SpriteBatch batch;
	private CheckBox root;
	Label tip_root;
	public CirnoMain(PlatformSpecificCode platformSpecificCode){
		this.platformSpecificCode = platformSpecificCode;
	}
	public boolean isroot;
	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		VisUI.load(new Skin(Gdx.files.internal("skin/x2/uiskin.json")));
		slider = new Slider(0, 1, 1, false, skin);
		batch = new SpriteBatch();
		dialogs = GDXDialogsSystem.install();
		stage = new Stage();
		Table table = new Table(skin);
		table.setFillParent(true);
		Button add=new Button(skin);
		Button minus=new Button(skin);
		add.add(new Label("+",skin)).width(20);
		minus.add(new Label("-",skin)).width(20);
		table.add(add);
		table.row();
		table.add(minus);
		table.row();
		table.add(slider).width(300).height(20).pad(10); // 设置滑块的大小和边距
		Gdx.input.setInputProcessor(new CirnoInputProcessor());
		CirnoInputProcessor.INSTANCE.inputProcessors.add(stage);
		table.row();
		cdpi=new TextField("loading",skin);
		cdpi.setDisabled(true);
		table.add(cdpi).width(200).height(30).pad(10);
		table.row();
		Button apply=new Button(skin);
		init();
		try {
			apply.add(new Label(platformSpecificCode.getStringResource("apply"), skin));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		add.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slider.setValue(slider.getValue()+10);
			}
		});
		minus.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				slider.setValue(slider.getValue()-10);
			}
		});
		apply.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (isroot) {
					// 按钮点击事件处理逻辑
					ExecResult result = platformSpecificCode.executeShellCmd("su","wm density " + (int) slider.getValue());
					VisDialog dialog;
					try {
						dialog = Dialogs.showOKDialog(stage, ((result.success && !result.error) ? platformSpecificCode.getStringResource("success") : platformSpecificCode.getStringResource("fail")) + ":" + result.exitCode, result.output);
						dialog.setSkin(skin);
						platformSpecificCode.log(3, CirnoMain.class.getName(), result.output);
					} catch (NoSuchFieldException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					Timer.schedule(new Timer.Task() {
						@Override
						public void run() {
							dialog.hide(); // 关闭对话框
							init();
						}
					}, 1f);
				}
			}
		});
		table.add(apply);
		root=new CheckBox("",skin);
		table.row();
		table.add(root);
		tip_root=new Label("",skin);
		root.add(tip_root);
		stage.addActor(table);
	}
	public void init(){
		int dpi=platformSpecificCode.getDeviceDpi();
		slider.setRange(dpi-50,dpi+50);
		slider.setValue(dpi);
	}
	@Override
	public void render () {
		isroot=root.isChecked();
		ScreenUtils.clear(1, 0, 0, 1);
		try {
			tip_root.setText(platformSpecificCode.getStringResource(isroot?"root":"non"));
			cdpi.setText(platformSpecificCode.getStringResource("current")+":"+ slider.getValue());
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void dispose () {
		VisUI.dispose();
		batch.dispose();
		stage.dispose();
	}
}
