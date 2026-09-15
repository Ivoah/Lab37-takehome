// import java.util.UUID
import play.api.libs.json.*
import net.ivoah.squall.*
import java.nio.file.{Files, Paths}
import scala.io.Source

object Database {
  given Connector = {
    if (Files.notExists(Paths.get("database.db"))) {
      given Connector("jdbc:sqlite:database.db")
      val schema = Source.fromResource("schema.sql").getLines().mkString("\n")
      schema.sql.execute()
      summon[Connector]
    } else Connector("jdbc:sqlite:database.db")
  }

  def saveOrder(order: Order): String = {
    sql"""
      insert into "order" (id, updated, firstName, lastName, items, notes, meta)
      values (${order.id}, ${order.updated}, ${order.firstName}, ${order.lastName}, ${order.items}, ${order.notes}, ${Json.stringify(Json.toJson(order.meta))})
      returning id
    """.query(_.getString("id")).head
  }

  def getOrders(): Map[String, Seq[Order]] =
    sql"""select * from "order""""
      .query(r => r.getString("id") -> Order.fromResultSet(r))
      .groupMap(_._1)(_._2)
      .view.mapValues(_.sortBy(_.updated)).toMap
  def getOrder(id: String): Option[Seq[Order]] = {
    val orders = sql"""select * from "order" where id=$id""".query(Order.fromResultSet)
    if (orders.nonEmpty) Some(orders) else None
  }
}
