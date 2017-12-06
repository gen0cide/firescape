/**
 * rsc 10-02-2016
 */

package org.firescape.client.opcode;

import java.math.BigInteger;

public class Keys {

  public static BigInteger exponent204 = new BigInteger(
    "1370158896620336158431733257575682136836100155721926632321599369132092701295540721504104229217666225601026879393318399391095704223500673696914052239029335");
  //private static BigInteger modulus204 = new BigInteger("7162900525229798032761816791230527296329313291232324290237849263501208207972894053929065636522363163621000728841182238772712427862772219676577293600221789");
  public static BigInteger modulus204 = new BigInteger(
    "1549611057746979844352781944553705273443228154042066840514290174539588436243191882510185738846985723357723362764835928526260868977814405651690121789896823");

  public static BigInteger exponent234 = new BigInteger("10001", 16);
  public static BigInteger modulus234 = new BigInteger(
    "ca950472ae9765185bf290ff54a823b1d29b46dc3cf676203bb871efa278d9c49e16defc53ff479305123454505082f4700b0da381047f51b872f9bbeea653f21fd248a10ff5239b30234add35913cb6068d316edd418611334ae047fcd9acb7b0c13b30393a26204dc85183e0a95555c01bee800440e974bb9b441f464f4057",
    16
  );

  public static BigInteger getExponent(int version) {
    switch (version) {
      case 234:
        return exponent234;
      default:
        return exponent204;
    }
  }

  public static BigInteger getModulus(int version) {
    switch (version) {
      case 234:
        return modulus234;
      default:
        return modulus204;
    }
  }
}
