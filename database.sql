ALTER TABLE "public"."wieldable_items" DROP CONSTRAINT "fk_wieldable_items" CASCADE;
ALTER TABLE "public"."wieldable_item_stats" DROP CONSTRAINT "fk_wieldable_item_stats" CASCADE;
ALTER TABLE "public"."wieldable_item_stats" DROP CONSTRAINT "fk_wieldable_item_stats_1" CASCADE;
ALTER TABLE "public"."arrow_heads" DROP CONSTRAINT "fk_arrow_heads" CASCADE;
ALTER TABLE "public"."bows" DROP CONSTRAINT "fk_bows" CASCADE;
ALTER TABLE "public"."bows" DROP CONSTRAINT "fk_bows_1" CASCADE;
ALTER TABLE "public"."dart_tips" DROP CONSTRAINT "fk_dart_tips" CASCADE;
ALTER TABLE "public"."fires" DROP CONSTRAINT "fk_fires" CASCADE;
ALTER TABLE "public"."gems" DROP CONSTRAINT "fk_gems" CASCADE;
ALTER TABLE "public"."advanced_potions" DROP CONSTRAINT "fk_advanced_potions" CASCADE;
ALTER TABLE "public"."advanced_potions" DROP CONSTRAINT "fk_advanced_potions_1" CASCADE;
ALTER TABLE "public"."advanced_potions" DROP CONSTRAINT "fk_advanced_potions_2" CASCADE;
ALTER TABLE "public"."foods" DROP CONSTRAINT "fk_foods" CASCADE;
ALTER TABLE "public"."foods" DROP CONSTRAINT "fk_foods_1" CASCADE;
ALTER TABLE "public"."foods" DROP CONSTRAINT "fk_foods_2" CASCADE;
ALTER TABLE "public"."crafts" DROP CONSTRAINT "fk_crafts" CASCADE;
ALTER TABLE "public"."item_heals" DROP CONSTRAINT "fk_item_heals" CASCADE;
ALTER TABLE "public"."potions" DROP CONSTRAINT "fk_potions" CASCADE;
ALTER TABLE "public"."potions" DROP CONSTRAINT "fk_potions_1" CASCADE;
ALTER TABLE "public"."bars" DROP CONSTRAINT "fk_bars" CASCADE;
ALTER TABLE "public"."bars" DROP CONSTRAINT "fk_bars_1" CASCADE;
ALTER TABLE "public"."required_ores" DROP CONSTRAINT "fk_required_ores" CASCADE;
ALTER TABLE "public"."required_ores" DROP CONSTRAINT "fk_required_ores_1" CASCADE;
ALTER TABLE "public"."smithing_items" DROP CONSTRAINT "fk_smithing_items" CASCADE;
ALTER TABLE "public"."logs" DROP CONSTRAINT "fk_logs" CASCADE;
ALTER TABLE "public"."logs" DROP CONSTRAINT "fk_logs_1" CASCADE;
ALTER TABLE "public"."logs" DROP CONSTRAINT "fk_logs_2" CASCADE;
ALTER TABLE "public"."fishing_objects" DROP CONSTRAINT "fk_fishing_objects" CASCADE;
ALTER TABLE "public"."fishing_methods" DROP CONSTRAINT "fk_fishing_methods" CASCADE;
ALTER TABLE "public"."fishing_methods" DROP CONSTRAINT "fk_fishing_methods_1" CASCADE;
ALTER TABLE "public"."fishing_methods" DROP CONSTRAINT "fk_fishing_methods_2" CASCADE;
ALTER TABLE "public"."fishing_yields" DROP CONSTRAINT "fk_fishing_yields" CASCADE;
ALTER TABLE "public"."fishing_yields" DROP CONSTRAINT "fk_fishing_yields_1" CASCADE;
ALTER TABLE "public"."mining_objects" DROP CONSTRAINT "fk_mining_objects" CASCADE;
ALTER TABLE "public"."mining_objects" DROP CONSTRAINT "fk_mining_objects_1" CASCADE;
ALTER TABLE "public"."woodcutting_objects" DROP CONSTRAINT "fk_woodcutting_objects" CASCADE;
ALTER TABLE "public"."woodcutting_objects" DROP CONSTRAINT "fk_woodcutting_objects_1" CASCADE;
ALTER TABLE "public"."required_runes" DROP CONSTRAINT "fk_required_runes" CASCADE;
ALTER TABLE "public"."required_runes" DROP CONSTRAINT "fk_required_runes_1" CASCADE;
ALTER TABLE "public"."game_object_locations" DROP CONSTRAINT "fk_table_1" CASCADE;
ALTER TABLE "public"."item_locations" DROP CONSTRAINT "fk_item_locations" CASCADE;
ALTER TABLE "public"."npc_locations" DROP CONSTRAINT "fk_npc_locations" CASCADE;
ALTER TABLE "public"."player_settings" DROP CONSTRAINT "fk_player_settings" CASCADE;
ALTER TABLE "public"."player_appearances" DROP CONSTRAINT "fk_player_appearances" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_1" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_2" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_3" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_4" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_5" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_6" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_7" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_8" CASCADE;
ALTER TABLE "public"."player_worn_items" DROP CONSTRAINT "fk_player_worn_items_9" CASCADE;
ALTER TABLE "public"."player_inventory_items" DROP CONSTRAINT "fk_player_inventory_items" CASCADE;
ALTER TABLE "public"."player_inventory_items" DROP CONSTRAINT "fk_player_inventory_items_1" CASCADE;
ALTER TABLE "public"."shop_equalizers" DROP CONSTRAINT "fk_shop_equalizers" CASCADE;
ALTER TABLE "public"."shop_equalizers" DROP CONSTRAINT "fk_shop_equalizers_1" CASCADE;
ALTER TABLE "public"."shop_items" DROP CONSTRAINT "fk_shop_items" CASCADE;
ALTER TABLE "public"."shop_items" DROP CONSTRAINT "fk_shop_items_1" CASCADE;
ALTER TABLE "public"."player_banks" DROP CONSTRAINT "fk_player_banks" CASCADE;
ALTER TABLE "public"."player_banks" DROP CONSTRAINT "fk_player_banks_1" CASCADE;
ALTER TABLE "public"."player_logins" DROP CONSTRAINT "fk_player_logins" CASCADE;
ALTER TABLE "public"."player_chat_messages" DROP CONSTRAINT "fk_player_chat_messages" CASCADE;
ALTER TABLE "public"."player_private_messages" DROP CONSTRAINT "fk_player_private_messages" CASCADE;
ALTER TABLE "public"."player_private_messages" DROP CONSTRAINT "fk_player_private_messages_1" CASCADE;
ALTER TABLE "public"."player_commands" DROP CONSTRAINT "fk_player_commands" CASCADE;
ALTER TABLE "public"."player_kills" DROP CONSTRAINT "fk_player_kills" CASCADE;
ALTER TABLE "public"."player_kills" DROP CONSTRAINT "fk_player_kills_1" CASCADE;
ALTER TABLE "public"."player_kills" DROP CONSTRAINT "fk_player_kills_2" CASCADE;
ALTER TABLE "public"."player_deaths" DROP CONSTRAINT "fk_player_deaths" CASCADE;
ALTER TABLE "public"."player_deaths" DROP CONSTRAINT "fk_player_deaths_1" CASCADE;
ALTER TABLE "public"."player_deaths" DROP CONSTRAINT "fk_player_deaths_2" CASCADE;
ALTER TABLE "public"."player_stats" DROP CONSTRAINT "fk_player_stats" CASCADE;
ALTER TABLE "public"."player_stats" DROP CONSTRAINT "fk_player_stats_1" CASCADE;
ALTER TABLE "public"."player_friends" DROP CONSTRAINT "fk_player_friends" CASCADE;
ALTER TABLE "public"."player_friends" DROP CONSTRAINT "fk_player_friends_1" CASCADE;
ALTER TABLE "public"."player_ignores" DROP CONSTRAINT "fk_player_ignores" CASCADE;
ALTER TABLE "public"."player_ignores" DROP CONSTRAINT "fk_player_ignores_1" CASCADE;
ALTER TABLE "public"."player_logs" DROP CONSTRAINT "fk_player_logs" CASCADE;
ALTER TABLE "public"."npc_drops" DROP CONSTRAINT "fk_npc_drops" CASCADE;
ALTER TABLE "public"."npc_drops" DROP CONSTRAINT "fk_npc_drops_1" CASCADE;

