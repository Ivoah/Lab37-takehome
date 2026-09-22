import play.api.libs.json.*

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
