package me.fru1t.subedit.utils

import java.io.File

/**
 * Yeah, it's bad practice. But who's gonna read this source anyway. Monolith for utilities that are used across
 * multiple tools.
 */
object Utils {
  private const val NEWLINE = "\r\n"

  /** Prompts the user for a file. Returns the file if valid or `null` if no file could be prodded from the user. */
  fun askForFile(): File? {
    println("File path?")
    val file = File(readLine() ?: return null)

    if (!file.exists()) {
      println("Didn't find file... Try again?")
      if (askYes()) {
        return askForFile()
      }
      return null
    }

    if (!file.canWrite() || !file.canRead()) {
      println("Looks like it's protected from here, sorry")
      return null
    }

    return file
  }

  /**
   * Writes the given [contents] to the [file], overwriting without confirmation if the file already exists.
   * Contents are encoded one line per element, using standard newline encoding.
   */
  fun writeFile(file: File, contents: List<String>) {
    file.delete()
    file.createNewFile()
    file.writer().use { writer ->
      contents.forEach { line ->
        writer.write(line + NEWLINE)
      }
    }
  }

  fun askYes(): Boolean = readLine()?.toLowerCase() == "y"

  fun askForInt(message: String): Int {
    println(message)
    val size = readLine()?.toIntOrNull()
    if (size == null) {
      println("Invalid input...")
      return askForInt(message)
    }
    return size
  }
}