DROP INDEX "public"."npcIDIndex" CASCADE;
DROP INDEX "public"."itemIndex" CASCADE;
DROP INDEX "public"."statIndex" CASCADE;
DROP INDEX "public"."wieldableItemsIndex" CASCADE;
DROP INDEX "public"."wieldable_item_stats_index" CASCADE;
DROP INDEX "public"."arrowHeadIndex" CASCADE;
DROP INDEX "public"."bowIndex" CASCADE;
DROP INDEX "public"."dartTipsIndex" CASCADE;
DROP INDEX "public"."doors_index" CASCADE;
DROP INDEX "public"."logIndex" CASCADE;
DROP INDEX "public"."game_objects_index" CASCADE;
DROP INDEX "public"."gemIndexz" CASCADE;
DROP INDEX "public"."advancedPotionsIndex" CASCADE;
DROP INDEX "public"."foods_index" CASCADE;
DROP INDEX "public"."crafts_index" CASCADE;
DROP INDEX "public"."item_heals_index" CASCADE;
DROP INDEX "public"."potions_index" CASCADE;
DROP INDEX "public"."bars_index" CASCADE;
DROP INDEX "public"."required_ores_index" CASCADE;
DROP INDEX "public"."smithing_items_index" CASCADE;
DROP INDEX "public"."logs_index" CASCADE;
DROP INDEX "public"."fishing_objects_index" CASCADE;
DROP INDEX "public"."fishing_methods_index" CASCADE;
DROP INDEX "public"."fishing_yields_index" CASCADE;
DROP INDEX "public"."mining_objects_index" CASCADE;
DROP INDEX "public"."woodcutting_objects_index" CASCADE;
DROP INDEX "public"."prayers_index" CASCADE;
DROP INDEX "public"."spells_index" CASCADE;
DROP INDEX "public"."required_runes_index" CASCADE;
DROP INDEX "public"."tiles_index" CASCADE;
DROP INDEX "public"."game_object_locations_index" CASCADE;
DROP INDEX "public"."item_locations_index" CASCADE;
DROP INDEX "public"."npc_locations_index" CASCADE;
DROP INDEX "public"."players_index" CASCADE;
DROP INDEX "public"."player_settings_index" CASCADE;
DROP INDEX "public"."" CASCADE;
DROP INDEX "public"."player_worn_items_index" CASCADE;
DROP INDEX "public"."player_inventory_items_index" CASCADE;
DROP INDEX "public"."shops_index" CASCADE;
DROP INDEX "public"."shop_equalizers_index" CASCADE;
DROP INDEX "public"."shop_items_index" CASCADE;
DROP INDEX "public"."player_banks_index" CASCADE;
DROP INDEX "public"."player_logins_index" CASCADE;
DROP INDEX "public"."player_chat_messages_index" CASCADE;
DROP INDEX "public"."player_private_messages_index" CASCADE;
DROP INDEX "public"."player_commands_index" CASCADE;
DROP INDEX "public"."player_kills_index" CASCADE;
DROP INDEX "public"."player_deaths_index" CASCADE;
DROP INDEX "public"."player_stats_index" CASCADE;
DROP INDEX "public"."player_friends_index" CASCADE;
DROP INDEX "public"."player_ignores_index" CASCADE;
DROP INDEX "public"."player_logs_index" CASCADE;
DROP INDEX "public"."npc_drops_index" CASCADE;

