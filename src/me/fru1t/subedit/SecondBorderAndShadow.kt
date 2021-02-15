package me.fru1t.subedit

import me.fru1t.subedit.utils.AssFile.transformDialogue
import me.fru1t.subedit.utils.Utils


/** Monolith for the SecondBorderAndShadow tool. */
object SecondBorderAndShadow {
  private const val BORDER_SIZE_CODE_TEMPLATE = "{\\bord%d}"
  private const val BLUR_CODE_TEMPLATE = "{\\blur%d}"
  private const val BORDER_COLOR_CODE_TEMPLATE = "{\\3c&H%s&}"
  private const val BORDER_ALPHA_CODE_TEMPLATE = "{\\3a&H%s&}"

  /**
   * Console application that adds a border and blurred shadow to each subtitle,
   * generating 2 additional subtitle lines per input line.
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
                    + (shadowColor?.let { BORDER_COLOR_CODE_TEMPLATE.format(it) } ?: "")
                    + BORDER_SIZE_CODE_TEMPLATE.format(shadowSize)
                    + BLUR_CODE_TEMPLATE.format(shadowBlur),
            (borderColor2?.let { BORDER_COLOR_CODE_TEMPLATE.format(it) } ?: "")
                    + BORDER_SIZE_CODE_TEMPLATE.format(borderSize2),
            (borderColor1?.let { BORDER_COLOR_CODE_TEMPLATE.format(it) } ?: "")
                    + BORDER_SIZE_CODE_TEMPLATE.format(borderSize1)
    )

    transformDialogue(inFile, outFile) { dialogue ->
      sequence {
        for ((index, prefix) in prefixForEachLayer.withIndex()) {
          yield(dialogue.copy(text = prefix + dialogue.text, layer = (dialogue.layer.toInt() + index).toString()))
        }
      }
    }

    println("Oki done. enjoy")
  }
}
