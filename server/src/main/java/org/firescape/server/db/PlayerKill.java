package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "player_id", parent = Player.class),
  @BelongsTo(foreignKeyName = "victim_id", parent = Player.class),
  @BelongsTo(foreignKeyName = "npc_id", parent = NPC.class)
})
@Table("player_kills")
@DbName("firescape")
public class PlayerKill extends Model {}
