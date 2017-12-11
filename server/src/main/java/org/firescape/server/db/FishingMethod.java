package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "fishing_object_id", parent = FishingObject.class),
  @BelongsTo(foreignKeyName = "net_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "bait_id", parent = Item.class),
})
@Table("fishing_methods")
@DbName("firescape")
public class FishingMethod extends Model {}
