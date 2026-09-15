import play.api.libs.json.*
import scala.util.Try

// {"response": 200, "data": {"7be39c2e042b576a218ea85f126f9ff98f4cea4a5655f03ca402430a0bc4f68f": {"order": 4645, "name": "Grilled cheese with tomato soup", "category": "Lunch & Dinner", "price": 12.74, "status": "ordered"}, "3c3458906cb4060d20a64ae1bf8045a86a5a583aeb3365d389a86aa55aa8b219": {"order": 4646, "name": "Mashed potatoes and gravy", "category": "Side", "price": 34.73, "status": "ordered"}, "3bc7e23af63736af1c34a0a54bee141f9e5262214eeeed89b045fe615c086c3b": {"order": 4646, "name": "Ham and cheese croissant", "category": "Toasts & Light Bites", "price": 47.01, "status": "ordered"}, "cf02c20112452304243295060ee3fcfd1f39ab8be1a774dfe0e0a02ea983011c": {"order": 4646, "name": "Corn on the cob", "category": "Side", "price": 23.72, "status": "ordered"}, "676c97976594dea675d57ad5531c053b8b767ae644e542008eeb882b54e8efae": {"order": 4646, "name": "Western omelet", "category": "Breakfast (All Day)", "price": 37.39, "status": "ordered"}, "f3d2b60194877c68e78351bf4c84dd88775571b51174ecd2e7b592f84dd1c4ba": {"order": 4646, "name": "Egg rolls (2)", "category": "Appetizers", "price": 35.61, "status": "ordered"}, "3147f5186266a88005bdaff3b880c892b53c9724538daf65b76cc1b8d65eac7a": {"order": 4646, "name": "Lemon meringue pie", "category": "Pie & Desserts", "price": 24.19, "status": "ordered"}, "9286ab6cce6b53e26705674e5bf114a0f79af10639b32290996469db94e08030": {"order": 4647, "name": "Mashed potatoes and gravy", "category": "Side", "price": 49.89, "status": "ordered"}, "d7c8dcdde3de1478e71ddd2b127a923f3af81b77a18bd61935e660fc3f9f3300": {"order": 4647, "name": "Sesame balls", "category": "Desserts", "price": 13.45, "status": "ordered"}, "077077aac9808efbdad20899813112dfde82f41d119253ad557ec2c6b3432191": {"order": 4647, "name": "Club sandwich with fries", "category": "Lunch & Dinner", "price": 44.15, "status": "ordered"}, "64d83ac6bd6b4eed876af59d403c10106b922182acf7c9f661b67bed981db470": {"order": 4647, "name": "Cappuccino", "category": "Coffee & Tea", "price": 23.26, "status": "ordered"}, "8bce351d185350e137e243e71b54fc7e74cfaf184d7c70db821a24d7742432e3": {"order": 4647, "name": "Avocado toast", "category": "Toasts & Light Bites", "price": 43.03, "status": "ordered"}}}

enum OrderStatus {
  case ordered
  case processing
  case with_courier
  case delivered
}
given Format[OrderStatus] {
  def reads(json: JsValue): JsResult[OrderStatus] = JsResult.fromTry(Try(OrderStatus.valueOf(json.as[String])))
  def writes(o: OrderStatus): JsValue = Json.toJson(o.toString)
}

case class APIOrder(
  orderId: String,
  orderNumber: Int,
  name: String,
  category: String,
  price: Double,
  status: OrderStatus
)
