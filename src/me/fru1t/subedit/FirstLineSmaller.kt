package me.fru1t.subedit

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

val DIALOGUE_REGEX = Regex("^(Dialogue: [^,]+,[^,]+,[^,]+,[^,]+,[^,]*,\\d+,\\d+,\\d+,[^,]*,)(.*)\$")
const val NEWLINE = "\\N"
const val FONT_WRAPPER_BEFORE = "{\\fs20}"
const val FONT_WRAPPER_AFTER = "{\\fs}"

object FirstLineSmaller {
  fun run() {
    println("File path?")
    val file = File(readLine() ?: return)

    if (!file.exists()) {
      println("Didn't find file... Try again?")
      if (askYes()) {
        run()
      }
      return
    }

    if (!file.canWrite() || !file.canRead()) {
      println("Looks like it's protected from here, sorry")
      return
    }

    val fileContents: ArrayList<String> = ArrayList()
    val fileScanner = Scanner(file)
    while (fileScanner.hasNextLine()) {
      fileContents.add(fileScanner.nextLine())
    }
    fileScanner.close()

    var nonDialogueLines = 0
    var convertedLines = 0
    var nonConvertedLines = 0
    for (lineNumber in 0 until fileContents.size) {
      println("[$lineNumber/${fileContents.size}] Non Dialogue: $nonDialogueLines; Converted: $convertedLines; NonConverted: $nonConvertedLines")

      val line = fileContents[lineNumber]
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
    if (!askYes()) {
      println("Oki, ignoring. Bai")
      return
    }

    file.delete()
    file.createNewFile()
    fileContents.forEach { line -> file.appendText(line + "\r\n") }

    println("Oki done. enjoy")
  }

  private fun askYes(): Boolean = readLine()?.toLowerCase() == "y"
}