DROP TABLE "public"."npcs" CASCADE;
DROP TABLE "public"."items" CASCADE;
DROP TABLE "public"."stats" CASCADE;
DROP TABLE "public"."wieldable_items" CASCADE;
DROP TABLE "public"."wieldable_item_stats" CASCADE;
DROP TABLE "public"."arrow_heads" CASCADE;
DROP TABLE "public"."bows" CASCADE;
DROP TABLE "public"."dart_tips" CASCADE;
DROP TABLE "public"."doors" CASCADE;
DROP TABLE "public"."fires" CASCADE;
DROP TABLE "public"."game_objects" CASCADE;
DROP TABLE "public"."gems" CASCADE;
DROP TABLE "public"."advanced_potions" CASCADE;
DROP TABLE "public"."foods" CASCADE;
DROP TABLE "public"."crafts" CASCADE;
DROP TABLE "public"."item_heals" CASCADE;
DROP TABLE "public"."potions" CASCADE;
DROP TABLE "public"."bars" CASCADE;
DROP TABLE "public"."required_ores" CASCADE;
DROP TABLE "public"."smithing_items" CASCADE;
DROP TABLE "public"."logs" CASCADE;
DROP TABLE "public"."fishing_objects" CASCADE;
DROP TABLE "public"."fishing_methods" CASCADE;
DROP TABLE "public"."fishing_yields" CASCADE;
DROP TABLE "public"."mining_objects" CASCADE;
DROP TABLE "public"."woodcutting_objects" CASCADE;
DROP TABLE "public"."prayers" CASCADE;
DROP TABLE "public"."spells" CASCADE;
DROP TABLE "public"."required_runes" CASCADE;
DROP TABLE "public"."tiles" CASCADE;
DROP TABLE "public"."game_object_locations" CASCADE;
DROP TABLE "public"."item_locations" CASCADE;
DROP TABLE "public"."npc_locations" CASCADE;
DROP TABLE "public"."players" CASCADE;
DROP TABLE "public"."player_settings" CASCADE;
DROP TABLE "public"."player_appearances" CASCADE;
DROP TABLE "public"."player_worn_items" CASCADE;
DROP TABLE "public"."player_inventory_items" CASCADE;
DROP TABLE "public"."shops" CASCADE;
DROP TABLE "public"."shop_equalizers" CASCADE;
DROP TABLE "public"."shop_items" CASCADE;
DROP TABLE "public"."player_banks" CASCADE;
DROP TABLE "public"."player_logins" CASCADE;
DROP TABLE "public"."player_chat_messages" CASCADE;
DROP TABLE "public"."player_private_messages" CASCADE;
DROP TABLE "public"."player_commands" CASCADE;
DROP TABLE "public"."player_kills" CASCADE;
DROP TABLE "public"."player_deaths" CASCADE;
DROP TABLE "public"."player_stats" CASCADE;
DROP TABLE "public"."player_friends" CASCADE;
DROP TABLE "public"."player_ignores" CASCADE;
DROP TABLE "public"."player_logs" CASCADE;
DROP TABLE "public"."npc_drops" CASCADE;

