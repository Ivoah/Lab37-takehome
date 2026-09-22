import scala.io.Source
import java.io.File
import scala.util.Try
import java.time.LocalDateTime

object APIStub {
  private val orders = Source.fromFile(File("reference/api_responses.jsonl")).getLines()

  def get(time_since: LocalDateTime): String = Try(orders.next()).getOrElse("""{"response": 500, "error": "Internal server error"}""")
}
