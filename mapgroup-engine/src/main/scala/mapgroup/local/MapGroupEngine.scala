package mapgroup.local

import akka.actor.{ActorSystem, Props}
import com.typesafe.config._

import com.github.nscala_time.time.Imports._

import scalaz._
import Scalaz._
import scala.util.Random

import mapgroup.actors._
import mapgroup.model._

object MapGroupEngine {

  val df = new java.text.DecimalFormat("#.##");

  lazy val configFile  = getClass.getClassLoader.getResource("application.conf").getFile
  lazy val config      = ConfigFactory.parseFile(new java.io.File(configFile))
  lazy val system      = ActorSystem("MapGroupSystem", config)

  lazy val elements: List[Element] =
    parseElements(scala.io.Source.fromFile("../data/acm_clients_refined.data").getLines().toList)

  def start = system.actorOf(Props[SourceActor], name="sourceActor")

  def stop = system.shutdown

  def characGroupByCharacValue(es: List[Element]): CharacGroup = {
    val x = elementAffinityByCharacValue(es)
    characGroup(x._2, x._1)
  }

  def characGroup(es: List[Element], cs: Set[(String, String)]): CharacGroup =
    CharacGroup(id         = System.currentTimeMillis().toString,
                groupDate  = new DateTime(),
                elements   = es.map(e => e.id),
                percent    = s"${df.format(((es.length * 1.0) / elements.length) * 100)}%",
                affinities = cs.map(t => t._1 + "::" + t._2).toList)

  /*
   * Returns a tuple of characs and elements that contains characs
   * characs are given away
   */
  def elementAffinityByCharacValue(es: List[Element]): (Set[(String, String)], List[Element]) = {
    val x = elementAffinityByCharacExists(es)
    val keyCharac = x._1.head //Choose one characteristics to be a "key"
    val s  = x._1.flatMap(c => x._2.groupBy(e => (c, e.characs(c)))) //Group client-ids by charac-id and charac-value
                //.filter(y => x._2.forall(e2 => y._2.contains(e2)))
    if (s.length > 0)
     (s.flatten(s2 => Set(s2._1)), s.head._2)
    else
     (Set.empty, List.empty)
  }

  /*
   * Returns a tuple of characs and elements that contains all characs
   * characs are given away
   */
  def elementAffinityByCharacExists(es: List[Element]): (Set[String], List[Element]) = {
    val characCustomerMap = characIndexes(es) //Create a map of customer-id indexed by characs
    val randomCharacs     = sortition(characCustomerMap.keySet) //Sortition of random characs
    println(s"[MapGroupEngine.elementAffinityByCharacExists] - Looking for clients with these characteristics:  ${randomCharacs.toString}")
    val affinityElements  = es.filter(e => randomCharacs.forall(c => e.characs.contains(c))) //All elements with all random characs
    println(s"[MapGroupEngine.elementAffinityByCharacExists] - Found ${affinityElements.length} elements!")
    (randomCharacs, affinityElements)
  }

  /*
   * Returns n random values from a list
   */
  def sortition[T](l: Set[T]): Set[T] =
    Random.shuffle(l).take(Random.nextInt(l.length))

  /*
   * Creates a map of customer-ids indexed by characs
   */
  def characIndexes(es: List[Element]): Map[String, List[String]] =
    es.foldLeft(Map[String, List[String]]())((acc, e) => acc |+| e.characIndexCaracteristicasUnicas)

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
