import net.ivoah.vial.*

import org.apache.commons.csv.{CSVParser, CSVFormat}
import java.io.File
import java.nio.file.Paths
import scala.jdk.CollectionConverters.*
import play.api.libs.json.*
import scala.util.Try

class Endpoints() {
  def router: Router = Router {
    case ("GET" , "/", r) => Response(Templates(r).root())
    case ("GET", s"/static/$file", _) => Response.forFile(Paths.get("static"), Paths.get(file))

    case ("GET", "/orders", r) => Response(Templates(r).orders(Database.getOrders()))
    case ("GET", s"/order/$id", r) => Database.getOrder(id).map(history => Response(Templates(r).order(history))).getOrElse(Response.NotFound())

    case ("POST", "/ingest/csv", r) =>
      r.form.expect("csv") { (csv: File) =>
        val orders = CSVParser.parse(csv, java.nio.charset.Charset.defaultCharset(), CSVFormat.DEFAULT.builder().setHeader().get()).asScala.map(Order.fromCSV)
        orders.foreach(Database.saveOrder)
        Response(Json.stringify(Json.toJson(orders)))
      }.getOrElse(Response.BadRequest())

    case ("POST", "/ingest/webhook", r) =>
      Try(Json.parse(r.body).as[WebhookOrder])
        .map(Order.fromWebhook)
        .map(o => {println(o); o})
        .map(Database.saveOrder)
        .fold(err => Response.BadRequest(err.toString), id => Response(Json.stringify(Json.toJson(id))))
  }
}
