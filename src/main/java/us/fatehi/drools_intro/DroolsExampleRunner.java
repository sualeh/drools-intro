/**
 * � Copyright 2014-16 Sualeh Fatehi
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/deed.en_US.
 */
package us.fatehi.drools_intro;


import java.util.Scanner;

import us.fatehi.drools_intro.fact.ProcessState;
import us.fatehi.drools_intro.fact.ProcessState.State;

public class DroolsExampleRunner
{

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

        final ProcessState fact = new ProcessState("New Process", State.NOT_STARTED);

        final DroolsCallable drools = new DroolsCallable(droolsFile);
        drools.addFact(fact);
        drools.call();
      }
    }
  }

}
