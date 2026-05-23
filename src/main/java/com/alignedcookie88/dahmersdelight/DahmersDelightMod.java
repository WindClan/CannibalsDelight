package com.alignedcookie88.dahmersdelight;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DahmersDelightMod implements ModInitializer {

	public static final String modId = "dahmersdelight";
    public static final Logger LOGGER = LoggerFactory.getLogger(modId);

	public static final Item RAW_HUMAN_MEAT_ITEM = new Item(new Item.Settings().food(
            new FoodComponent.Builder()
					.nutrition(2)
					.saturationModifier(0.5F)
					.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*30), 0.25F)
					.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*30), 0.25F)
					.build()
	));
	public static final Item COOKED_HUMAN_MEAT_ITEM = new Item(new Item.Settings().food(
			new FoodComponent.Builder()
					.nutrition(5)
					.saturationModifier(0.75F)
					.build()
	));
	public static final Item MINCED_HUMAN_ITEM = new Item(new Item.Settings().food(
			new FoodComponent.Builder()
					.nutrition(1)
					.saturationModifier(0.5F)
					.statusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*30), 0.25F)
					.statusEffect(new StatusEffectInstance(StatusEffects.HUNGER, 20*30), 0.25F)
					.build()
	));
	public static final Item HUMAN_PATTY_ITEM = new Item(new Item.Settings().food(
			new FoodComponent.Builder()
					.nutrition(2)
					.saturationModifier(0.75F)
					.build()
	));
	public static final Item HUMAN_BURGER_ITEM = new Item(new Item.Settings().food(
			new FoodComponent.Builder()
					.nutrition(15)
					.saturationModifier(0.9F)
					.build()
	));
	public static final Item PASTA_WITH_HUMAN_MEATBALLS_ITEM = new Item(new Item.Settings().food(
			new FoodComponent.Builder()
					.nutrition(20)
					.saturationModifier(1.0F)
					.build()
	));
	public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(COOKED_HUMAN_MEAT_ITEM))
			.displayName(Text.translatable("itemGroup.dahmersdelight.dahmersdelight"))
			.entries((context, entries) -> {
				entries.add(RAW_HUMAN_MEAT_ITEM);
				entries.add(COOKED_HUMAN_MEAT_ITEM);
				entries.add(MINCED_HUMAN_ITEM);
				entries.add(HUMAN_PATTY_ITEM);
				entries.add(HUMAN_BURGER_ITEM);
				entries.add(PASTA_WITH_HUMAN_MEATBALLS_ITEM);
			})
			.build();

	public static final TagKey<Item> HUMAN_MEAT_KNIVES_ITAG = TagKey.of(RegistryKeys.ITEM, Identifier.of(modId, "human_meat_knives"));
	public static final TagKey<EntityType<?>> HUMAN_ENTITIES_ETAG = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(modId, "human_entities"));

	@Override
	public void onInitialize() {
		LOGGER.info("Registering items");
		Registry.register(Registries.ITEM, Identifier.of(modId, "raw_human_meat"), RAW_HUMAN_MEAT_ITEM);
		Registry.register(Registries.ITEM, Identifier.of(modId, "cooked_human_meat"), COOKED_HUMAN_MEAT_ITEM);
		Registry.register(Registries.ITEM, Identifier.of(modId, "minced_human"), MINCED_HUMAN_ITEM);
		Registry.register(Registries.ITEM, Identifier.of(modId, "human_patty"), HUMAN_PATTY_ITEM);
		Registry.register(Registries.ITEM, Identifier.of(modId, "human_burger"), HUMAN_BURGER_ITEM);
		Registry.register(Registries.ITEM, Identifier.of(modId, "pasta_with_human_meatballs"), PASTA_WITH_HUMAN_MEATBALLS_ITEM);
		LOGGER.info("Registered.");
		LOGGER.info("Registering item groups");
		Registry.register(Registries.ITEM_GROUP, Identifier.of(modId, "dahmersdelight"), ITEM_GROUP);
		LOGGER.info("Registered.");


		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, entity, killedEntity) -> {
			if (entity.isPlayer()) { // if the killer is player
				PlayerEntity player = (PlayerEntity) entity; // get entity as player
				ItemStack mainhand = player.getInventory().getMainHandStack(); // get player's main hand item
				if (mainhand.isIn(HUMAN_MEAT_KNIVES_ITAG)) { // check the main hand item is in the dahmersdelight:human_meat_knives item tag
					if (killedEntity.getType().isIn(HUMAN_ENTITIES_ETAG)) { // check the killed entity is in the dahmersdelight:human_entities entity type tag
						Vec3d pos = killedEntity.getPos(); // get the position of the killed entity
						ItemEntity e = new ItemEntity(world, pos.x, pos.y, pos.z, new ItemStack(RAW_HUMAN_MEAT_ITEM, 1)); // create the meat drop entity
						world.spawnEntity(e); // spawn in the entity - i forgot to do this and was so confused lol
					}
				}
			}
		});
	}
}