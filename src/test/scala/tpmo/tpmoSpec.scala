package tpmo

import org.specs2.mutable.Specification
import tpmo._
import java.util.NoSuchElementException
import scala.util.Try
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

import org.specs2.time.NoTimeConversions

class tpmoSpec extends Specification with NoTimeConversions {

  "PimpedOption" should {

    "some map to success try" in {
      Option(1).toTry must beSuccessfulTry.withValue(1)
    }

    "none map to failed try" in {
      None.toTry must beFailedTry.withThrowable[NoSuchElementException]
    }
  }

  "PimpedTry" should {

    "success map to success future" in {
      Try(1 / 1).toFuture must be_==(1).await
    }

    "failed map to failed future" in {
      val tryF = Try(1 / 0).toFuture
      Await.result(tryF, 1.second) must throwA[ArithmeticException]
    }
  }

  "TryPimpMyOption" should {

    "work with futures" in {
      val sumF = for {
        f1 <- Option(1).toFuture
        f2 <- Try(1/1).toFuture
        f3 <- Future(1/1)
      } yield f1 + f2 + f3

      sumF must be_==(3).await
    }

    "faild for none" in {
      val none: Option[Int] = None
      val sumF = for {
        f1 <- none.toFuture
        f2 <- Try(1/1).toFuture
        f3 <- Future(1/1)
      } yield f1 + f2 + f3

      Await.result(sumF, 1.second) must throwA[NoSuchElementException]
    }

    "faild for exception" in {
      val sumF = for {
        f1 <- Option(1).toFuture
        f2 <- Try(1/0).toFuture
        f3 <- Future(1/1)
      } yield f1 + f2 + f3

      Await.result(sumF, 1.second) must throwA[ArithmeticException]
    }
  }
}
