/**
 * © Copyright 2014 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */
package com.sample.fact;


import java.io.Serializable;

public class ProcessState
  implements Serializable
{

  public enum State
  {
    NOT_STARTED,
    RUNNING,
    FINISHED,
    ABORTED
  }

  private static final long serialVersionUID = -5958805852235635082L;

  private String name;
  private State state;
  private String notes;

  public ProcessState(final String name, final State state)
  {
    this.name = name;
    this.state = state;
    notes = "";
  }

  public String getName()
  {
    return name;
  }

  public String getNotes()
  {
    return notes;
  }

  public State getState()
  {
    return state;
  }

  public void setName(final String name)
  {
    this.name = name;
  }

  public void setNotes(final String notes)
  {
    this.notes = notes;
  }

  public void setState(final State state)
  {
    this.state = state;
  }

  @Override
  public String toString()
  {
    return "ProcessState(name=" + name + ", state=" + state + ", notes="
           + notes + ")";
  }

}
