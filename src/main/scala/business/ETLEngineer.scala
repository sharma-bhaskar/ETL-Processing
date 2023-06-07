package business

import java.io.File

trait ETLEngineer {

  def readAllFiles(sourceDir: String): List[String]

  def readData(inputFiles: File): Either[String, String]

  def transformText(text: String): String

  def writeFile(outputFile: File, transformedText: String): Either[String, Unit]
}
