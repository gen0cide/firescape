package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "game_object_id", parent = GameObject.class),
})
@Table("fishing_objects")
@DbName("firescape")
public class FishingObject extends Model {}
