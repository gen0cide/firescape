package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "spell_id", parent = Spell.class),
  @BelongsTo(foreignKeyName = "rune_id", parent = Item.class),
})
@Table("required_runes")
@DbName("firescape")
public class RequiredRune extends Model {}
