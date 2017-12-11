package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "log_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "shortbow_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "longbow_id", parent = Item.class),
})
@Table("logs")
@DbName("firescape")
public class Log extends Model {}
