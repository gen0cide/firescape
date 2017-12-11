package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "ingredient_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "cooked_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "burned_id", parent = Item.class),
})
@Table("foods")
@DbName("firescape")
public class Food extends Model {}
