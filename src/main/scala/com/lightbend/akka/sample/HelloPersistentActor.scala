package com.lightbend.akka.sample

import akka.Done
import akka.actor.{ActorLogging, Props}
import akka.persistence.PersistentActor

object HelloPersistentActor {
  case class Save(message: String)
  case object Get

  def props(id: String): Props = Props(new HelloPersistentActor(id))
}

class HelloPersistentActor(id: String) extends PersistentActor with ActorLogging {
  import HelloPersistentActor._

  override val persistenceId: String = s"helloPersistentActor-$id"

  private var state: String = ""

  log.info("Starting HelloPersistentActor with id: {}", persistenceId)

  def receiveRecover: Receive = {
    case msg: String => this.state = msg
  }

  def receiveCommand: Receive = {
    case Save(msg) =>
      log.info(s"Saving message $msg")
      persist(msg) { _ =>
        sender() ! Done
      }
    case Get => sender() ! state
  }
}
