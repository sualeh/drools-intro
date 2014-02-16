/**
 * © Copyright 2014 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */
package com.sample;


import java.util.List;
import java.util.concurrent.Callable;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DroolsCallable<F>
  implements Callable<Integer>
{

  private String droolsFile;
  private List<F> facts;

  @Override
  public Integer call()
    throws Exception
  {
    final KnowledgeBase kbase = readKnowledgeBase(droolsFile);
    final StatefulKnowledgeSession ksession = kbase
      .newStatefulKnowledgeSession();

    final KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
      .newConsoleLogger(ksession);

    for (final F fact: facts)
    {
      ksession.insert(fact);
    }

    System.out.println(">> Input facts: " + facts);

    final int i = ksession.fireAllRules();

    System.out.println(">> Output facts: " + facts);

    logger.close();

    return i;
  }

  private KnowledgeBase readKnowledgeBase(final String droolsFile)
    throws Exception
  {
    final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
      .newKnowledgeBuilder();
    kbuilder.add(ResourceFactory.newClassPathResource(droolsFile),
                 ResourceType.DRL);
    final KnowledgeBuilderErrors errors = kbuilder.getErrors();
    if (errors.size() > 0)
    {
      for (final KnowledgeBuilderError error: errors)
      {
        System.err.println(error);
      }
      throw new IllegalArgumentException("Could not parse knowledge.");
    }
    final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
    return kbase;
  }

  public void setDroolsFile(final String droolsFile)
  {
    this.droolsFile = droolsFile;
  }

  public void setFacts(final List<F> facts)
  {
    this.facts = facts;
  }

}
