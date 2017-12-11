package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "item_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "potion_id", parent = Item.class),
})
@Table("potions")
@DbName("firescape")
public class Potion extends Model {}
