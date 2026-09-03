package org.polyfrost.polyzoom;

import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;
import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Include;
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;

public class ZoomConfig extends Config {
	public enum ZoomKeyBehaviour { HOLD, TOGGLE }

	@Keybind(title = "Zoom", category = "Zoom", subcategory = "Keybinds")
	public OneConfigKeybind zoomKey = KeybindHelper.builder()
		.key(Keyboard.KEY_C)
		.action((Consumer<Boolean>) down -> PolyZoom.instance().onZoomKey(down))
		.build();

	@Keybind(title = "Secondary Zoom", category = "Zoom", subcategory = "Keybinds")
	public OneConfigKeybind secondaryZoomKey = KeybindHelper.builder()
		.key(Keyboard.KEY_F6)
		.action((Consumer<Boolean>) down -> PolyZoom.instance().onSecondaryZoomKey(down))
		.build();

	@Keybind(title = "Zoom In", category = "Zoom", subcategory = "Keybinds")
	public OneConfigKeybind zoomInKey = KeybindHelper.builder()
		.action(down -> { if (down) PolyZoom.instance().addScrollStep(1); })
		.build();

	@Keybind(title = "Zoom Out", category = "Zoom", subcategory = "Keybinds")
	public OneConfigKeybind zoomOutKey = KeybindHelper.builder()
		.action(down -> { if (down) PolyZoom.instance().addScrollStep(-1); })
		.build();

	@Dropdown(title = "Zoom Key Behaviour", category = "Zoom", subcategory = "Keybinds")
	public ZoomKeyBehaviour zoomKeyBehaviour = ZoomKeyBehaviour.HOLD;

	@Slider(title = "Initial Zoom", description = "How many times closer the initial zoom is.", category = "Zoom", subcategory = "Zoom", min = 1, max = 50)
	public int initialZoom = 4;

	@Slider(title = "Zoom In Time", description = "Seconds taken to reach full zoom.", category = "Zoom", subcategory = "Zoom", min = 0, max = 5, step = 0.05f)
	public double zoomInTime = 1.0;

	@Slider(title = "Zoom Out Time", description = "Seconds taken to return to normal.", category = "Zoom", subcategory = "Zoom", min = 0, max = 5, step = 0.05f)
	public double zoomOutTime = 0.5;

	@Dropdown(title = "Zoom In Transition", category = "Zoom", subcategory = "Zoom")
	public Transition zoomInTransition = Transition.EASE_OUT_EXP;

	@Dropdown(title = "Zoom Out Transition", category = "Zoom", subcategory = "Zoom")
	public Transition zoomOutTransition = Transition.EASE_OUT_EXP;

	@Switch(title = "Affect Hand FOV", description = "Zoom the held item along with the world.", category = "Zoom", subcategory = "Zoom")
	public boolean affectHandFov = true;

	@Switch(title = "Retain Zoom Steps", description = "Keep scroll steps after letting go of the zoom key.", category = "Zoom", subcategory = "Scrolling")
	public boolean retainZoomSteps = false;

	@Switch(title = "Scroll Zoom", description = "Scroll while zooming to zoom further.", category = "Zoom", subcategory = "Scrolling")
	public boolean scrollZoom = true;

	@Switch(title = "Keybind Scrolling", description = "Use the Zoom In/Out keys instead of the scroll wheel.", category = "Zoom", subcategory = "Scrolling")
	public boolean keybindScrolling = false;

	@Slider(title = "Scroll Step Count", description = "How many scroll steps are available.", category = "Zoom", subcategory = "Scrolling", min = 0, max = 50)
	public int scrollStepCount = 10;

	@Slider(title = "Zoom Per Step", description = "Percentage zoom added per scroll step.", category = "Zoom", subcategory = "Scrolling", min = 100, max = 500)
	public int zoomPerStep = 150;

	@Slider(title = "Scroll Zoom Smoothness", category = "Zoom", subcategory = "Scrolling", min = 0, max = 100)
	public int scrollZoomSmoothness = 70;

	@Slider(title = "Relative Sensitivity", description = "How much mouse sensitivity scales down with zoom.", category = "Zoom", subcategory = "Camera", min = 0, max = 100)
	public int relativeSensitivity = 100;

	@Switch(title = "Relative View Bobbing", description = "Scale view bobbing down with zoom.", category = "Zoom", subcategory = "Camera")
	public boolean relativeViewBobbing = true;

	@Slider(title = "Cinematic Camera", description = "Amount of cinematic camera smoothing while zooming. 0 disables it.", category = "Zoom", subcategory = "Camera", min = 0, max = 100)
	public int cinematicCamera = 0;

	@Slider(title = "Secondary Zoom Amount", category = "Zoom", subcategory = "Secondary Zoom", min = 1, max = 50)
	public int secondaryZoomAmount = 4;

	@Slider(title = "Secondary Zoom In Time", category = "Zoom", subcategory = "Secondary Zoom", min = 0, max = 30, step = 0.5f)
	public double secondaryZoomInTime = 10.0;

	@Slider(title = "Secondary Zoom Out Time", category = "Zoom", subcategory = "Secondary Zoom", min = 0, max = 30, step = 0.5f)
	public double secondaryZoomOutTime = 1.0;

	@Switch(title = "Hide HUD On Secondary Zoom", category = "Zoom", subcategory = "Secondary Zoom")
	public boolean secondaryHideHUDOnZoom = true;

	// passthrough options
	@SuppressWarnings("unused") @Include public String spyglassBehaviour = "combine";
	@SuppressWarnings("unused") @Include public String spyglassOverlayVisibility = "holding";
	@SuppressWarnings("unused") @Include public String spyglassSoundBehaviour = "with_overlay";

	public ZoomConfig() {
		super("zoomify.json", null, "PolyZoom", Category.QOL);
	}
}
