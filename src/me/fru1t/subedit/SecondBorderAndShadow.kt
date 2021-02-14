package me.fru1t.subedit

import me.fru1t.subedit.utils.Utils

/** Monolith for the SecondBorderAndShadow tool. */
object SecondBorderAndShadow {
  private const val BORDER_SIZE_CODE_TEMPLATE = "{\\bord%d}"
  private const val BLUR_CODE_TEMPLATE = "{\\blur%d}"
  private const val BORDER_COLOR_CODE_TEMPLATE = "{\\3c&H%s&}"
  private const val BORDER_ALPHA_CODE_TEMPLATE = "{\\3a&H%s&}"

  /**
   * Console application that turns adds a 2nd border and blurred shadow to each subtitle.
   * This method is self-contained to accept input via CLI.
   */
  fun run() {
    println("RUNNING: Add Double Outlines And Blurred Shadow")

    println("Source file (make sure all lines have no shadows)")
    val inFile = Utils.askForSourceFile() ?: return

    println("Target file")
    val outFile = Utils.askForOutputFile() ?: return

    println("First border size?")
    val borderSize1 = Utils.askForInt()

    println("First border color? (in BBGGRR format) (default: the border color of the style)")
    val borderColor1 = Utils.askForColor()

    println("Second border size? (First border width + second border width)")
    val borderSize2 = Utils.askForInt()

    println("Second border color? (in BBGGRR format) (default: the border color of the style)")
    val borderColor2 = Utils.askForColor()

    println("Shadow size?")
    val shadowSize = Utils.askForInt()

    println("Shadow color? (in hexadecimal BBGGRR format) (default: the border color of the style)")
    val shadowColor = Utils.askForColor()

    println("Shadow alpha? (in hexadecimal AA format. 00 = fully opaque)")
    val shadowAlpha = Utils.askForAlpha() ?: return

    println("Shadow blur amount? (non-negative integer)")
    val shadowBlur = Utils.askForInt()

    val prefixForEachLayer = listOf(
            BORDER_ALPHA_CODE_TEMPLATE.format(shadowAlpha)
                    + if (shadowColor == null) "" else  BORDER_COLOR_CODE_TEMPLATE.format(shadowColor)
                    + BORDER_SIZE_CODE_TEMPLATE.format(shadowSize) + BLUR_CODE_TEMPLATE.format(shadowBlur),
            (if (borderColor2 == null) "" else BORDER_COLOR_CODE_TEMPLATE.format(borderColor2))
                    + BORDER_SIZE_CODE_TEMPLATE.format(borderSize2),
            (if (borderColor1 == null) "" else BORDER_COLOR_CODE_TEMPLATE.format(borderColor1))
                    + BORDER_SIZE_CODE_TEMPLATE.format(borderSize1)
    )

    inFile.useLines { sourceLines ->
      val transformed = Utils.assFlatMap(sourceLines) { dialogue ->
        sequence {
          for ((index, prefix) in prefixForEachLayer.withIndex()) {
            val newDlg = dialogue.copy(text = prefix + dialogue.text, layer = (dialogue.layer.toInt() + index).toString())
            yield(newDlg)
          }
        }
      }

      Utils.writeFile(outFile, transformed)
    }
    println("Oki done. enjoy")
  }
}
