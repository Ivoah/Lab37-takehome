# Noah Rosamilia take home project

## Running

The only requirement for running this project is a working JVM.

`./mill run` will start a server listening on <http://127.0.0.1:3737>. The flags `--host` and `--port` allow you to change what IP/port the server binds to.

## Exploring

The three most important files in this project are [Endpoints.scala](src/Endpoints.scala), [Templates.scala](src/Templates.scala), and [Database.scala](src/Database.scala).
Almost all of the logic of the app is contained in those three files, with the rest being scaffolding or data modeling.
As the name might imply [Endpoints.scala](src/Endpoints.scala) contains all of the endpoints of the app and some of the business logic.
[Templates.scala](src/Templates.scala) contains the frontend HTML that is sent to the client.
All methods in [Templates.scala](src/Templates.scala) are pure functions and contain no business logic.
They only take data in and return HTML to be sent to the client.
[Database.scala](src/Database.scala) is where all database operations live.

## Design decisions

### Scala
I chose Scala primarily because it is the language I am the most fluent in, and I have written many websites in Scala.
The web framework I used for this project is one that I wrote myself for personal websites and have used in a professional project as well: <https://github.com/Ivoah/vial>.
Aside from that though I believe it provides many real advantages for a system like this.
Being based on the JVM makes it easy to run in server environments and very performant. 
Scala's strong type system makes maintainability easier as well.

## AI disclaimer

No AI was used in the creation of this project (with the exception of a few suggestions from Google search's built in AI)
