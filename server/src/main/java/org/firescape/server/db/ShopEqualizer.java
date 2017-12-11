package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
  @BelongsTo(foreignKeyName = "item_id", parent = Item.class),
  @BelongsTo(foreignKeyName = "shop_id", parent = Shop.class),
})
@Table("shop_equalizers")
@DbName("firescape")
public class ShopEqualizer extends Model {}
