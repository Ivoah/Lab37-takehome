import scalatags.Text.all.*
import scalatags.Text.tags2.title
import net.ivoah.vial.Request

class Templates(request: Request) {
  private def _head(_title: String) = head(
    title(_title),
    link(rel:="stylesheet", href:=s"/static/style.css"),
  )

  private def page(title: String)(content: Frag) = doctype("html")(html(
    head(_head(title)),
    body(content)
  )).render

  def root(): String = page("Root")(
    "Hi there"
  )
}
