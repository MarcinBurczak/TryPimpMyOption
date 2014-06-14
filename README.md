Try pimp my Option
===================

This library add simple utils to work with Future class when make asynchronous programming.
Why? Because Option monad is less than Try monad and Try is less than Future:
    Option < Try < Future

Work with monads of different types is hard, but we can convert them to upper monad.

Try pimp my Option library add:
- method toFuture to Try class
- methods toTry and toFuture to Option class

Now we can convert Option or Try to Future and use them in for-comprehension:

    val sumF: Future[Int] = for {
      f1 <- Option(1).toFuture
      f2 <- Try(1/1).toFuture
      f3 <- Future(1/1)
    } yield f1 + f2 + f3
