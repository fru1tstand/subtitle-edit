package me.fru1t.subedit

import me.fru1t.subedit.utils.Utils
import java.io.File


/** Monolith for the UnformatAndCsv tool. */
object UnformatAndCsv {
  // 1: Layer, 2: Start, 3: End, 4: Style, 5: Name, 6: MarginL, 7: MarginR, 8: MarginV, 9: Effect, 10: Text
  private val FORMATTING_STRING = Regex("\\{[^{]+}")
  private const val CSV_EXTENSION = ".csv"
  private const val DELIMITER = "\t"

  /**
   * Console application that creates a CSV from a given .ass file in the following output:
   * `starttime,style,line1,line2`
   */
  fun run() {
    println("RUNNING: Unformat and CSV")

    val file = Utils.askForSourceFile() ?: return
    val csv = File(file.path + CSV_EXTENSION)
    if (csv.exists()) {
      println("${csv.path} already exists, overwrite?")
      if (!Utils.askYes()) {
        println("Oki ignoring, bai bai")
        return
      }
    }

    file.useLines { sourceLines ->
      Utils.writeFile(csv, sequence {
        // header
        yield("Start time${DELIMITER}Style${DELIMITER}Line1${DELIMITER}Line2")

        Utils.assExtractDialogues(sourceLines).forEach { dialogue ->
          println(dialogue)
          val text = dialogue.text.replace(FORMATTING_STRING, "")
          val firstNewlinePosition = text.indexOf(Utils.SUB_NEWLINE)
          val text1: String
          val text2: String
          if (firstNewlinePosition == -1) {
            text1 = text
            text2 = ""
          } else {
            text1 = text.substring(0 until firstNewlinePosition)
            text2 = text.substring(firstNewlinePosition + Utils.SUB_NEWLINE.length)
          }

          yield("${dialogue.start}$DELIMITER" +
                  "${dialogue.style}$DELIMITER" +
                  "$text1$DELIMITER" +
                  "$text2"
          )
        }
      })
    }

    println("Oki done. enjoy")
  }

//  fun MatchResult.get(group: Int): String = groupValues[group].replace("\"", "\"\"")
}
