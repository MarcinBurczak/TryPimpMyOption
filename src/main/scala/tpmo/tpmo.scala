package tpmo

import scala.util.Try
import scala.concurrent.{Promise, Future}

object tpmo {

  implicit class PimpedTry[T](_try: Try[T]) {
    def toFuture: Future[T] = Promise().complete(_try).future
  }

  implicit class PimpedOption[T](option: Option[T]) {
    def toTry = Try(option.get)
    def toFuture = toTry.toFuture
  }
}
