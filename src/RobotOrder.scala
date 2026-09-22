import play.api.libs.json.*

case class RobotOrder(id: String, firstName: String, lastName: String, items: String) derives Format
object RobotOrder {
  def fromOrder(o: Order): RobotOrder = RobotOrder(o.id, o.firstName, o.lastName, o.items)
}
