package org.polyfrost.polyzoom;

import java.util.function.Consumer;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;
import org.polyfrost.oneconfig.internal.legacy.InputConstants;

public class KeybindConfig extends Config {
	private static final int UNBOUND = -1;

	@Keybind(title = "Zoom", category = "Keybinds")
	public OneConfigKeybind zoomKey = bind(InputConstants.KEY_C, down -> PolyZoom.instance().onZoomKey(down));

	@Keybind(title = "Secondary Zoom", category = "Keybinds")
	public OneConfigKeybind secondaryZoomKey = bind(InputConstants.KEY_F6, down -> PolyZoom.instance().onSecondaryZoomKey(down));

	@Keybind(title = "Zoom In", category = "Keybinds")
	public OneConfigKeybind zoomInKey = bind(UNBOUND, down -> { if (down) PolyZoom.instance().addScrollStep(1); });

	@Keybind(title = "Zoom Out", category = "Keybinds")
	public OneConfigKeybind zoomOutKey = bind(UNBOUND, down -> { if (down) PolyZoom.instance().addScrollStep(-1); });

	public KeybindConfig() {
		super("polyzoom-keybinds.json", "PolyZoom", Category.QOL);
	}

	@Override
	protected void initialize(boolean byConfigManager) {
		super.initialize(byConfigManager);
		if (tree != null) tree.addMetadata("hidden", true);
	}

	private static OneConfigKeybind bind(int key, Consumer<Boolean> action) {
		KeybindHelper builder = KeybindHelper.builder().action(action);
		if (key != UNBOUND) builder.key(key);
		return builder.build();
	}
}
