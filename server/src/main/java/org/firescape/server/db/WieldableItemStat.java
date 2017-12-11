package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "wieldable_item_id", parent = WieldableItem.class),
  @BelongsTo(foreignKeyName = "stat_id", parent = Stat.class),
})
@Table("wieldable_item_stats")
@DbName("firescape")
public class WieldableItemStat extends Model {}
