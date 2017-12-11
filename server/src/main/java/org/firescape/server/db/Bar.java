package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "ore_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "bar_id", parent = Item.class),
})
@Table("bars")
@DbName("firescape")
public class Bar extends Model {}
