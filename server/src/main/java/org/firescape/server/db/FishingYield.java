package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "fishing_method_id", parent = FishingMethod.class),
  @BelongsTo(foreignKeyName = "fish_id", parent = Item.class),
})
@Table("fishing_yields")
@DbName("firescape")
public class FishingYield extends Model {}
