package com.xenoage.zong.player;

import static com.xenoage.zong.desktop.gui.utils.Dialog.dialog;
import static com.xenoage.zong.player.Player.pApp;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;

import com.xenoage.utils.jse.lang.LangManager;
import com.xenoage.utils.jse.lang.LangResourceBundle;
import com.xenoage.zong.Voc;
import com.xenoage.zong.desktop.gui.utils.ResourceUpdater;
import com.xenoage.zong.gui.PlayerFrame;

/**
 * JavaFX application for the {@link Player}.
 * 
 * @author Andreas Wenger
 */
public class PlayerFXApp
	extends Application {
	
	@Getter private static PlayerFXApp instance = null;
	@Getter private PlayerFrame frame = null;
	

	@Override public void start(Stage stage)
		throws Exception {
		instance = this;
		//create frame
		frame = dialog(PlayerFrame.class, stage);
		frame.setTitle(pApp().getName());
		//show frame
		frame.show();
		//test: translate
		LangManager.loadLanguage("en");
		ResourceUpdater.updateScene(frame,
			PlayerFrame.class.getResourceAsStream("PlayerFrame.fxml"), new LangResourceBundle(Voc.values()));
	}

	@Override public void stop()
		throws Exception {
		super.stop();
		pApp().close();
	}

}
