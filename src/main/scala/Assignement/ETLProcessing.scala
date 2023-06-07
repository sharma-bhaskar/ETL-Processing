package Assignement

import business.ETLEngineer
import java.io._


class ETLProcessing(sourceDir: String, destinationDir: String) extends ETLEngineer {

  def processEngineer(): Unit = {

    val files = readAllFiles(sourceDir)

    files.foreach { file =>
      val inputFiles = new File(file)
      val outputFile = new File(destinationDir + "/" + "output_" + inputFiles.getName)

      val result = for {
        text <- readData(inputFiles)
        transformedText = transformText(text)
        _ <- writeFile(outputFile, transformedText)
      } yield ()

      result match {
        case Left(error) =>
          println(s"Error Processing the file ${inputFiles.getName}: $error")
        case _ =>
      }
    }
  }

  override def readAllFiles(sourceDir: String): List[String] = {
    val d = new File(sourceDir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).map(_.getAbsolutePath).toList
    } else {
      List.empty[String]
    }
  }

  override def readData(inputFiles: File): Either[String, String] = {
    try {
      val source = scala.io.Source.fromFile(inputFiles)
      val text = try source.mkString finally source.close()
      Right(text)
    } catch {
      case ex: Exception =>
        Left(ex.getMessage)
    }
  }

  override def transformText(text: String): String = {
    val words = text.split("\\s+")
    words.map(word => word.head.toUpper + word.tail.toLowerCase).mkString(" ")
  }

  override def writeFile(outputFile: File, transformedText: String): Either[String, Unit] = {
    try {
      val writer = new PrintWriter(outputFile)
      try {
        writer.write(transformedText)
        Right(())
      } finally {
        writer.close()
      }
    } catch {
      case ex: Exception =>
        Left(ex.getMessage)
    }
  }
}


object ETLProcessing {

  def main(args: Array[String]): Unit = {
    val sourceDir = "src/main/resources"
    val destinationDir = "src/main/resources/output"

    val processor = new ETLProcessing(sourceDir, destinationDir)
    processor.processEngineer()

  }
}
