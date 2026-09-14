import net.ivoah.vial.*

import java.nio.file.Paths

class Endpoints() {
  def router: Router = Router {
    case ("GET" , "/", r) => Response(Templates(r).root())
    case ("GET", s"/static/$file", _) => Response.forFile(Paths.get("static"), Paths.get(file))
  }
}
