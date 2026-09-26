package org.polyfrost.polyzoom;

import java.util.Map;
import java.util.function.Consumer;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.Node;
import org.polyfrost.oneconfig.api.config.v1.Properties;
import org.polyfrost.oneconfig.api.config.v1.Property;
import org.polyfrost.oneconfig.api.config.v1.Tree;
import org.polyfrost.oneconfig.api.config.v1.annotations.Keybind;
import org.polyfrost.oneconfig.api.ui.v1.keybind.KeybindHelper;
import org.polyfrost.oneconfig.api.ui.v1.keybind.OneConfigKeybind;
import org.polyfrost.oneconfig.internal.legacy.InputConstants;

public class KeybindConfig extends Config {
	private static final int UNBOUND = -1;

	@Keybind(title = "Zoom", subcategory = "Keybinds")
	public OneConfigKeybind zoomKey = bind(InputConstants.KEY_C, down -> PolyZoom.instance().onZoomKey(down));

	@Keybind(title = "Secondary Zoom", subcategory = "Keybinds")
	public OneConfigKeybind secondaryZoomKey = bind(InputConstants.KEY_F6, down -> PolyZoom.instance().onSecondaryZoomKey(down));

	@Keybind(title = "Zoom In", subcategory = "Keybinds")
	public OneConfigKeybind zoomInKey = bind(UNBOUND, down -> { if (down) PolyZoom.instance().addScrollStep(1); });

	@Keybind(title = "Zoom Out", subcategory = "Keybinds")
	public OneConfigKeybind zoomOutKey = bind(UNBOUND, down -> { if (down) PolyZoom.instance().addScrollStep(-1); });

	public KeybindConfig() {
		super("polyzoom-keybinds.json", "PolyZoom", Category.QOL);
	}

	@Override
	protected Tree makeTree() {
		Tree tree = super.makeTree();
		if (tree != null) tree.addMetadata("hidden", true);
		return tree;
	}

	// add a view of every keybind to another config's tree
	void addViews(Tree target) {
		if (tree == null) initialize(false);
		if (tree == null) return;
		for (Node node : tree.map.values()) {
			if (node instanceof Property) target.put(view(node.getID()));
		}
	}

	// void typed so the config doesn't save this view, reads/writes go to the real property
	@SuppressWarnings({"unchecked", "rawtypes"})
	private Property<?> view(String id) {
		Property<Object> real = real(id);
		Property<Object> view = Properties.functional(() -> real(id).get(), value -> {
			real(id).set(value);
			save();
		}, id, real.getTitle(), real.description, (Class) Void.class);
		Map<String, Object> metadata = real.getMetadata();
		if (metadata != null) view.addMetadata(metadata);
		view.addMetadata("oc_no_mc_mirror", true);
		return view;
	}

	private Property<Object> real(String id) {
		return Property.recast(getProperty(id));
	}

	private static OneConfigKeybind bind(int key, Consumer<Boolean> action) {
		KeybindHelper builder = KeybindHelper.builder().action(action);
		if (key != UNBOUND) builder.key(key);
		return builder.build();
	}
}
