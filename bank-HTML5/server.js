//Create an express server
var express = require('express');
var app = express();
var serv = require('http').Server(app); 

const cors = require("cors");

const corsOptions = {
  origin: "http://127.0.0.1:8080",
};

app.get('/',function(req, res) {
	//This is the main HTML file the serves uses
    res.sendFile(__dirname + '/index.html');
}); 
app.use('/img',express.static(__dirname + '/img'));
app.use(cors(corsOptions));

 
//Set server port, and log "Server started."
serv.listen(2000);
console.log("Server started.");


//Socket handing
const io = require('socket.io')(serv, {
    cors: {
        origin: "http://localhost:2000",
        methods: ["GET", "POST"],
        transports: ['websocket', 'polling'],
        credentials: true
    },
    allowEIO3: true
});

//When a new player joins, creates that sockets connections
io.sockets.on('connection', function(socket){ 
	
    socket.on('sendHTTP',function(data){
		//Login/ register
		if (data.command == "login") {
			login(socket, data.username, data.password);
		}
		if (data.command == "register") {
			register(socket, data.firstname, data.lastname, data.email, data.username, data.password);
		}

		//User Actions
		if (data.command == "checking") {
			addAccount(socket, data.userID, "checking-account");
		}
		if (data.command == "credit") {
			addAccount(socket, data.userID, "credit-account");
		}
		if (data.command == "update") {
			update(socket, data.userID, data.firstname, data.lastname, data.email, data.username, data.password);
		}
		//Account actions
		if (data.command == "deposit") {
			deposit(socket, data.account, data.amount);
		}
		if (data.command == "withdraw") {
			withdraw(socket, data.account, data.amount);
		}
		if (data.command == "transfer") {
			transfer(socket, data.account, data.amount, data.toid);
		}
		if (data.command == "history") {
			getHistory(socket, data.account);
		}
		if (data.command == "delete") {
			deleteAccount(socket,data.account);
		}
		if (data.command == "graph") {
			getGraph(socket, data.account);
		}
		if (data.command == "enter stock") {
			enterStock(socket,data.account, data.amount);
		}
		if (data.command == "exit stock") {
			exitStock(socket,data.account, data.amount);
		}

    });


});


//HTTP request functions

//Login / Register
async function login(socket, username, password) {
	const url = "http://localhost:8080/login";

	postData = {
		username: username,
		password: password
	}

	const requestOptions = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(postData)
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "login", data: json });
	} catch (error) {
		console.error(error.message);
		socket.emit('results', { type: "login-failure", data: "Invalid Credentials or server Error" });
	}
}

async function register(socket, firstname, lastname, email, username, password) {
	const url = "http://localhost:8080/register";

	postData = {
		firstName: firstname,
		lastName: lastname,
		email: email,
		username: username,
		password: password
	}

	const requestOptions = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(postData)
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "login", data: json });
	} catch (error) {
		console.error(error.message);
		socket.emit('results', { type: "register-failure", data: "username/email in use or server Error" });
	}
}

async function update(socket, userID, firstname, lastname, email, username, password) {
	const url = "http://localhost:8080/update";

	postData = {
		userID: userID,
		firstName: firstname,
		lastName: lastname,
		email: email,
		username: username,
		password: password
	}

	const requestOptions = {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(postData)
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "login", data: json });
	} catch (error) {
		console.error(error.message);
		socket.emit('results', { type: "update-failure", data: "username/email in use or server Error" });
	}
}

//User Actions

async function addAccount(socket, userID, type) {
	const url = "http://localhost:8080/" + type + "/" + userID;

	const requestOptions = {
		method: 'POST',
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "account-create", data: json });
	} catch (error) {
		console.error(error.message);
		socket.emit('results', { type: "account-failure", data: "server Error" });
	}
}

//Account actions
async function deposit(socket, account, amount) {

	const url = "http://localhost:8080/account/" + account + "/deposit/" + amount;

	const requestOptions = {
		method: 'POST',
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "balance", data: json });
	} catch (error) {
		console.error(error.message);
	}
}

async function withdraw(socket, account, amount) {
	const url = "http://localhost:8080/account/" + account + "/withdraw/" + amount;

	const requestOptions = {
		method: 'POST',
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "balance", data: json });
	} catch (error) {
		console.error(error.message);
	}
}
async function transfer(socket, account, amount, toid) {
	const url = "http://localhost:8080/account/" + account + "/transfer/" + amount + "/" + toid;

	const requestOptions = {
		method: 'POST',
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "balance", data: json });
	} catch (error) {
		console.error(error.message);
	}
}

async function getHistory(socket, account) {
	const url = "http://localhost:8080/account/" + account + "/history";
	try {
		const response = await fetch(url);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "history", data: json });
	} catch (error) {
		console.error(error.message);
	}
}

async function getGraph(socket, account) {
	const url = "http://localhost:8080/account/" + account + "/graph";
	try {
		const response = await fetch(url);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "graph", data: json });
	} catch (error) {
		console.error(error.message);
	}
}

async function deleteAccount(socket, account) {
	const url = "http://localhost:8080/account/" + account;

	const requestOptions = {
		method: 'DELETE',
	};


	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "delete", data: account });
	} catch (error) {
		console.error(error.message);
	}
}

async function enterStock(socket, account, amount) {

	const url = "http://localhost:8080/stock/" + account + "/" + amount;

	const requestOptions = {
		method: 'POST',
	};

	try {
		const response = await fetch(url, requestOptions);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "balance", data: json });
	} catch (error) {
		console.error(error.message);
	}
}

async function exitStock(socket, account, amount) {

	const url = "http://localhost:8080/stock/" + account + "/" + amount;

	try {
		const response = await fetch(url);
		if (!response.ok) {
			throw new Error(`Response status: ${response.status}`);
		}

		const json = await response.json();
		socket.emit('results', { type: "balance", data: json });
	} catch (error) {
		console.error(error.message);
	}
}