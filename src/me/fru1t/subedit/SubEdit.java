package me.fru1t.subedit;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main method entrypoint for Subtitle Edit Tools.
 */
public class SubEdit {
  private static final String PROGRAMS_OUTPUT = "a: FirstLineSmaller; b: UnformatAndCsv; c: SecondBorderAndShadow";

  public static void main(String[] args) throws IOException {
    String programToRun;
    if (args.length == 1) {
      programToRun = args[0];
    } else {
      System.out.println("Yo, what do you wanna run? BTW, you can just pass this as a CLI arg too");
      System.out.println(PROGRAMS_OUTPUT);

      // Note: "in" is never closed as we don't want to close the underlying System.in stream
      Scanner in = new Scanner(System.in);
      programToRun = in.nextLine();
    }

    switch (programToRun) {
      case "a" -> FirstLineSmaller.INSTANCE.run();
      case "b" -> UnformatAndCsv.INSTANCE.run();
      case "c" -> SecondBorderAndShadow.INSTANCE.run();
      default -> {
        System.out.println("Sorry, I couldn't understand <" + programToRun + ">. Please choose from the following:");
        System.out.println(PROGRAMS_OUTPUT);
      }
    }
  }
}
