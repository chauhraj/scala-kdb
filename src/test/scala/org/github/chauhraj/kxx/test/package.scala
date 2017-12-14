package org.github.chauhraj.kxx

import java.nio.ByteOrder

import scodec._
import scodec.bits._
import scodec.codecs._

package object test {

  sealed trait MessageType
  case object Async extends MessageType
  case object Sync  extends MessageType
  case object Response extends MessageType

  implicit val messageType: Codec[MessageType] = {
    discriminated[MessageType].by(uint8)
      .typecase(0, provide(Async))
      .typecase(1, provide(Sync))
      .typecase(2, provide(Response))
  }

  implicit val byteOrder: Codec[ByteOrdering] = {
    discriminated[ByteOrdering].by(uint8)
      .typecase(0, provide(ByteOrdering.BigEndian))
      .typecase(1, provide(ByteOrdering.LittleEndian))
  }


  case class QMessage(byteOrder: ByteOrdering, messageType: MessageType, ignore: Int, size: Long, dataType: Int, value: Long)
  object QMessage {

    /*
    ("magic_number"  | byteOrdering    ) >>:~ { implicit ordering =>
      ("version_major" | guint16         ) ::
        ("version_minor" | guint16         ) ::
        ("thiszone"      | gint32          ) ::
        ("sigfigs"       | guint32         ) ::
        ("snaplen"       | guint32         ) ::
        ("network"       | LinkType.codec  )
    }}.as[GlobalHeader]
    */
    implicit val codec: Codec[QMessage] = "kdb-message" | {
      ("byte-ordering"    | byteOrder  >>:~ { implicit byteOrdering =>
        ("message-type"   | messageType   ) ::
        ("ignore"         | uint16   ) ::
        ("message-length" | endiannessDependent(uint32, uint32L)) ::
        ("data-type"      | endiannessDependent(int8, int8L)) ::
        ("value"          | endiannessDependent(uint32, uint32L))
    })}.as[QMessage]
  }
}
