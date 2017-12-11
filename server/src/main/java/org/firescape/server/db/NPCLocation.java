package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsTo(parent = NPC.class, foreignKeyName = "npc_id")
@Table("npc_locations")
@DbName("firescape")
public class NPCLocation extends Model {}
