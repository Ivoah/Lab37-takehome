import net.ivoah.vial.*

import org.apache.commons.csv.{CSVParser, CSVFormat}
import java.io.File
import java.nio.file.Paths
import scala.jdk.CollectionConverters.*
import play.api.libs.json.*
import scala.util.Try
import java.time.LocalDateTime
import scala.util.Success

class Endpoints() {
  private var lastPoll = LocalDateTime.MIN

  def router: Router = Router {
    case ("GET" , "/", r) => Response.Redirect("/orders")
    case ("GET", s"/static/$file", _) => Response.forFile(Paths.get("static"), Paths.get(file))

    case ("GET", "/orders", _) => Response(Templates.orders(Database.getOrders()))
    case ("GET", s"/order/$id", _) => Database.getOrder(id).map(history => Response(Templates.order(history))).getOrElse(Response.NotFound())
    case ("GET", "/scheduled", _) => Response(Templates.scheduled(Database.getScheduledOrders()))

    case ("GET", "/ingest/csv", _) => Response(Templates.uploadCSV())
    case ("POST", "/ingest/csv", r) =>
      r.form.expect("csv") { (csv: File) =>
        val orders = CSVParser.parse(
          csv,
          java.nio.charset.Charset.defaultCharset(),
          CSVFormat.DEFAULT.builder().setHeader().get()
        ).asScala.map(Order.fromCSV)
        val ids = orders.map(Database.saveOrder).toSeq
        Response(Templates.uploadCSV(Some(ids)))
      }.getOrElse(Response.BadRequest())

    case ("GET", "/ingest/webhook", _) => Response(Templates.testWebhook())
    case ("POST", "/ingest/webhook", r) =>
      Try(Json.parse(r.body).as[WebhookOrder])
        .map(Order.fromWebhook)
        .map(o => {println(o); o})
        .map(Database.saveOrder)
        .fold(err => Response.BadRequest(err.toString), Response.apply)
    
    case ("GET", "/ingest/poll", _) => Response(Templates.pollAPI())
    case ("POST", "/ingest/poll", _) =>
      val apiResponse = Try(Json.parse(APIStub.get(lastPoll)).as[APIResponse])
      val orders = apiResponse match {
        case Success(APIResponse(200, Some(apiOrders), None)) =>
          lastPoll = LocalDateTime.now()
          apiOrders.map(Order.fromExternalAPI.tupled)
        case _ => Seq()
      }

      val ids = orders.map(Database.saveOrder)
      Response.json(ids)
    
    case ("GET", "/sendOrders", _) =>
      val orders = Database.getScheduledOrders().map(_.copy(updated = LocalDateTime.now(), dispatched = true))
      if (orders.nonEmpty) {
        orders.foreach(Database.saveOrder) // Update orders in the database
      }
      Response(Templates.sendOrders(orders.map(RobotOrder.fromOrder)))
    
    case ("GET", "/api/orders", _) => Response(Json.stringify(Json.toJson(Database.getOrders())), Map("Content-Type" -> Seq("application/json")))
    case ("GET", s"/api/order/$id", _) => Response(Json.stringify(Json.toJson(Database.getOrder(id))), Map("Content-Type" -> Seq("application/json")))
  }
}
