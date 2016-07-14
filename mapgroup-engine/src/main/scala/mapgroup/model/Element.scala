package mapgroup.model

import scalaz._
import Scalaz._

class Element(val id: String,
              val characs: Map[String, String]) {

  lazy val characIndex: Map[String, List[String]] = {
    characs.foldLeft(Map[String, List[String]]())((acc, c) => acc |+| Map(c._1 -> List(id)))
  }

  override def toString(): String = id

}

object Element {

   def apply(id: String,
             characs: Map[String, String]) = new Element(id, characs)

}
