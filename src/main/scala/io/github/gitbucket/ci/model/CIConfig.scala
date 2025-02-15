package io.github.gitbucket.ci.model

import scala.language.postfixOps

trait CIConfigComponent { self: gitbucket.core.model.Profile =>
  import profile.api._
  import self._

  lazy val CIConfigs = TableQuery[CIConfigs]

  class CIConfigs(tag: Tag) extends Table[CIConfig](tag, "CI_CONFIG") {
    val userName = column[String]("USER_NAME", O PrimaryKey)
    val repositoryName = column[String]("REPOSITORY_NAME")
    val buildType = column[String]("BUILD_TYPE")
    val buildScript = column[String]("BUILD_SCRIPT")
    val notification = column[Boolean]("NOTIFICATION")
    val runAfterMerge = column[Boolean]("RUN_AFTER_MERGE")
    val skipWords = column[String]("SKIP_WORDS")
    val runWords = column[String]("RUN_WORDS")
    def * = (userName, repositoryName, buildType, buildScript, notification, runAfterMerge, skipWords.?, runWords.?) <> (CIConfig.tupled, CIConfig.unapply)
  }
}

case class CIConfig(
  userName: String,
  repositoryName: String,
  buildType: String,
  buildScript: String,
  notification: Boolean,
  runAfterMerge: Boolean,
  skipWords: Option[String],
  runWords: Option[String]
){
  lazy val skipWordsSeq: Seq[String] = skipWords.map(_.split(",").map(_.trim).toSeq).getOrElse(Nil)
  lazy val runWordsSeq: Seq[String] = runWords.map(_.split(",").map(_.trim).toSeq).getOrElse(Nil)
}
