package me.fru1t.subedit.utils

import java.io.File

object AssFile {
  fun transformDialogue(inFile: File, outFile: File, transform: (Dialogue) -> Sequence<Dialogue>) {
    inFile.useLines { sourceLines ->
      val transformed = Utils.assFlatMap(sourceLines, transform)
      Utils.writeFile(outFile, transformed)
    }
  }
}
