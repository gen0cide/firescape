package org.firescape.server.entityhandling.defs.extras;

import org.firescape.server.entityhandling.defs.EntityDef;

public class CertDef {

  /**
   * The name of the item this cert is for
   */
  public String name;
  /**
   * The ID of the certificate
   */
  public int certID;
  /**
   * The ID of the assosiated item
   */
  public int itemID;

  public String getName() {
    return name;
  }

  public int getCertID() {
    return certID;
  }

  public int getItemID() {
    return itemID;
  }
}