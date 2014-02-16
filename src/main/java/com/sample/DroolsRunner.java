package com.sample;


import java.util.ArrayList;
import java.util.List;

import com.sample.fact.ProcessState;
import com.sample.fact.ProcessState.State;

public class DroolsRunner
{

  public static final void main(final String[] args)
    throws Exception
  {
    final String droolsFile = args[0];
    System.out.println("Running Drools rules from: " + droolsFile);
    
    final List<ProcessState> facts = new ArrayList<>();
    facts.add(new ProcessState("New Process", State.NOT_STARTED));

    final DroolsCallable<ProcessState> drools = new DroolsCallable<>();
    drools.setDroolsFile(droolsFile);
    drools.setFacts(facts);
    drools.call();
  }

}
