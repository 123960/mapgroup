def mapName(characs: String): String = {
  val mapCharac = Map("1" -> "Account", "2" -> "Client Type", "3" -> "MDN", "4" -> "Product", "5" -> "Technology", "6" -> "Area Code", "7" -> "Company Operator", "8" -> "Geographical State", "9" -> "Account State", "10" -> "Account State Date", "11" -> "Equip Model", "12" -> "Equip Manufacturer", "13" -> "Account Activation", "14" -> "Last Recharge Date", "15" -> "Portability Status", "16" -> "Last Recharge Value")
  val odds  = characs.split("#").zipWithIndex.filter(x => x._2 % 2 == 0).map(x => mapCharac(x._1))
  val evens = characs.split("#").zipWithIndex.filter(x => x._2 % 2 == 1).map(x => x._1)
  odds.zip(evens).map(t => s"${t._1}=${t._2}").mkString("|")
}

val f = scala.io.Source.fromFile("../data/acm_clients_raw.csv").getLines().toList;

val f2 = f.map(l => {
  val v = l.replaceAll("\"", "").split(";")
  v(0) + ";" + v(1) + ";" + mapName(v(2))
}) mkString("\n")

  new java.io.PrintWriter("../data/acm_clients_refined.data") { write(f2); close }
