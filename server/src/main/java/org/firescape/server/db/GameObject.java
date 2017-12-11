package org.firescape.server.db;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

@Table("game_objects")
@DbName("firescape")
public class GameObject extends Model {}
