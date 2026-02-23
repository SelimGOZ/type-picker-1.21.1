package net.fortuneblack05.typerando;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fortuneblack05.typerando.commands.TypePickerCommands;

public class TypePicker implements ModInitializer {
	public static final TypePickerManager MANAGER = new TypePickerManager();

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(MANAGER::load);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> MANAGER.save(server));

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				TypePickerCommands.register(dispatcher)
		);

		System.out.println("[TypeRando] Loaded.");
	}
}