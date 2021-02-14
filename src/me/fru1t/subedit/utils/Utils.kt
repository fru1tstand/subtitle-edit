package me.fru1t.subedit.utils

import java.io.File

/**
 * Yeah, it's bad practice. But who's gonna read this source anyway. Monolith for utilities that are used across
 * multiple tools.
 */
object Utils {
  public const val SUB_NEWLINE = "\\N"

  private val DIALOGUE_REGEX = Regex("^Dialogue: ([^,]+),([^,]+),([^,]+),([^,]+),([^,]*),(\\d+),(\\d+),(\\d+),([^,]*),(.*)\$")
  private val COLOR_REGEX = Regex("[0-9A-F]{6}")
  private val ALPHA_REGEX = Regex("[0-9A-F]{2}")

  /** Prompts the user for a file to read from.
   * Returns the file if valid or `null` if no file could be prodded from the user. */
  fun askForSourceFile(): File? {
    println("File path?")
    val file = File(readLine() ?: return null)

    if (!file.exists()) {
      println("Didn't find file... Try again?")
      if (askYes()) {
        return askForSourceFile()
      }
      return null
    }

    if (!file.canRead()) {
      println("Looks like it's protected from here, sorry")
      return null
    }

    return file
  }

  /** Prompts the user for a file to output to.
   * Returns the file if valid or `null` if no file could be prodded from the user. */
  fun askForOutputFile(): File? {
    println("File path?")
    val file = File(readLine() ?: return null)

    if (!file.exists()) {
      return file
    } else if (!file.canWrite()) {
      println("Looks like it's protected from here, sorry")
      return null
    } else {
      println("File already exists... Overwrite?")
      if (askYes()) {
        return file
      }
      return null
    }
  }

  /**
   * Writes the given [contents] to the [file], overwriting without confirmation if the file already exists.
   * Contents are encoded one line per element, using OS-specific newline encoding.
   */
  fun writeFile(file: File, contents: Sequence<String>) {
    file.delete()
    file.createNewFile()
    file.printWriter().use { writer ->
      contents.forEach { line ->
        writer.println(line)
      }
    }
  }

  fun askYes(): Boolean = readLine()?.toLowerCase() == "y"

  fun askForInt(): Int {
    val size = readLine()?.toIntOrNull()
    if (size == null) {
      println("Invalid input...")
      return askForInt()
    }
    return size
  }

  /**
   * Returns a sequence that transforms [fileLines] of an ASS file using [lineTransformer].
   * Non-dialogue lines will bypass [lineTransformer].
   */
  fun assFlatMap(fileLines: Sequence<String>, lineTransformer: (Dialogue) -> Sequence<Dialogue>): Sequence<String> = sequence {
    for (line in fileLines) {
      val matcher = DIALOGUE_REGEX.matchEntire(line)
      if (matcher == null) {
        yield(line)
        continue
      }
      val dialogue = Dialogue.fromList(matcher.groupValues.drop(1))  // drop the first which is the entire string
      val transformedLines = lineTransformer(dialogue)
      for (line2 in transformedLines) yield(line2.toFormattedString())
    }
  }

  fun assExtractDialogues(fileLines: Sequence<String>) = sequence {
    for (line in fileLines) {
      val matcher = DIALOGUE_REGEX.matchEntire(line) ?: continue
      val dialogue = Dialogue.fromList(matcher.groupValues.drop(1))  // drop the first which is the entire string
      yield(dialogue)
    }
  }

  fun askForColor(): String? {
    val color = readLine() ?: return null
    if (!color.matches(COLOR_REGEX)) {
      println("Invalid input...")
      return askForColor()
    }
    return color
  }

  fun askForAlpha(): String? {
    val alpha = readLine() ?: return null
    if (!alpha.matches(ALPHA_REGEX)) {
      println("Invalid input...")
      return askForAlpha()
    }
    return alpha
  }
}
