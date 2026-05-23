package windclan.cannibalsdelight;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class main implements ModInitializer {

	public static final String modId = "cannibalsdelight";
    public static final Logger LOGGER = LoggerFactory.getLogger(modId);

	public static final Item RAW_HUMAN_MEAT_ITEM = new Item(new Item.Properties().food(
            new FoodProperties.Builder()
					.nutrition(2)
					.saturationModifier(0.5F)
					.effect(new MobEffectInstance(MobEffects.POISON, 20*30), 0.25F)
					.effect(new MobEffectInstance(MobEffects.HUNGER, 20*30), 0.25F)
					.build()
	));
	public static final Item COOKED_HUMAN_MEAT_ITEM = new Item(new Item.Properties().food(
			new FoodProperties.Builder()
					.nutrition(5)
					.saturationModifier(0.75F)
					.build()
	));
	public static final Item MINCED_HUMAN_ITEM = new Item(new Item.Properties().food(
			new FoodProperties.Builder()
					.nutrition(1)
					.saturationModifier(0.5F)
					.effect(new MobEffectInstance(MobEffects.POISON, 20*30), 0.25F)
					.effect(new MobEffectInstance(MobEffects.HUNGER, 20*30), 0.25F)
					.build()
	));
	public static final Item HUMAN_PATTY_ITEM = new Item(new Item.Properties().food(
			new FoodProperties.Builder()
					.nutrition(2)
					.saturationModifier(0.75F)
					.build()
	));
	public static final Item HUMAN_BURGER_ITEM = new Item(new Item.Properties().food(
			new FoodProperties.Builder()
					.nutrition(15)
					.saturationModifier(0.9F)
					.build()
	));
	public static final Item PASTA_WITH_HUMAN_MEATBALLS_ITEM = new Item(new Item.Properties().food(
			new FoodProperties.Builder()
					.nutrition(20)
					.saturationModifier(1.0F)
					.build()
	));
	public static final CreativeModeTab ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(COOKED_HUMAN_MEAT_ITEM))
			.title(Component.translatable("itemGroup.cannibalsdelight.cannibalsdelight"))
			.displayItems((context, entries) -> {
				entries.accept(RAW_HUMAN_MEAT_ITEM);
				entries.accept(COOKED_HUMAN_MEAT_ITEM);
				entries.accept(MINCED_HUMAN_ITEM);
				entries.accept(HUMAN_PATTY_ITEM);
				entries.accept(HUMAN_BURGER_ITEM);
				entries.accept(PASTA_WITH_HUMAN_MEATBALLS_ITEM);
			})
			.build();

	public static final TagKey<Item> HUMAN_MEAT_KNIVES_ITAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "human_meat_knives"));
	public static final TagKey<EntityType<?>> HUMAN_ENTITIES_ETAG = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(modId, "human_entities"));

	@Override
	public void onInitialize() {
		LOGGER.info("Registering items");
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "raw_human_meat"), RAW_HUMAN_MEAT_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "cooked_human_meat"), COOKED_HUMAN_MEAT_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "minced_human"), MINCED_HUMAN_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "human_patty"), HUMAN_PATTY_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "human_burger"), HUMAN_BURGER_ITEM);
		Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(modId, "pasta_with_human_meatballs"), PASTA_WITH_HUMAN_MEATBALLS_ITEM);
		LOGGER.info("Registered.");
		LOGGER.info("Registering item groups");
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(modId, "cannibalsdelight"), ITEM_GROUP);
		LOGGER.info("Registered.");


		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity.isAlwaysTicking()) { // if the killer is player
				Player player = (Player) entity; // get entity as player
				ItemStack mainhand = player.getInventory().getSelected(); // get player's main hand item
				if (mainhand.is(HUMAN_MEAT_KNIVES_ITAG)) { // check the main hand item is in the cannibalsdelight:human_meat_knives item tag
					if (killedEntity.getType().is(HUMAN_ENTITIES_ETAG)) { // check the killed entity is in the cannibalsdelight:human_entities entity type tag
						Vec3 pos = killedEntity.position(); // get the position of the killed entity
						ItemEntity e = new ItemEntity(world, pos.x, pos.y, pos.z, new ItemStack(RAW_HUMAN_MEAT_ITEM, (int)Math.floor(Math.random()*3+0.5))); // create the meat drop entity
						world.addFreshEntity(e); // spawn in the entity - i forgot to do this and was so confused lol
					}
				}
			}
		});
	}
}