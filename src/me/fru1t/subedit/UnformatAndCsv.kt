package me.fru1t.subedit

import me.fru1t.subedit.utils.Utils
import java.io.File


/** Monolith for the UnformatAndCsv tool. */
object UnformatAndCsv {
  // 1: Layer, 2: Start, 3: End, 4: Style, 5: Name, 6: MarginL, 7: MarginR, 8: MarginV, 9: Effect, 10: Text
  private val DIALOGUE_REGEX = Regex("^Dialogue: ([^,]+),([^,]+),([^,]+),([^,]+),([^,]*),(\\d+),(\\d+),(\\d+),([^,]*),(.*)$")
  private val FORMATTING_STRING = Regex("\\{[^{]+}")
  private const val NEWLINE = "\\N"
  private const val CSV_EXTENSION = ".csv"
  private const val DELIMITER = "\t"

  /**
   * Console application that creates a CSV from a given .ass file in the following output:
   * `starttime,style,line1,line2`
   */
  fun run() {
    println("RUNNING: Unformat and CSV")

    val file = Utils.askForFile() ?: return
    val fileContents = ArrayList(file.readLines())

    var dialogueLine = 0
    val output = ArrayList<String>()
    output.add("Start time${DELIMITER}Style${DELIMITER}Line1${DELIMITER}Line2")
    for (lineNumber in fileContents.indices) {
      val line = fileContents[lineNumber]
      val matcher = DIALOGUE_REGEX.matchEntire(line) ?: continue
      dialogueLine++

      val text = matcher.get(10).replace(FORMATTING_STRING, "")
      val firstNewlinePosition = text.indexOf(NEWLINE)
      val text1: String
      val text2: String
      if (firstNewlinePosition == -1) {
        text1 = text
        text2 = ""
      } else {
        text1 = text.substring(0 until firstNewlinePosition)
        text2 = text.substring(firstNewlinePosition + 2)
      }

      // 1: Layer, 2: Start, 3: End, 4: Style, 5: Name, 6: MarginL, 7: MarginR, 8: MarginV, 9: Effect, 10: Text
      output.add("${matcher.get(2)}$DELIMITER" +
          "${matcher.get(4)}$DELIMITER" +
          "$text1$DELIMITER" +
          "$text2"
      )
    }

    val csv = File(file.path + CSV_EXTENSION)
    if (csv.exists()) {
      println("${csv.path} already exists, overwrite?")
      if (!Utils.askYes()) {
        println("Oki ignoring, bai bai")
        return
      }
    }

    Utils.writeFile(csv, output)

    println("Oki done. enjoy")
  }

  fun MatchResult.get(group: Int): String = groupValues[group].replace("\"", "\"\"")
}