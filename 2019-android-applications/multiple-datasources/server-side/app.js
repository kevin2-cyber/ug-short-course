const app = require("express")();
const morgan = require("morgan");
const mysql = require("mysql");
const Flatted = require("flatted");

// Middlewares
app.use(morgan("dev"));

// MySQL Connection
var connection = mysql.createConnection({
  host: "localhost",
  password: "",
  database: "pied_piper",
  user: "root"
});

// Connect to database
connection.connect(err => {
  if (err) {
    return console.log(err.message);
  } else {
    console.log("Connected to the pied piper database successfully");
  }
});

app.get("/auth", (req, res) => {
  console.log(req.query);

  var id = req.query.id;
  var name = req.query.name;
  var avatar = req.query.avatar || "";
  var timestamp = new Date().getTime();

  connection.query(
    "select * from users where name = ?",
    [name],
    (err, results, fields) => {
      if (err) {
        return res.status(404).send(null);
      } else {
        if (results[0] == null) {
          connection.query(
            "insert into users values(?,?,?,?)",
            [id, name, avatar, timestamp],
            (err, results, fields) => {
              if (err) {
                return res.status(201).send(null);
              } else {
                return res.status(201).send({
                  id,
                  name,
                  avatar,
                  timestamp
                });
              }
            }
          );
        } else {
          return res.status(201).send(results[0]);
        }
      }
    }
  );
});

// Login route
app.post("/auth", (req, res) => {
  // if (req.body) {
  // var phone = req.body.phone;
  var id = req.body.id;
  var name = req.body.name;
  var avatar = req.body.avatar;

  connection.query(
    "insert into users values(?,?,?,?)",
    [id, name, avtar, new Date().getTime()],
    (err, results, fields) => {
      if (err) {
        return;
      } else {
        return res.status(201).send({
          id,
          name,
          avatar,
          timestamp: new Date().getTime()
        });
      }
    }
  );
  // } else {
  // res.status(401).send(null);
  // }
});

// Get all users
app.get("/users", (req, res) => {
  connection.query(
    "select * from users order by id desc",
    [],
    (err, results, fields) => {
      if (err) {
        return [];
      } else {
        return res.status(200).send(results);
      }
    }
  );
});

// Get user by id
app.post("/users/:id", (req, res) => {
  // Get the user's id form the request
  var id = req.params.id;

  if (id) {
    connection.query(
      "select * from users where id = ? limit 1",
      [id],
      (err, rows, fields) => {
        if (rows[0] == null) {
          return res.status(404).send(null);
        }
        return res.status(200).send(rows[0]);
      }
    );
  } else {
    return res.status(404).send({
      message: "Your request is invalid"
    });
  }
});

// Add multiple users
app.post("/users/new/multi", (req, res) => {
  if (req.body) {
    var query = "insert into user values(?,?,?,?)";
    req.body.forEach(user => {
      var params = [user.id, user.name, user.avatar, new Date().getTime()];
      connection.query(query, params, (err, rows, fields) => {
        if (err) {
          return res.status(400).send({
            message: err.message
          });
        }
        return res.status(200).send({
          message: "Fields inserted successfully"
        });
      });
    });
  }
});

// Add chat
app.get("/chats/new", (req, res) => {
  if (req.query) {
    var chat = req.query;
    var query = "insert into chat values(?,?,?,?,?)";
    connection.query(
      query,
      [
        chat.id,
        chat.sender,
        chat.recipient,
        chat.message,
        new Date().getTime()
      ],
      (err, rows, fields) => {
        if (err) {
          return res.status(400).send({
            message: err.message
          });
        }
        return res.status(200).send({
          message: "Fields inserted successfully"
        });
      }
    );
  } else {
    return res.status(404).send({
      message: "Chat could not be added"
    });
  }
});

// Add chat
app.get("/chats/new", (req, res) => {
  console.log(req.query);
  
  if (req.query) {
    var chat = req.query;
    var query = "insert into chat values(?,?,?,?,?)";
    connection.query(
      query,
      [
        chat.id,
        chat.sender,
        chat.recipient,
        chat.message,
        new Date().getTime()
      ],
      (err, rows, fields) => {
        if (err) {
          return res.status(400).send({
            message: err.message
          });
        }
        return res.status(200).send({
          message: "Fields inserted successfully"
        });
      }
    );
  } else {
    return res.status(404).send({
      message: "Chat could not be added"
    });
  }
});

// Add chat
app.get("/chats", (req, res) => {
  if (req.query) {
    var chat = req.query;
    var query = "select * from chat where sender = ? and recipient = ?";
    connection.query(
      query,
      [chat.sender, chat.recipient],
      (err, rows, fields) => {
        if (err) {
          return res.status(400).send([]);
        }
        return res.status(200).send(rows);
      }
    );
  } else return res.send([]);
});

app.delete("/chats/:id", (req, res) => {
  var id = req.params.id;

  connection.query(
    "delete from chat where id = ?",
    [id],
    (err, rows, fields) => {
      if (err) {
        return res.status(400).send({
          message: `Unable to delete your chat with id: ${id}`
        });
      }
      return res.send({ message: "Chat delete successfully" });
    }
  );
});

// Export module
module.exports = app;
