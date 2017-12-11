package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "unstrung_bow_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "bow_id", parent = Item.class),
})
@Table("bows")
@DbName("firescape")
public class Bow extends Model {}
