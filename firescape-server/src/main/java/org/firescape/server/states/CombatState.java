package org.firescape.server.states;

public enum CombatState {
  ERROR, // Can be attacked
  RUNNING, // Can't be attacked
  WAITING, // Can be attacked
  WON, // Can be attacked
  LOST // Can be attacked
}