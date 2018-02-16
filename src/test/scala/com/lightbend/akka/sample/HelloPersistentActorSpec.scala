package com.lightbend.akka.sample

import java.io.File
import java.util.UUID

import akka.Done
import akka.actor.ActorSystem
import akka.persistence.cassandra.testkit.CassandraLauncher
import akka.testkit.{ImplicitSender, TestDuration, TestKit}
import com.lightbend.akka.sample.HelloPersistentActor.{Get, Save}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.duration.DurationLong
import scala.language.postfixOps

class HelloPersistentActorSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("HelloPersistentActorSpec", ConfigFactory.parseString(
    s"""
       |akka {
       |  persistence.journal.plugin = "cassandra-journal"
       |  persistence.snapshot-store.plugin = "cassandra-snapshot-store"
       |}
     """.stripMargin)))

  override def beforeAll: Unit = {
    val cassandraDirectory = new File("target/" + system.name)
    CassandraLauncher.start(
      cassandraDirectory,
      configResource = CassandraLauncher.DefaultTestConfigResource,
      clean = true,
      port = 0
    )

  }

  override def afterAll: Unit = {
    CassandraLauncher.stop()
    shutdown(system)
  }

  "A HelloPersistentActor" should "persist a sample message and return another message back" in {
    val actorId = UUID.randomUUID().toString
    val actor = system.actorOf(HelloPersistentActor.props(actorId), s"hello-actor-$actorId")

    watch(actor)

    val message = UUID.randomUUID().toString
    actor ! Save(s"hello-$message")
    expectMsg(Done)

    system.stop(actor)
    expectTerminated(actor, 3.seconds.dilated)

    val sameActorAgain = system.actorOf(HelloPersistentActor.props(actorId), s"hello-actor-$actorId")

    sameActorAgain ! Get
    expectMsg(s"hello-$message")
  }
}
