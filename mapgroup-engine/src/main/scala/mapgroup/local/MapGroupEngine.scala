package mapgroup.local

import scalaz._
import Scalaz._
import scala.util.Random
import com.github.nscala_time.time.Imports._

import mapgroup.model._

object MapGroupEngine {

  def characGroup(es: List[Element], cs: Set[(String, String)]): CharacGroup =
    CharacGroup(id         = System.currentTimeMillis().toString,
                groupDate  = new DateTime(),
                elements   = es.map(e => e.id),
                affinities = cs.map(t => t._1 + "::" + t._2).toList)

  /*
   * Returns a tuple of characs and elements that contains characs
   * characs are given away
   */
  def elementAffinityByCharacValue(elements: List[Element]): (Set[(String, String)], List[Element]) = {
    val x = elementAffinityByCharacExists(elements)
    val s = x._1.flatMap(c => x._2.groupBy(e => (c, e.characs(c))))
                .filter(y => x._2.forall(e2 => y._2.contains(e2)))
    (s.flatten(s2 => Set(s2._1)), s.head._2)
  }

  /*
   * Returns a tuple of characs and elements that contains all characs
   * characs are given away
   */
  def elementAffinityByCharacExists(elements: List[Element]): (Set[String], List[Element]) = {
    val characCustomerMap = characIndexes(elements) //Create a map of customer-id indexed by characs
    val randomCharacs     = sortition(characCustomerMap.keySet) //Sortition of random characs
    val affinityElements  = elements.filter(e => randomCharacs.forall(c => e.characs.contains(c))) //All elements with all random characs
    (randomCharacs, affinityElements)
  }

  /*
   * Returns n random values from a list
   */
  def sortition[T](l: Set[T]): Set[T] =
    Random.shuffle(l).take(Random.nextInt(l.length))

  /*
   * Creates a map of customer-id indexed by characs
   */
  def characIndexes(elements: List[Element]): Map[String, List[String]] =
    elements.foldLeft(Map[String, List[String]]())((acc, e) => acc |+| e.characIndex)

  /*
   * Transforms a string list in the format number;customer-id;characs in a element list
   */
  def parseElements(str: List[String]): List[Element] = {
    for(l <- str) yield parseElement(l)
  } toList

  /*
   * Transforms a string in the format number;customer-id;characs in a element object
   */
  def parseElement(str: String): Element = {
    val s = str.split(";")
    Element(s(1), parseCharac(s(2)))
  }

  /*
   * Transforms a string in the format key=value|key=value in a map
   */
  def parseCharac(str: String): Map[String, String] =
    str.split("\\|").foldLeft(Map[String, String]())((acc, c) => {
      val s = c.split("=")
      acc |+| Map(s(0) -> s(1))
    })

}
