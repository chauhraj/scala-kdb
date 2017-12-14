package org.github.chauhraj.kxx.test

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpec}

trait WithKDBInstance extends FlatSpec with BeforeAndAfterAll  {

  val qHome: String = sys.env("QHOME")

  val kdbExe: String = s"$qHome/l32/q"

  val port: Int = 8001

  var process: sys.process.Process

  override protected final def beforeAll(): Unit = {
    import sys.process._

    process = s"$kdbExe -p $port".run(true)

  }

  override protected def afterAll(): Unit = {
    process.destroy()
  }
}
