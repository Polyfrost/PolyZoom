package org.polyfrost.polyzoom;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Include;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public class ZoomConfig extends Config {
	@Dropdown(title = "Zoom Key Behaviour", subcategory = "Keybinds", options = {"hold", "toggle"})
	public String zoomKeyBehaviour = "hold";

	@Slider(title = "Initial Zoom", description = "How many times closer the initial zoom is.", subcategory = "Zoom", min = 1, max = 50)
	public int initialZoom = 4;

	@Slider(title = "Zoom In Time", description = "Seconds taken to reach full zoom.", subcategory = "Zoom", min = 0, max = 5, step = 0.05f)
	public double zoomInTime = 1.0;

	@Slider(title = "Zoom Out Time", description = "Seconds taken to return to normal.", subcategory = "Zoom", min = 0, max = 5, step = 0.05f)
	public double zoomOutTime = 0.5;

	@Dropdown(title = "Zoom In Transition", subcategory = "Zoom", options = {"instant", "linear", "ease_in_sine", "ease_out_sine", "ease_in_out_sine", "ease_in_quad",
		"ease_out_quad", "ease_in_out_quad", "ease_in_cubic", "ease_out_cubic", "ease_in_out_cubic",
		"ease_in_exp", "ease_out_exp", "ease_in_out_exp"})
	public String zoomInTransition = "ease_out_exp";

	@Dropdown(title = "Zoom Out Transition", subcategory = "Zoom", options = {"instant", "linear", "ease_in_sine", "ease_out_sine", "ease_in_out_sine", "ease_in_quad",
		"ease_out_quad", "ease_in_out_quad", "ease_in_cubic", "ease_out_cubic", "ease_in_out_cubic",
		"ease_in_exp", "ease_out_exp", "ease_in_out_exp"})
	public String zoomOutTransition = "ease_out_exp";

	@Switch(title = "Affect Hand FOV", description = "Zoom the held item along with the world.", subcategory = "Zoom")
	public boolean affectHandFov = true;

	@Switch(title = "Retain Zoom Steps", description = "Keep scroll steps after letting go of the zoom key.", subcategory = "Scrolling")
	public boolean retainZoomSteps = false;

	@Switch(title = "Scroll Zoom", description = "Scroll while zooming to zoom further.", subcategory = "Scrolling")
	public boolean scrollZoom = true;

	@Switch(title = "Keybind Scrolling", description = "Use the Zoom In/Out keys instead of the scroll wheel.", subcategory = "Scrolling")
	public boolean keybindScrolling = false;

	@Slider(title = "Scroll Step Count", description = "How many scroll steps are available.", subcategory = "Scrolling", min = 0, max = 50)
	public int scrollStepCount = 10;

	@Slider(title = "Zoom Per Step", description = "Percentage zoom added per scroll step.", subcategory = "Scrolling", min = 100, max = 500)
	public int zoomPerStep = 150;

	@Slider(title = "Scroll Zoom Smoothness", subcategory = "Scrolling", min = 0, max = 100)
	public int scrollZoomSmoothness = 70;

	@Slider(title = "Relative Sensitivity", description = "How much mouse sensitivity scales down with zoom.", subcategory = "Camera", min = 0, max = 100)
	public int relativeSensitivity = 100;

	@Switch(title = "Relative View Bobbing", description = "Scale view bobbing down with zoom.", subcategory = "Camera")
	public boolean relativeViewBobbing = true;

	@Slider(title = "Cinematic Camera", description = "Amount of cinematic camera smoothing while zooming. 0 disables it.", subcategory = "Camera", min = 0, max = 100)
	public int cinematicCamera = 0;

	@Slider(title = "Secondary Zoom Amount", subcategory = "Secondary Zoom", min = 1, max = 50)
	public int secondaryZoomAmount = 4;

	@Slider(title = "Secondary Zoom In Time", subcategory = "Secondary Zoom", min = 0, max = 30, step = 0.5f)
	public double secondaryZoomInTime = 10.0;

	@Slider(title = "Secondary Zoom Out Time", subcategory = "Secondary Zoom", min = 0, max = 30, step = 0.5f)
	public double secondaryZoomOutTime = 1.0;

	@Switch(title = "Hide HUD On Secondary Zoom", subcategory = "Secondary Zoom")
	public boolean secondaryHideHUDOnZoom = true;

	// passthrough options
	@SuppressWarnings("unused") @Include public String spyglassBehaviour = "combine";
	@SuppressWarnings("unused") @Include public String spyglassOverlayVisibility = "holding";
	@SuppressWarnings("unused") @Include public String spyglassSoundBehaviour = "with_overlay";

	private final KeybindConfig keybinds;

	public ZoomConfig(KeybindConfig keybinds) {
		super("zoomify.json", null, "PolyZoom", Category.QOL);
		this.keybinds = keybinds;
	}

	@Override
	protected Tree makeTree() {
		Tree tree = super.makeTree();
		if (tree != null) keybinds.addViews(tree);
		return tree;
	}
}
