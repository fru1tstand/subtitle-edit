package me.fru1t.subedit

import me.fru1t.subedit.utils.Utils


/** Monolith for the FirstLineSmaller tool. */
object FirstLineSmaller {
  private val DIALOGUE_REGEX = Regex("^(Dialogue: [^,]+,[^,]+,[^,]+,[^,]+,[^,]*,\\d+,\\d+,\\d+,[^,]*,)(.*)\$")
  private const val NEWLINE = "\\N"
  private const val FONT_WRAPPER_BEFORE = "{\\fs20}"
  private const val FONT_WRAPPER_AFTER = "{\\fs}"

  /**
   * Console application that adds a smaller font style to the first line of a multi-line subtitle. This method is
   * self-contained to accept input via CLI.
   *
   * Ignores lines that...
   *  - Are empty or single-line
   *  - Already have a style (even if it's not a font size style)
   */
  fun run() {
    println("RUNNING: First line smaller")

    val file = Utils.askForFile() ?: return
    val fileContents = ArrayList(file.readLines())

    var nonDialogueLines = 0
    var convertedLines = 0
    var nonConvertedLines = 0
    for (lineNumber in fileContents.indices) {
      val line = fileContents[lineNumber]
      println(
        "[$lineNumber/${fileContents.size}] Non Dialogue: $nonDialogueLines; " +
            "Converted: $convertedLines; NonConverted: $nonConvertedLines; - $line")

      val matcher = DIALOGUE_REGEX.matchEntire(line)
      if (matcher == null) {
        nonDialogueLines++
        continue;
      }
      val firstNewline = matcher.groupValues[2].indexOf(NEWLINE)
      if (firstNewline == -1) {
        nonConvertedLines++
        println("Ignoring single line")
        continue;
      }
      if (matcher.groupValues[2][firstNewline - 1] == '}') {
        nonConvertedLines++
        println("Ignored already fonted line")
        continue;
      }

      fileContents[lineNumber] =
        matcher.groupValues[1] +
            FONT_WRAPPER_BEFORE +
            matcher.groupValues[2].substring(0 until firstNewline) +
            FONT_WRAPPER_AFTER +
            NEWLINE +
            matcher.groupValues[2].substring(firstNewline + 2)
      convertedLines++
    }

    println("[final] Non Dialogue: $nonDialogueLines; Converted: $convertedLines; NonConverted: $nonConvertedLines")

    if (convertedLines == 0) {
      println("Nothing to do here. bai")
      return
    }

    println("Found $convertedLines line(s) to convert. Continue? ")
    if (!Utils.askYes()) {
      println("Oki, ignoring. Bai")
      return
    }

    file.delete()
    file.createNewFile()
    fileContents.forEach { line -> file.appendText(line + "\r\n") }

    println("Oki done. enjoy")
  }

}