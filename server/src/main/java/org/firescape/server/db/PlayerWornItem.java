package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "player_id", parent = Player.class),
  @BelongsTo(foreignKeyName = "head_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "top_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "bottom_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "weapon_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "shield_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "cape_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "gloves_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "boots_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "amulet_item_id", parent = WieldableItem.class),
})
@Table("player_worn_items")
@DbName("firescape")
public class PlayerWornItem extends Model {}
