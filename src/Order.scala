import org.apache.commons.csv.CSVRecord
import play.api.libs.json.*
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.UUID

case class Order(id: String, updated: LocalDateTime, firstName: String, lastName: String, items: String, notes: String, meta: Map[String, String]) derives Format
object Order {
  def fromResultSet(r: ResultSet): Order = Order(
    r.getString("id"), r.getTimestamp("updated").toLocalDateTime,
    r.getString("firstName"), r.getString("lastName"), r.getString("items"), r.getString("notes"),
    Json.parse(r.getString("meta")).as[Map[String, String]]
  )

  def fromWebhook(o: WebhookOrder): Order = Order(
    o.order_id, LocalDateTime.now(),
    o.first_name, o.last_name, o.items.mkString(", "), o.notes,
    Map(
      "order_source" -> o.order_source,
      "restaurant" -> o.restaurant,
      "total" -> o.total.toString
    )
  )
  
  // Wade,Wilson,"Fried banana, Sweet tea, Apple pie, Classic chicken sandwich, Espresso, Croissant",,true,breakfast
  def fromCSV(record: CSVRecord): Order = Order(
    UUID.randomUUID().toString, LocalDateTime.now(),
    record.get("first_name"), record.get("last_name"), record.get("items"), record.get("notes"),
    Map(
      "tomorrow" -> record.get("tomorrow"),
      "meal" -> record.get("meal")
    )
  )
}

// {"order_id": "c59a0083-581d-4eb9-946e-98b32890be3a", "order_source": "Overeats", "restaurant": "Sam & Ella's", "first_name": "Laura", "last_name": "Kinney", "total": 144.16, "items": ["Lemon meringue pie", "Cherry pie \u00e0 la mode", "Chicken fried steak", "Grilled cheese with tomato soup"], "notes": ""}
case class WebhookOrder(
  order_id: String,
  order_source: String,
  restaurant: String,
  first_name: String,
  last_name: String,
  total: Double,
  items: Seq[String],
  notes: String
) derives Format
