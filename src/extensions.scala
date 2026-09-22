import play.api.libs.json.*
import java.time.{LocalTime, LocalDateTime}

extension [T](i: Seq[T]) {
  def join(joiner: T): Seq[T] = i.flatMap(Seq(_, joiner)).dropRight(1)
}

extension (f: Format.type) {
  inline def derived[T]: Format[T] = Json.format[T]
}

extension (ldt: LocalDateTime) {
  def nextAtTime(t: LocalTime): LocalDateTime = {
    val atTime = ldt.toLocalDate().atTime(t)
    if (ldt.isAfter(atTime)) atTime.plusDays(1)
    else atTime
  }
}
