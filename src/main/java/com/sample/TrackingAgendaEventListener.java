/**
 * © Copyright 2014-15 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */
package com.sample;


import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;

public class TrackingAgendaEventListener
  extends DefaultAgendaEventListener
{

  @Override
  public void afterMatchFired(final AfterMatchFiredEvent event)
  {
    final Rule rule = event.getMatch().getRule();
    System.out.println("* Fired: " + rule);
    System.out.println("  - facts: " + event.getMatch().getObjects());
  }

  @Override
  public void matchCreated(final MatchCreatedEvent event)
  {
    final Rule rule = event.getMatch().getRule();
    System.out.println("* Matched: " + rule);
    System.out.println("  - facts: " + event.getMatch().getObjects());
  }

  @Override
  public void matchCancelled(final MatchCancelledEvent event)
  {
    final Rule rule = event.getMatch().getRule();
    System.out.println("* Cancelled: " + rule);
  }
  
}