CREATE TABLE "public"."npcs" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"command" text,
	"hair_color" integer,
	"top_color" integer,
	"skin_color" integer,
	"camera1" integer,
	"camera2" integer,
	"walk_model" integer,
	"combat_model" integer,
	"combat_sprite" integer,
	"hits" integer,
	"attack" integer,
	"defense" integer,
	"strength" integer,
	"respawn_time" integer,
	"attackable" boolean,
	"aggressive" boolean,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "npcIDIndex" ON "public"."npcs" USING btree ("id" ASC);

CREATE TABLE "public"."items" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"command" text,
	"sprite_id" integer,
	"stackable" boolean,
	"wieldable" boolean,
	"base_price" integer,
	"picture_mask" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "itemIndex" ON "public"."items" USING btree ("id" ASC);

CREATE TABLE "public"."stats" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "statIndex" ON "public"."stats" USING btree ("id" ASC);

CREATE TABLE "public"."wieldable_items" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint NOT NULL,
	"sprite" integer,
	"type" integer,
	"armor_bonus" integer,
	"weapon_aim_bonus" integer,
	"weapon_power_bonus" integer,
	"magic_bonus" integer,
	"prayer_bonus" integer,
	"range_bonus" integer,
	"wield_pos" integer,
	"female_only" boolean,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "wieldableItemsIndex" ON "public"."wieldable_items" USING btree ("id" ASC);

