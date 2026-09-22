import scalatags.Text.all.*
import scalatags.Text.tags2.title
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object Templates {
  private val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)

  private val tabs = Seq(
    "Orders" -> "/orders",
    "Upload CSV" -> "/ingest/csv",
    "Test webhook" -> "/ingest/webhook",
    "Test polling API" -> "/ingest/poll"
  )

  private def page(pageName: String)(content: Frag*) = doctype("html")(html(
    head(
      title(pageName),
      link(rel:="stylesheet", href:=s"/static/style.css"),
      script(src:="https://code.jquery.com/jquery-4.0.0.min.js")
    ),
    body(
      h1(pageName),
      div(cls:="tabbar", for ((tabName, address) <- tabs) yield div(cls:="tab",
        a(cls:=(if (tabName == pageName) "underline" else ""), href:=address, tabName)
      )),
      content
    )
  )).render

  def orders(orders: Map[String, Seq[Order]]): String = page("Orders")(
    table(
      thead(tr(th("Order ID"), th("Name"), th("Items"), th("Scheduled"), th("Dispatched"), th("Updates"))),
      tbody(for ((_id, history) <- orders.toSeq; order = history.last) yield tr(id:=_id,
        td(cls:="wrap", a(href:=s"/order/$_id", _id)),
        td(s"${order.firstName} ${order.lastName}"),
        td(order.items),
        td(dateTimeFormatter.format(order.scheduled)),
        td(if (order.dispatched) "✓" else "✗"),
        td(history.length)
      ))
    )
  )

  def order(history: Seq[Order]): String = page("Orders")(
    h1(cls:="wrap", s"Order ID: ${history.last.id}"),
    table(
      thead(tr(th("Name"), th("Items"), th("Notes"), th("Scheduled"), th("Dispatched"), th("Other"))),
      tbody(for (order <- history) yield tr(
        td(s"${order.firstName} ${order.lastName}"),
        td(order.items),
        td(order.notes),
        td(dateTimeFormatter.format(order.scheduled)),
        td(if (order.dispatched) "✓" else "✗"),
        td(order.meta.map{case (k, v) => StringFrag(s"$k: $v")}.toSeq.join(br()))
      ))
    )
  )

  def uploadCSV(idsOpt: Option[Seq[String]] = None): String = page("Upload CSV")(
    form(method:="POST", enctype:="multipart/form-data",
      label("Upload file: ", input(`type`:="file", name:="csv", accept:="text/csv")), br(),
      button("Upload"),
      idsOpt.map(ids => table(ids.map(id => tr(td("Loaded ", a(href:=s"/order/$id", id))))))
    )
  )

  def testWebhook(): String = page("Test webhook")(
    textarea(id:="data"), br(),
    button("Submit", onclick:="""
      for (order of $("#data").val().split("\n")) {
        if (order) $.post("/ingest/webhook", order, id => {
          $("#messages").prepend(`<tr><td>Loaded <a href="/order/${id}">${id}</a></td></tr>`);
        });
      }
    """),
    table(id:="messages")
  )

  def testPoll(): String = page("Test poll")(
    button("Poll", onclick:="""
      $.post("/ingest/poll", "", ids => {
        if (ids.length > 0) {
          $("#messages").empty();
          for (id of ids) {
            $("#messages").append(`<tr><td>Loaded <a href="/order/${id}">${id}</a></td></tr>`);
          }
        } else {
          $("#messages").html("<tr><td>API returned no orders</td></tr>");
        }
      })
    """),
    table(id:="messages")
  )
}
