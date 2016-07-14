package mapgroup.model

import com.github.nscala_time.time.Imports._

abstract class Group

case class CharacGroup (val id: String,
                        val groupDate: DateTime,
                        val elements: List[String],
                        val affinities: List[String]) extends Group {

  override def toString(): String = s"[id:${id}|affinities:${affinities.mkString("|")}]"

}
