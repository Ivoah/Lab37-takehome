import scalatags.Text.all.*
import scalatags.Text.tags2.title
import net.ivoah.vial.Request

class Templates(request: Request) {
  private def _head(_title: String) = head(
    title(_title),
    link(rel:="stylesheet", href:=s"/static/style.css"),
  )

  private def page(title: String)(content: Frag*) = doctype("html")(html(
    head(_head(title)),
    body(content)
  )).render

  def root(): String = page("Root")(
    "Hi there"
  )

  def orders(orders: Map[String, Seq[Order]]): String = page("Orders")(
    table(
        thead(
          tr(th("Order ID"), th("Name"), th("Items"), th("Notes"), th("#Updates")/*, th("Other")*/)
        ),
        for ((_id, history) <- orders.toSeq; order = history.last) yield {
          tr(id:=_id,
            td(a(href:=s"/order/$_id", _id)),
            td(s"${order.firstName} ${order.lastName}"),
            td(order.items),
            td(order.notes),
            td(history.length)
            // td(order.meta.map{case (k, v) => StringFrag(s"$k: $v")}.toSeq.join(br()))
          )
        }
      )
  )

  def order(history: Seq[Order]): String = page("Orders")(
    h1(s"Order ID: ${history.last.id}"),
    table(
        thead(
          tr(th("Name"), th("Items"), th("Notes")/*, th("Other")*/)
        ),
        for (order <- history) yield {
          tr(
            td(s"${order.firstName} ${order.lastName}"),
            td(order.items),
            td(order.notes),
            // td(order.meta.map{case (k, v) => StringFrag(s"$k: $v")}.toSeq.join(br()))
          )
        }
      )
  )
}
