package me.fru1t.subedit.utils

import me.fru1t.subedit.FirstLineSmaller
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Yeah, it's bad practice. But who's gonna read this source anyway. Monolith for utilities that are used across
 * multiple tools.
 */
object Utils {
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

  /** Load the file contents into a list, one entry per line. */
  fun scanFile(file: File): List<String> {
    val fileContents =  file.readLines()
    return fileContents
  }

  fun askYes(): Boolean = readLine()?.toLowerCase() == "y"
}