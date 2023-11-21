package io.github.eggohito.advancement_macros;

import com.mojang.serialization.DataResult;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.macro.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvancementMacros implements ModInitializer {

	public static final String MOD_NAMESPACE = "advancement-macros";
	public static final String MAPPING_KEY = of("mapping").toString();
	public static final String CODEC_TYPE_KEY = of("trigger").toString();
	public static final String PASS_ORDER_KEY = of("pass_order").toString();

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAMESPACE);

	public static final Pattern REPLACEABLE_CHARACTERS = Pattern.compile("[ .:\\-/]");
	public static final Pattern INVALID_MACRO_CHARACTERS = Pattern.compile("[^a-zA-Z0-9_]");

	public static final RegistryKey<Registry<Macro.Type>> REGISTRY_KEY;
	public static final Registry<Macro.Type> REGISTRY;

	public static String VERSION_STRING;

	static {
		REGISTRY_KEY = RegistryKey.ofRegistry(of("macro_types"));
		REGISTRY = FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();
	}

	@Override
	public void onInitialize() {

		FabricLoader.getInstance().getModContainer(MOD_NAMESPACE).map(ModContainer::getMetadata).ifPresent(metadata -> {

			VERSION_STRING = metadata.getVersion().getFriendlyString();

			if (VERSION_STRING.contains("+")) {
				VERSION_STRING = VERSION_STRING.split("\\+")[0];
			} else if (VERSION_STRING.contains("-")) {
				VERSION_STRING = VERSION_STRING.split("-")[0];
			}

		});

		register(PlacedBlockCriterionMacro::getFactory);
		register(PlayerKilledEntityCriterionMacro::getFactory);
		register(EntityKilledPlayerCriterionMacro::getFactory);
		register(ChangedDimensionCriterionMacro::getFactory);
		register(AllayDropItemOnBlockCriterionMacro::getFactory);
		register(UsedEnderEyeCriterionMacro::getFactory);
		register(BredAnimalsCriterionMacro::getFactory);
		register(ConsumeItemCriterionMacro::getFactory);
		register(TameAnimalCriterionMacro::getFactory);
		register(PlayerInteractedWithEntityCriterionMacro::getFactory);
		register(RecipeCraftedCriterionMacro::getFactory);
		register(ThrownItemPickedUpByEntityCriterionMacro::getFactory);
		register(ThrownItemPickedUpByPlayerCriterionMacro::getFactory);
		register(FishingRodHookedCriterionMacro::getFactory);
		register(VillagerTradeCriterionMacro::getFactory);
		register(EntityHurtPlayerCriterionMacro::getFactory);
		register(PlayerHurtEntityCriterionMacro::getFactory);
		register(InventoryChangedCriterionMacro::getFactory);
		register(RecipeUnlockedCriterionMacro::getFactory);
		register(BrewedPotionCriterionMacro::getFactory);
		register(ConstructBeaconCriterionMacro::getFactory);
		register(EnchantedItemCriterionMacro::getFactory);
		register(SummonedEntityCriterionMacro::getFactory);
		register(NetherTravelCriterionMacro::getFactory);
		register(FallFromHeightCriterionMacro::getFactory);
		register(RideEntityInLavaCriterionMacro::getFactory);
		register(EnterBlockCriterionMacro::getFactory);
		register(FilledBucketCriterionMacro::getFactory);
		register(CuredZombieVillagerCriterionMacro::getFactory);
		register(LevitationCriterionMacro::getFactory);
		register(TargetHitCriterionMacro::getFactory);
		register(ChanneledLightningCriterionMacro::getFactory);
		register(PlayerGeneratesContainerLootCriterionMacro::getFactory);
		register(LightningStrikeCriterionMacro::getFactory);
		register(EffectsChangedCriterionMacro::getFactory);
		register(KilledByCrossbowCriterionMacro::getFactory);
		register(SlideDownBlockCriterionMacro::getFactory);
		register(BeeNestDestroyedCriterionMacro::getFactory);
		register(StartedRidingCriterionMacro::getFactory);
		register(ItemUsedOnBlockCriterionMacro::getFactory);
		register(KillMobNearSculkCatalystCriterionMacro::getFactory);
		register(ItemDurabilityChangedCriterionMacro::getFactory);
		register(UsedTotemCriterionMacro::getFactory);
		register(ShotCrossbowCriterionMacro::getFactory);
		register(TickCriterionMacro::getFactory);
		register(LocationCriterionMacro::getFactory);
		register(AvoidVibrationCriterionMacro::getFactory);
		register(SleptInBedCriterionMacro::getFactory);
		register(HeroOfTheVillageCriterionMacro::getFactory);
		register(UsingItemCriterionMacro::getFactory);
		register(ImpossibleCriterionMacro::getFactory);

		LOGGER.info("Advancement Macros {} has been initialized!", VERSION_STRING);

	}

	public static DataResult<String> validateMappingName(String name) {

		if (name.isEmpty()) {
			return DataResult.error(() -> "Macro mapping name cannot be empty!");
		}

		Matcher matcher = INVALID_MACRO_CHARACTERS.matcher(name);

		StringBuilder invalidCharsBuilder = new StringBuilder();
		String separator = "";

		while (matcher.find()) {

			String group = matcher.group();
			if (invalidCharsBuilder.indexOf(group) != -1) {
				continue;
			}

			invalidCharsBuilder
				.append(separator)
				.append("\"").append(group).append("\"");

			separator = ", ";

		}

		if (!invalidCharsBuilder.isEmpty()) {
			return DataResult.error(() -> "Macro mapping name \"%s\" contains invalid character(s): [%s]".formatted(name, invalidCharsBuilder));
		}

		return DataResult.success(name);

	}

	public static Identifier of(String path) {
		return new Identifier(MOD_NAMESPACE, path);
	}

	public static void register(Macro.Supplier supplier) {

		Macro.Factory factory = supplier.getFactory();

		Criterion<?> criterion = factory.getData().getLeft();
		Macro.Type macroType = factory.getData().getRight();

		Identifier macroId = Criteria.getId(criterion);
		Registry.register(REGISTRY, macroId, macroType);

	}

}