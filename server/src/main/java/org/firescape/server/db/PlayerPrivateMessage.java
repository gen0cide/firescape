package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "sender_id", parent = Player.class),
  @BelongsTo(foreignKeyName = "recipient_id", parent = Player.class)
})
@Table("player_private_messages")
@DbName("firescape")
public class PlayerPrivateMessage extends Model {}
