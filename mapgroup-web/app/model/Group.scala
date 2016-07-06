package model

import com.github.nscala_time.time.Imports._

abstract class Group

case class CharacGroup (val id: String,
                        val groupDate: DateTime,
                        val elements: List[String],
                        val groupAffinity: List[String]) extends Group
