var socket = require('socket.io').listen(17790);
var rooms = [];
var maxPerRoom = 2;
 
socket.on('connection', function(client){

    // search for free rooms
    var currentRoom = -1;
    for(var room in rooms ) {
        if(rooms[room].players.length < maxPerRoom) {
            currentRoom = rooms[room];
        }
    }
    
    // found free room? If not create one with the current client as 'host'
    if(currentRoom === -1) {
        currentRoom = new Room(client.id)
        rooms.push(currentRoom);
    }

    // add own id to current players in room
    count = currentRoom.players.length;
    var player = new Player(client);
    currentRoom.players.push(new Player(client));
    console.log("room:",currentRoom.id, "players:", count);

    // send own id to client
    client.emit('init',{ player: client.id, room: currentRoom.id, gameinprogress: currentRoom.gameInProgress, count: count });
    
    // send active players to connected client and inform others about the new player
    var n = 0;
    for( var player in currentRoom.players ) { 
        if(currentRoom.players[player].client.id !== client.id) {
            client.emit('playerconnect',{ player: currentRoom.players[player].client.id, count: n });
    
            // send client id to other client
            currentRoom.players[player].client.emit('playerconnect',{ player: client.id, count: count });
    
        } 
        n = n + 1;
    }
    
    client.room = currentRoom;
    client.player = player;
    
    
    // player ready message
    client.on('ready', function(message){
        for( var player in client.room.players ) { 
            // set current player to ready
            if(client.room.players[player].client.id === client.id) {
                client.room.players[player].ready = true;
            } else {
                // send everybody else in the room the ready message
                // TODO maybe we don't need this...
                client.room.players[player].client.emit('ready', {player: client.id, message: message});
            }
        }        
        
        // check if all players are ready
        // if so then start a new round
        ready = 0;
        for( var player in client.room.players ) { 
            if(client.room.players[player].ready === true) {
                ready = ready + 1;
            }
        }
        console.log("players ready:", ready, client.room.players.length);
        if(ready === client.room.players.length && ready >= 2 && client.room.gameInProgress === false) {
            // all ready so start a new round
            var cnt = 0
            for( var player in client.room.players ) { 
                client.room.players[player].client.emit('startround',{ player: client.room.players[player].client.id, count: cnt }); 
                cnt++;
            }
            client.room.gameInProgress = true;
        }
    });
    
    // player not ready message
    client.on('notready', function(message){
        for( var player in client.room.players ) { 
            // set current player to ready
            if(client.room.players[player].client.id === client.id) {
                client.room.players[player].ready = false;
            } else {
                // send everybody else in the room the ready message
                // TODO maybe we don't need this...
                client.room.players[player].client.emit('notready', {player: client.id, message: message});
            }
        }        
    });

    // update message
    client.on('update', function(message){
        // broadcast event to other players in the same room
        for( var player in client.room.players ) { 
            if(client.room.players[player].client.id !== client.id) {
                client.room.players[player].client.emit('update', {player: client.id, message: message});
            }
        }
    });
    
    // update message
    client.on('hit', function(message){
        // broadcast event to other players in the same room
        for( var player in client.room.players ) { 
            if(client.room.players[player].client.id !== client.id) {
                client.room.players[player].client.emit('hit', {player: client.id, message: message});
            }
        }
    });
    
    // synchronize message
    client.on('synchronize', function(message){
        // broadcast event to other players in the same room
        for( var player in client.room.players ) { 
            if(client.room.players[player].client.id !== client.id) {
                client.room.players[player].client.volatile.emit('synchronize', {player: client.id, message: message});
            }
        }
    });
    
    // disconnect message
    client.on('disconnect', function(){     
        // broadcast event to other players in the same room
        for( var player in client.room.players ) { 
            if(client.room.players[player].client.id !== client.id) {
                client.room.players[player].client.emit('playerdisconnect',{ player: client.id });
            }
        }

        console.log("players active", client.room.players.length);    
        //remove player from current room
        var n = 0;
        for( var player in client.room.players ) { 
            if(client.room.players[player].client.id === client.id) {
                console.log("found player", client.id);
                break;
            }
            n = n + 1;
        }
        client.room.players.splice(n, 1);
        console.log("players active", client.room.players.length);
    
        // if room is empty then delete room    
        if(client.room.players.length === 0) {
            var n = 0;
            for(var room in rooms ) {
                if(rooms[room].id === client.room.id) {
                    break;
                }
                n = n + 1;
            }
            if(rooms[n].players.length === 0) {
                console.log("delete room", client.id);
                rooms.splice(n, 1);
            }            
        } else if (client.room.players.length === 1) {
          //check if only one player remains...
          for( var player in client.room.players ) { 
                client.room.players[player].client.emit('endRound', {winner: player.id} );                
            }     
        }
    });
});
 
function Player(client, roomId) {
        this.client = client;
        this.roomId = roomId;
        this.points = 0;
        this.ready = false;
};

function Room(id) {
        this.id = id;
        this.gameInProgress = false;
        this.currentRound = 1;
        this.players = [];
};
