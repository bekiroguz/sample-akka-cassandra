package com.lightbend.akka.sample

import akka.persistence.cassandra.testkit.CassandraLauncher
import com.typesafe.config.ConfigFactory

object CassandraConfig {

  val cassandraPort = CassandraLauncher.randomPort

  val config = ConfigFactory.parseString(
    s"""
       |akka.persistence.journal.plugin = "cassandra-journal"
       |akka.persistence.snapshot-store.plugin = "cassandra-snapshot-store"
       |
       |cassandra-journal.port = $cassandraPort
       |cassandra-snapshot-store.port = $cassandraPort
     """.stripMargin)

}
