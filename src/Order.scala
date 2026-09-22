import org.apache.commons.csv.CSVRecord
import play.api.libs.json.*
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.UUID
import java.time.LocalTime

case class Order(id: String, updated: LocalDateTime, firstName: String, lastName: String, items: String, notes: String, dispatched: Boolean, scheduled: LocalDateTime, meta: Map[String, String]) derives Format
object Order {
  def fromResultSet(r: ResultSet): Order = Order(
    r.getString("id"), r.getTimestamp("updated").toLocalDateTime,
    r.getString("firstName"), r.getString("lastName"),
    r.getString("items"), r.getString("notes"), r.getBoolean("dispatched"), r.getTimestamp("scheduled").toLocalDateTime(),
    Json.parse(r.getString("meta")).as[Map[String, String]]
  )

  def fromWebhook(o: WebhookOrder): Order = Order(
    o.order_id, LocalDateTime.now(),
    o.first_name, o.last_name,
    o.items.mkString(", "), o.notes, false, LocalDateTime.now(),
    Map(
      "order_source" -> o.order_source,
      "restaurant" -> o.restaurant,
      "total" -> o.total.toString
    )
  )

  def fromExternalAPI(id: String, o: APIOrder): Order = Order(
    id, LocalDateTime.now(),
    "", "",
    o.name, "", false, LocalDateTime.now(),
    Map(
      "category" -> o.category,
      "price" -> o.price.toString,
      "status" -> o.status.toString
    )
  )
  
  // Wade,Wilson,"Fried banana, Sweet tea, Apple pie, Classic chicken sandwich, Espresso, Croissant",,true,breakfast
  def fromCSV(record: CSVRecord): Order = Order(
    UUID.randomUUID().toString, LocalDateTime.now(),
    record.get("first_name"), record.get("last_name"),
    record.get("items"), record.get("notes"), false, record.get("meal") match {
      case "breakfast" => LocalDateTime.now().nextAtTime(LocalTime.of(7, 0))
      case "lunch" => LocalDateTime.now().nextAtTime(LocalTime.of(13, 0))
      case "dinner" => LocalDateTime.now().nextAtTime(LocalTime.of(18, 0))
      case m =>
        println(s"Unknown meal \"$m\"")
        LocalDateTime.now()
    },
    Map(
      "tomorrow" -> record.get("tomorrow"),
      "meal" -> record.get("meal")
    )
  )
}
