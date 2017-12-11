package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "bar_id", parent = Bar.class), @BelongsTo(foreignKeyName = "ore_id", parent = Item.class),
})
@Table("required_ores")
@DbName("firescape")
public class RequiredOre extends Model {}
