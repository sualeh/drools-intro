/**
 * � Copyright 2014 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */
package com.sample;


import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Callable;

import org.drools.core.io.impl.InputStreamResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.sample.fact.ProcessState;
import com.sample.fact.ProcessState.State;

public class DroolsExampleRunner
{

  public static class DroolsCallable
    implements Callable<Integer>
  {

    private final String droolsFile;
    private final Set<Object> facts;

    public DroolsCallable(final String droolsFile)
    {
      this.droolsFile = droolsFile;
      facts = new HashSet<>();
    }

    public void addFact(final Object fact)
    {
      facts.add(fact);
    }

    public void addFacts(final Collection<?> facts)
    {
      this.facts.addAll(facts);
    }

    /**
     * See <a href=
     * "https://github.com/droolsjbpm/drools/blob/master/drools-examples-api/kiefilesystem-example/src/main/java/org/drools/example/api/kiefilesystem/KieFileSystemExample.java"
     * > Drools example code </a>
     */
    @Override
    public Integer call()
      throws Exception
    {

      final KieServices ks = KieServices.Factory.get();
      final KieRepository kr = ks.getRepository();
      
      final KieFileSystem kfs = ks.newKieFileSystem();
      kfs.write("src/main/resources" + droolsFile, getRule());

      final KieBuilder kb = ks.newKieBuilder(kfs);
      kb.buildAll(); // KieModule is automatically deployed to
                     // KieRepository if successfully built
      if (kb.getResults().hasMessages(Level.ERROR))
      {
        throw new RuntimeException("Build Errors:\n"
                                   + kb.getResults().toString());
      }

      final KieContainer kContainer = ks.newKieContainer(kr
        .getDefaultReleaseId());

      final KieSession kSession = kContainer.newKieSession();

      for (final Object fact: facts)
      {
        kSession.insert(fact);
      }

      System.out.println(">> Input facts: " + facts);
      final int i = kSession.fireAllRules();
      System.out.println(">> Output facts: " + facts);

      kSession.destroy();

      return i;
    }

    private Resource getRule()
    {
      final InputStream stream = this.getClass()
        .getResourceAsStream(droolsFile);
      if (stream == null)
      {
        throw new IllegalAccessError("Cannot load " + droolsFile);
      }
      final Resource resource = new InputStreamResource(stream);
      return resource;
    }

  }

  public static final void main(final String[] args)
    throws Exception
  {
    try (final Scanner scanner = new Scanner(System.in);)
    {
      while (true)
      {
        System.out.print("\n\nRun Drools example #: ");
        final int exampleNmber = scanner.nextInt();
        if (exampleNmber <= 0)
        {
          return;
        }
        final String droolsFile = String.format("/%d_State.drl", exampleNmber);
        System.out.println("Running Drools rules from: " + droolsFile);

        final DroolsCallable drools = new DroolsCallable(droolsFile);
        drools.addFact(new ProcessState("New Process", State.NOT_STARTED));
        drools.call();
      }
    }
  }

}