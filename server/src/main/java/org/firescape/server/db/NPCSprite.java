package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@Table("npc_sprites")
@DbName("firescape")
@BelongsToParents({
  @BelongsTo(foreignKeyName = "npc_id", parent = NPC.class),
})
public class NPCSprite extends Model {}