CREATE TABLE "public"."wieldable_item_stats" (
	"id" serial NOT NULL PRIMARY KEY,
	"wieldable_item_id" bigint,
	"stat_id" bigint,
	"required_level" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "wieldable_item_stats_index" ON "public"."wieldable_item_stats" USING btree ("id" ASC);

CREATE TABLE "public"."arrow_heads" (
	"id" serial NOT NULL PRIMARY KEY,
	"arrow_id" bigint,
	"required_level" integer,
	"experience" double precision,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "arrowHeadIndex" ON "public"."arrow_heads" USING btree ("id" ASC);

CREATE TABLE "public"."bows" (
	"id" serial NOT NULL PRIMARY KEY,
	"unstrung_bow_id" bigint,
	"bow_id" bigint,
	"experience" integer,
	"required_level" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "bowIndex" ON "public"."bows" USING btree ("id" ASC);

CREATE TABLE "public"."dart_tips" (
	"id" serial NOT NULL PRIMARY KEY,
	"dart_id" bigint,
	"required_level" integer,
	"experience" double precision,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "dartTipsIndex" ON "public"."dart_tips" USING btree ("id" ASC);

CREATE TABLE "public"."doors" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"command1" text,
	"command2" text,
	"door_type" integer,
	"unknown" integer,
	"model_var1" integer,
	"model_var2" integer,
	"model_var3" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "doors_index" ON "public"."doors" USING btree ("id" ASC);

CREATE TABLE "public"."fires" (
	"id" serial NOT NULL PRIMARY KEY,
	"log_id" bigint,
	"required_level" integer,
	"experience" integer,
	"length" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "logIndex" ON "public"."fires" USING btree ("id" ASC);

CREATE TABLE "public"."game_objects" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"command1" text,
	"command2" text,
	"object_type" integer,
	"width" integer,
	"height" integer,
	"ground_item_var" integer,
	"object_model" text,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "game_objects_index" ON "public"."game_objects" USING btree ("id" ASC);

CREATE TABLE "public"."gems" (
	"id" serial NOT NULL PRIMARY KEY,
	"gem_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "gemIndexz" ON "public"."gems" USING btree ("id" ASC);

CREATE TABLE "public"."advanced_potions" (
	"id" serial NOT NULL PRIMARY KEY,
	"unfinished_id" bigint,
	"ingredient_id" bigint,
	"potion_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "advancedPotionsIndex" ON "public"."advanced_potions" USING btree ("id" ASC);

CREATE TABLE "public"."foods" (
	"id" serial NOT NULL PRIMARY KEY,
	"ingredient_id" bigint,
	"cooked_id" bigint,
	"burned_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "foods_index" ON "public"."foods" USING btree ("id" ASC);

CREATE TABLE "public"."crafts" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "crafts_index" ON "public"."crafts" USING btree ("id" ASC);

CREATE TABLE "public"."item_heals" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint,
	"healing_amount" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "item_heals_index" ON "public"."item_heals" USING btree ("id" ASC);

CREATE TABLE "public"."potions" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint,
	"potion_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "potions_index" ON "public"."potions" USING btree ("id" ASC);

CREATE TABLE "public"."bars" (
	"id" serial NOT NULL PRIMARY KEY,
	"ore_id" bigint,
	"bar_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "bars_index" ON "public"."bars" USING btree ("id" ASC);

CREATE TABLE "public"."required_ores" (
	"id" serial NOT NULL PRIMARY KEY,
	"bar_id" bigint,
	"ore_id" bigint,
	"amount" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "required_ores_index" ON "public"."required_ores" USING btree ("id" ASC);

CREATE TABLE "public"."smithing_items" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint,
	"required_level" integer,
	"amount" integer,
	"bar_count" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "smithing_items_index" ON "public"."smithing_items" USING btree ("id" ASC);

CREATE TABLE "public"."logs" (
	"id" serial NOT NULL PRIMARY KEY,
	"log_id" bigint,
	"shortbow_id" bigint,
	"shortbow_level" integer,
	"shortbow_experience" integer,
	"longbow_id" bigint,
	"longbow_level" integer,
	"longbow_experience" integer,
	"shaft_level" integer,
	"shaft_amount" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "logs_index" ON "public"."logs" USING btree ("id" ASC);

CREATE TABLE "public"."fishing_objects" (
	"id" serial NOT NULL PRIMARY KEY,
	"game_object_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "fishing_objects_index" ON "public"."fishing_objects" USING btree ("id" ASC);

CREATE TABLE "public"."fishing_methods" (
	"id" serial NOT NULL PRIMARY KEY,
	"fishing_object_id" bigint,
	"net_id" bigint,
	"bait_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "fishing_methods_index" ON "public"."fishing_methods" USING btree ("id" ASC);

CREATE TABLE "public"."fishing_yields" (
	"id" serial NOT NULL PRIMARY KEY,
	"fishing_method_id" bigint,
	"fish_id" bigint,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "fishing_yields_index" ON "public"."fishing_yields" USING btree ("id" ASC);

CREATE TABLE "public"."mining_objects" (
	"id" serial NOT NULL PRIMARY KEY,
	"game_object_id" bigint,
	"ore_id" bigint,
	"respawn_time" integer,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "mining_objects_index" ON "public"."mining_objects" USING btree ("id" ASC);

CREATE TABLE "public"."woodcutting_objects" (
	"id" serial NOT NULL PRIMARY KEY,
	"game_object_id" bigint,
	"log_id" bigint,
	"success_rate" integer,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "woodcutting_objects_index" ON "public"."woodcutting_objects" USING btree ("id" ASC);

CREATE TABLE "public"."prayers" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"drain_rate" integer,
	"required_level" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "prayers_index" ON "public"."prayers" USING btree ("id" ASC);

CREATE TABLE "public"."spells" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"spell_type" integer,
	"required_level" integer,
	"experience" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "spells_index" ON "public"."spells" USING btree ("id" ASC);

CREATE TABLE "public"."required_runes" (
	"id" serial NOT NULL PRIMARY KEY,
	"spell_id" bigint,
	"rune_id" bigint,
	"count" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "required_runes_index" ON "public"."required_runes" USING btree ("id" ASC);

CREATE TABLE "public"."tiles" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"description" text,
	"color" integer,
	"object_type" integer,
	"unknown" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "tiles_index" ON "public"."tiles" USING btree ("id" ASC);

CREATE TABLE "public"."game_object_locations" (
	"id" serial NOT NULL PRIMARY KEY,
	"game_object_id" bigint,
	"x" integer,
	"y" integer,
	"direction" integer,
	"object_type" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "game_object_locations_index" ON "public"."game_object_locations" USING btree ("id" ASC);

CREATE TABLE "public"."item_locations" (
	"id" serial NOT NULL PRIMARY KEY,
	"item_id" bigint,
	"x" integer,
	"y" integer,
	"amount" integer,
	"respawn_time" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "item_locations_index" ON "public"."item_locations" USING btree ("id" ASC);

CREATE TABLE "public"."npc_locations" (
	"id" serial NOT NULL PRIMARY KEY,
	"npc_id" bigint,
	"start_x" integer,
	"start_y" integer,
	"min_x" integer,
	"min_y" integer,
	"max_x" integer,
	"max_y" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "npc_locations_index" ON "public"."npc_locations" USING btree ("id" ASC);

CREATE TABLE "public"."players" (
	"id" serial NOT NULL PRIMARY KEY,
	"username" text,
	"password" text,
	"rank" integer,
	"muted" boolean,
	"logged_in" boolean,
	"suspicious" boolean,
	"combat_style" bigint,
	"skulled" boolean,
	"fatigue" integer,
	"invisible" boolean,
	"gender" integer,
	"x" integer,
	"y" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "players_index" ON "public"."players" USING btree ("id" ASC);

CREATE TABLE "public"."player_settings" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"key" integer,
	"value" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_settings_index" ON "public"."player_settings" USING btree ("id" ASC);

CREATE TABLE "public"."player_appearances" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"hair_color" integer,
	"top_color" integer,
	"bottom_color" integer,
	"skin_color" integer,
	"head_type" integer,
	"body_type" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY ON "public"."player_appearances" USING btree ("id" ASC);

CREATE TABLE "public"."player_worn_items" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"head_item_id" bigint,
	"top_item_id" bigint,
	"bottom_item_id" bigint,
	"weapon_item_id" bigint,
	"shield_item_id" bigint,
	"cape_item_id" bigint,
	"gloves_item_id" bigint,
	"boots_item_id" bigint,
	"amulet_item_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_worn_items_index" ON "public"."player_worn_items" USING btree ("id" ASC);

CREATE TABLE "public"."player_inventory_items" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"item_id" bigint,
	"slot_id" integer,
	"amount" integer,
	"is_wielded" boolean,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_inventory_items_index" ON "public"."player_inventory_items" USING btree ("id" ASC);

CREATE TABLE "public"."shops" (
	"id" serial NOT NULL PRIMARY KEY,
	"name" text,
	"general" boolean,
	"sell_modifier" integer,
	"buy_modifier" integer,
	"min_x" integer,
	"min_y" integer,
	"max_x" integer,
	"max_y" integer,
	"greeting" text,
	"respawn_rate" integer,
	"options" text,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "shops_index" ON "public"."shops" USING btree ("id" ASC);

CREATE TABLE "public"."shop_equalizers" (
	"id" serial NOT NULL PRIMARY KEY,
	"shop_id" bigint,
	"item_id" bigint,
	"item_amount" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "shop_equalizers_index" ON "public"."shop_equalizers" USING btree ("id" ASC);

CREATE TABLE "public"."shop_items" (
	"id" serial NOT NULL PRIMARY KEY,
	"shop_id" bigint,
	"slot_id" integer,
	"item_id" bigint,
	"amount" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "shop_items_index" ON "public"."shop_items" USING btree ("id" ASC);

CREATE TABLE "public"."player_banks" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"item_id" bigint,
	"slot_id" integer,
	"quantity" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_banks_index" ON "public"."player_banks" USING btree ("id" ASC);

CREATE TABLE "public"."player_logins" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"ip" text,
	"login_status" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_logins_index" ON "public"."player_logins" USING btree ("id" ASC);

CREATE TABLE "public"."player_chat_messages" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"x" integer,
	"y" integer,
	"message" text,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_chat_messages_index" ON "public"."player_chat_messages" USING btree ("id" ASC);

CREATE TABLE "public"."player_private_messages" (
	"id" serial NOT NULL PRIMARY KEY,
	"sender_id" bigint,
	"recipient_id" bigint,
	"message" text,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_private_messages_index" ON "public"."player_private_messages" USING btree ("id" ASC);

CREATE TABLE "public"."player_commands" (
	"id" serial NOT NULL PRIMARY KEY,
	"sender_id" bigint,
	"x" integer,
	"y" integer,
	"command" text,
	"success" boolean,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_commands_index" ON "public"."player_commands" USING btree ("id" ASC);

CREATE TABLE "public"."player_kills" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"x" integer,
	"y" integer,
	"was_npc" boolean,
	"was_player" boolean,
	"was_duel" boolean,
	"npc_id" bigint,
	"victim_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_kills_index" ON "public"."player_kills" USING btree ("id" ASC);

CREATE TABLE "public"."player_deaths" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"x" integer,
	"y" integer,
	"was_npc" boolean,
	"was_player" boolean,
	"was_duel" boolean,
	"npc_id" bigint,
	"killer_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_deaths_index" ON "public"."player_deaths" USING btree ("id" ASC);

CREATE TABLE "public"."player_stats" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"stat_id" bigint,
	"experience" integer,
	"current_level" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_stats_index" ON "public"."player_stats" USING btree ("id" ASC);

CREATE TABLE "public"."player_friends" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"friend_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_friends_index" ON "public"."player_friends" USING btree ("id" ASC);

CREATE TABLE "public"."player_ignores" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"ignored_id" bigint,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_ignores_index" ON "public"."player_ignores" USING btree ("id" ASC);

CREATE TABLE "public"."player_logs" (
	"id" serial NOT NULL PRIMARY KEY,
	"player_id" bigint,
	"x" integer,
	"y" integer,
	"action" integer,
	"metadata" jsonb,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "player_logs_index" ON "public"."player_logs" USING btree ("id" ASC);

CREATE TABLE "public"."npc_drops" (
	"id" serial NOT NULL PRIMARY KEY,
	"npc_id" bigint,
	"item_id" bigint,
	"amount" integer,
	"weight" integer,
	"created_at" timestamp with time zone,
	"updated_at" timestamp with time zone
) WITHOUT OIDS;

CREATE UNIQUE INDEX CONCURRENTLY "npc_drops_index" ON "public"."npc_drops" USING btree ("id" ASC);


ALTER TABLE "public"."wieldable_items" ADD CONSTRAINT "fk_wieldable_items" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."wieldable_item_stats" ADD CONSTRAINT "fk_wieldable_item_stats" FOREIGN KEY ("wieldable_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."wieldable_item_stats" ADD CONSTRAINT "fk_wieldable_item_stats_1" FOREIGN KEY ("stat_id") REFERENCES "public"."stats" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."arrow_heads" ADD CONSTRAINT "fk_arrow_heads" FOREIGN KEY ("arrow_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."bows" ADD CONSTRAINT "fk_bows" FOREIGN KEY ("unstrung_bow_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."bows" ADD CONSTRAINT "fk_bows_1" FOREIGN KEY ("bow_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."dart_tips" ADD CONSTRAINT "fk_dart_tips" FOREIGN KEY ("dart_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fires" ADD CONSTRAINT "fk_fires" FOREIGN KEY ("log_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."gems" ADD CONSTRAINT "fk_gems" FOREIGN KEY ("gem_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."advanced_potions" ADD CONSTRAINT "fk_advanced_potions" FOREIGN KEY ("unfinished_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."advanced_potions" ADD CONSTRAINT "fk_advanced_potions_1" FOREIGN KEY ("ingredient_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."advanced_potions" ADD CONSTRAINT "fk_advanced_potions_2" FOREIGN KEY ("potion_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."foods" ADD CONSTRAINT "fk_foods" FOREIGN KEY ("ingredient_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."foods" ADD CONSTRAINT "fk_foods_1" FOREIGN KEY ("cooked_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."foods" ADD CONSTRAINT "fk_foods_2" FOREIGN KEY ("burned_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."crafts" ADD CONSTRAINT "fk_crafts" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."item_heals" ADD CONSTRAINT "fk_item_heals" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."potions" ADD CONSTRAINT "fk_potions" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."potions" ADD CONSTRAINT "fk_potions_1" FOREIGN KEY ("potion_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."bars" ADD CONSTRAINT "fk_bars" FOREIGN KEY ("ore_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."bars" ADD CONSTRAINT "fk_bars_1" FOREIGN KEY ("bar_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."required_ores" ADD CONSTRAINT "fk_required_ores" FOREIGN KEY ("ore_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."required_ores" ADD CONSTRAINT "fk_required_ores_1" FOREIGN KEY ("bar_id") REFERENCES "public"."bars" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."smithing_items" ADD CONSTRAINT "fk_smithing_items" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."logs" ADD CONSTRAINT "fk_logs" FOREIGN KEY ("log_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."logs" ADD CONSTRAINT "fk_logs_1" FOREIGN KEY ("shortbow_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."logs" ADD CONSTRAINT "fk_logs_2" FOREIGN KEY ("longbow_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_objects" ADD CONSTRAINT "fk_fishing_objects" FOREIGN KEY ("game_object_id") REFERENCES "public"."game_objects" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_methods" ADD CONSTRAINT "fk_fishing_methods" FOREIGN KEY ("fishing_object_id") REFERENCES "public"."fishing_objects" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_methods" ADD CONSTRAINT "fk_fishing_methods_1" FOREIGN KEY ("net_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_methods" ADD CONSTRAINT "fk_fishing_methods_2" FOREIGN KEY ("bait_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_yields" ADD CONSTRAINT "fk_fishing_yields" FOREIGN KEY ("fishing_method_id") REFERENCES "public"."fishing_methods" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."fishing_yields" ADD CONSTRAINT "fk_fishing_yields_1" FOREIGN KEY ("fish_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."mining_objects" ADD CONSTRAINT "fk_mining_objects" FOREIGN KEY ("game_object_id") REFERENCES "public"."game_objects" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."mining_objects" ADD CONSTRAINT "fk_mining_objects_1" FOREIGN KEY ("ore_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."woodcutting_objects" ADD CONSTRAINT "fk_woodcutting_objects" FOREIGN KEY ("game_object_id") REFERENCES "public"."game_objects" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."woodcutting_objects" ADD CONSTRAINT "fk_woodcutting_objects_1" FOREIGN KEY ("log_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."required_runes" ADD CONSTRAINT "fk_required_runes" FOREIGN KEY ("spell_id") REFERENCES "public"."spells" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."required_runes" ADD CONSTRAINT "fk_required_runes_1" FOREIGN KEY ("rune_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."game_object_locations" ADD CONSTRAINT "fk_table_1" FOREIGN KEY ("game_object_id") REFERENCES "public"."game_objects" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."item_locations" ADD CONSTRAINT "fk_item_locations" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."npc_locations" ADD CONSTRAINT "fk_npc_locations" FOREIGN KEY ("npc_id") REFERENCES "public"."npcs" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_settings" ADD CONSTRAINT "fk_player_settings" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_appearances" ADD CONSTRAINT "fk_player_appearances" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_1" FOREIGN KEY ("head_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_2" FOREIGN KEY ("top_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_3" FOREIGN KEY ("bottom_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_4" FOREIGN KEY ("weapon_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_5" FOREIGN KEY ("shield_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_6" FOREIGN KEY ("cape_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_7" FOREIGN KEY ("gloves_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_8" FOREIGN KEY ("boots_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_worn_items" ADD CONSTRAINT "fk_player_worn_items_9" FOREIGN KEY ("amulet_item_id") REFERENCES "public"."wieldable_items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_inventory_items" ADD CONSTRAINT "fk_player_inventory_items" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_inventory_items" ADD CONSTRAINT "fk_player_inventory_items_1" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."shop_equalizers" ADD CONSTRAINT "fk_shop_equalizers" FOREIGN KEY ("shop_id") REFERENCES "public"."shops" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."shop_equalizers" ADD CONSTRAINT "fk_shop_equalizers_1" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."shop_items" ADD CONSTRAINT "fk_shop_items" FOREIGN KEY ("shop_id") REFERENCES "public"."shops" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."shop_items" ADD CONSTRAINT "fk_shop_items_1" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_banks" ADD CONSTRAINT "fk_player_banks" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_banks" ADD CONSTRAINT "fk_player_banks_1" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_logins" ADD CONSTRAINT "fk_player_logins" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_chat_messages" ADD CONSTRAINT "fk_player_chat_messages" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_private_messages" ADD CONSTRAINT "fk_player_private_messages" FOREIGN KEY ("sender_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_private_messages" ADD CONSTRAINT "fk_player_private_messages_1" FOREIGN KEY ("recipient_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_commands" ADD CONSTRAINT "fk_player_commands" FOREIGN KEY ("sender_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_kills" ADD CONSTRAINT "fk_player_kills" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_kills" ADD CONSTRAINT "fk_player_kills_1" FOREIGN KEY ("npc_id") REFERENCES "public"."npcs" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_kills" ADD CONSTRAINT "fk_player_kills_2" FOREIGN KEY ("victim_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_deaths" ADD CONSTRAINT "fk_player_deaths" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_deaths" ADD CONSTRAINT "fk_player_deaths_1" FOREIGN KEY ("npc_id") REFERENCES "public"."npcs" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_deaths" ADD CONSTRAINT "fk_player_deaths_2" FOREIGN KEY ("killer_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_stats" ADD CONSTRAINT "fk_player_stats" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_stats" ADD CONSTRAINT "fk_player_stats_1" FOREIGN KEY ("stat_id") REFERENCES "public"."stats" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_friends" ADD CONSTRAINT "fk_player_friends" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_friends" ADD CONSTRAINT "fk_player_friends_1" FOREIGN KEY ("friend_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_ignores" ADD CONSTRAINT "fk_player_ignores" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_ignores" ADD CONSTRAINT "fk_player_ignores_1" FOREIGN KEY ("ignored_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."player_logs" ADD CONSTRAINT "fk_player_logs" FOREIGN KEY ("player_id") REFERENCES "public"."players" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."npc_drops" ADD CONSTRAINT "fk_npc_drops" FOREIGN KEY ("npc_id") REFERENCES "public"."npcs" ("id") ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE "public"."npc_drops" ADD CONSTRAINT "fk_npc_drops_1" FOREIGN KEY ("item_id") REFERENCES "public"."items" ("id") ON DELETE CASCADE ON UPDATE CASCADE;

