/**
 * © Copyright 2014-16 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */

package us.fatehi.drools_intro;


import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
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

public class DroolsCallable
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
      throw new RuntimeException(kb.getResults().toString());
    }

    final KieContainer kContainer = ks
      .newKieContainer(kr.getDefaultReleaseId());

    final KieSession kSession = kContainer.newKieSession();
    // kSession.addEventListener(new
    // DebugAgendaEventListener(System.out));
    kSession.addEventListener(new TrackingAgendaEventListener());

    for (final Object fact: facts)
    {
      kSession.insert(fact);
    }

    System.out.println(">> Input");
    System.out.println("  - facts: " + facts);
    final int i = kSession.fireAllRules();
    System.out.println(">> Output");
    System.out.println("  - facts: " + facts);

    kSession.destroy();

    return i;
  }

  private Resource getRule()
  {
    final InputStream stream = this.getClass().getResourceAsStream(droolsFile);
    if (stream == null)
    {
      throw new IllegalAccessError("Cannot load " + droolsFile);
    }
    final Resource resource = new InputStreamResource(stream);
    return resource;
  }

}
