package com.github.chauhraj

import java.time._
import java.util
import java.util.UUID

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.handler.codec._
import nio.NioEventLoopGroup
import socket._
import socket.nio.NioSocketChannel

import scala.reflect._
import _root_.io.netty.buffer.ByteBuf


package object kdb {

  case class KDBClient (remote: String, port: Int) {

    private def connect(): Unit = {
      val eventloop = new NioEventLoopGroup()
      val bootstrap = new Bootstrap()
        .channel(classOf[NioSocketChannel])
        .group(eventloop)
        .handler(KDBClientChannelInitializer())
        .connect(remote, port)
    }
  }

  case class KDBClientChannelInitializer() extends ChannelInitializer[SocketChannel] {
    override def initChannel(ch: SocketChannel): Unit = {
      ch.pipeline().addLast("decoder", KDBProtocolDecoder())
    }
  }

  case class KDBProtocolDecoder() extends ByteToMessageDecoder {
    override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: util.List[AnyRef]): Unit = ???
  }

  case class KDBProtocolEncoder() extends MessageToByteEncoder[types.Type[_]] {
    override def encode(ctx: ChannelHandlerContext, msg: types.Type[_], out: ByteBuf): Unit = ???
  }

  object types {

    type QArray[T] = Array[T]
    sealed abstract class Type[T: ClassTag](chr: Char, typeId: Byte, size: Byte) {
      val clazzTag: ClassTag[T] = classTag[T]
    }

    case object QBoolean   extends Type[Boolean]('b', -1, 1)
    case object QGuid      extends Type[UUID]('g', -2, 16)
    case object QByte      extends Type[Byte]('x', -4, 1)
    case object QShort     extends Type[Short]('h', -5, 2)
    case object QInt       extends Type[Int]('i', -6, 4)
    case object QLong      extends Type[Long]('j', -7, 8)
    case object QReal      extends Type[Float]('e', -8, 4)
    case object QFloat     extends Type[Double]('f', -9, 8)
    case object QChar      extends Type[Char]('c', -10, 1)
    case object QSymbol    extends Type[String]('s', -11, 16)
    case object QTimestamp extends Type[LocalDateTime]('p', -12, 8)
    case object QMonth     extends Type[Month]('m', -13, 4)
    case object QDate      extends Type[LocalDate]('d', -14, 4)
    case object QDatetime  extends Type[LocalDateTime]('z', -15, 8)
    case object QTimespan  extends Type[Int]('n', -16, 4)
    case object QMinute    extends Type[Int]('u', -17, 1)
    case object QSecond    extends Type[Short]('v', -18, 1)
    case object QTime      extends Type[LocalTime]('t', -19, 4)

  }

  def typeOf(o: Any): Int = o match {
    case t: Boolean ⇒ -1
    case t: UUID ⇒ -2
    case t: Byte ⇒ -4
    case t: Short ⇒ -5
    case t: Int ⇒ -6
    case t: Long ⇒ -7
    case t: Float ⇒ -8
    case t: Double ⇒ -9
    case t: Char ⇒ -10
    case t: String ⇒ -11
    case t: LocalDate ⇒ -14
    case t: LocalTime ⇒ -19
    case t: LocalDateTime ⇒ -12
    case t: LocalDate ⇒ -15
    case t: Duration ⇒ -16
    case t: Month ⇒ -13
//    case t: Minute ⇒ -17
//    case t: Second ⇒ -18
    case t: Array[Boolean] ⇒ 1
    case t: Array[UUID] ⇒ 2
    case t: Array[Byte] ⇒ 4
    case t: Array[Short] ⇒ 5
    case t: Array[Int] ⇒ 6
    case t: Array[Long] ⇒ 7
    case t: Array[Float] ⇒ 8
    case t: Array[Double] ⇒ 9
    case t: Array[Char] ⇒ 10
    case t: Array[String] ⇒ 11
    case t: Array[LocalDate] ⇒ 14
    case t: Array[LocalTime] ⇒ 19
    case t: Array[LocalDateTime] ⇒ 12
    case t: Array[LocalDate] ⇒ 15
    case t: Array[Duration] ⇒ 16
//    case t: Array[Month] ⇒ 13
//    case t: Array[Minute] ⇒ 17
//    case t: Array[Second] ⇒ 18
//    case t: Flip ⇒ 98
//    case t: Dict ⇒ 99
  }
}
