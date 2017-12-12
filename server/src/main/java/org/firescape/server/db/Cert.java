package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@Table("certs")
@DbName("firescape")
@BelongsToParents({
  @BelongsTo(foreignKeyName = "cert_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "item_id", parent = Item.class),
})
public class Cert extends Model {}
