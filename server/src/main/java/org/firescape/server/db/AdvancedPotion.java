package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "unfinished_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "ingredient_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "potion_id", parent = Item.class),
})
@Table("advanced_potions")
@DbName("firescape")
public class AdvancedPotion extends Model {}
