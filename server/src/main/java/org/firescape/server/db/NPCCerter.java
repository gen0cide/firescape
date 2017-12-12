package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@Table("npc_certers")
@DbName("firescape")
@BelongsToParents({
  @BelongsTo(foreignKeyName = "cert_id", parent = Cert.class),
  @BelongsTo(foreignKeyName = "npc_id", parent = NPC.class),
})
public class NPCCerter extends Model {}
