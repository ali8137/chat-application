'use strict';

const usernamePage = document.querySelector('#username-page');
const chatPage = document.querySelector('#chat-page');
const usernameForm = document.querySelector('#usernameForm');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectingElement = document.querySelector('.connecting');
const chatArea = document.querySelector('#chat-messages');
const logout = document.querySelector('#logout');

let stompClient = null;
let nickname = null;
let fullname = null;
let selectedUserId = null;

//the below method is invoked when the user clicks on the submit button in the form
function connect(event) {
    nickname = document.querySelector('#nickname').value.trim();
    fullname = document.querySelector('#fullname').value.trim();

    if (nickname && fullname) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    event.preventDefault();
}

// the below method is invoked when the stompClient successfully connects
// to the websocket server. it will subscribe the newly connected user to websocket endpoints
function onConnected() {
    stompClient.subscribe(`/user/${nickname}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/topic/public`, onMessageReceived2);
    stompClient.send("/app/user.addUser",
        {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'ONLINE'})
    )

    document.querySelector('#connected-user-fullname').textContent = fullname;

    findAndDisplayConnectedUsers().then();
}

// this method will fetch the connected users from the database and will
// display them as a list in the HTML element with the id
// "connectedUsers", which is the list of all the users in the chat, in the front side
async function findAndDisplayConnectedUsers() {
    const connectedUserResponse = await fetch('/users');
    let connectedUsers = await connectedUserResponse.json();
    connectedUsers = connectedUsers.filter(user => user.nickName !== nickname);

    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
        if (connectedUsers.indexOf(user) < connectedUsers.length - 1)
        {
            const separator = document.createElement('li');
            separator.classList.add('separator');
            connectedUsersList.appendChild(separator);
        }
    });
}

// this function will append the information of each user like his name, his
// image and his recent messages to the HTML element with the id
// "connectedUsers", which is the list of all the users in the chat
function appendUserElement (user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.nickName;
    const userImage = document.createElement('img');
    userImage.src = '/img/user_icon.png';
    userImage.alt = user.fullName;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.fullName;

    const receivedMsgs = document.createElement('span');
    receivedMsgs.textContent = '0';
    receivedMsgs.classList.add('nbr-msg', 'hidden');

    listItem.appendChild(userImage);
    listItem.appendChild(usernameSpan);
    listItem.appendChild(receivedMsgs);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

// this function will be triggered when the user clicks on an online user. when a
// client click on an online user, the chat history between this client and the user
// will be displayed and the client will be able to write his new messages in the
// input box for writing messages
function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    messageForm.classList.remove('hidden');

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');

    fetchAndDisplayUserChat().then();

    const nbrMsg = clickedUser.querySelector('.nbr-msg');
    nbrMsg.classList.remove('hidden');
}

//this function will display the chat history of the selected user. when the client click on an online user, the chat history between this client and the user will be displayed
async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${nickname}/${selectedUserId}`);

    console.log("nickname: ", nickname);
    console.log("selectedUserId: ", selectedUserId);

    const userChat = await userChatResponse.json();
    console.log("userChat: ", userChat);
    chatArea.innerHTML = '';

    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);

        console.log("chat senderId: ", chat.senderId);
        console.log("chat content: ", chat.content);
    });

    chatArea.scrollTop = chatArea.scrollHeight;
}

//the below function is called to display the new messages sent by or into the user
function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');

    if (senderId === nickname) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }

    const message = document.createElement('p');
    message.textContent = content;

    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

//the below function will be called when the user writes his message and wants to send it to another user
function sendMessage(event) {
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: nickname,
            recipientId: selectedUserId,
            content: messageContent,
            timestamp: new Date()
        };

        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));

        displayMessage(nickname, messageContent);

        messageInput.value = '';
    }

    chatArea.scrollTop = chatArea.scrollHeight;

    event.preventDefault();
}

//this method is called when the stomp client fails to connect to the websocket server
function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

// the function "onMessageReceived" is a callback function for the method subscribe() up there. that's why this method takes as parameter "payload" which is the payload of the message that is received
//the below method is triggered when the a new message is sent to the endpoints the user is subscribing to. it will display the new messages to the chat area. it will also re-call the function "findAndDisplayConnectedUsers" whenever a new user subscribes to the endpoint "topic/public"
async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    const message = JSON.parse(payload.body);

    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }

    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

//i added the below method , just to debug some issue nothing more. you can delete it if you want
// the function "onMessageReceived2" is a callback function for the method subscribe() up there. that's why this method takes as parameter "payload" which is the payload of the message that is received
//the below method is triggered when the a new message is sent to the endpoints the user is subscribing to. it will display the new messages to the chat area. it will also re-call the function "findAndDisplayConnectedUsers" whenever a new user subscribes to the endpoint "topic/public"
async function onMessageReceived2(payload) {
    await findAndDisplayConnectedUsers();
    const message = JSON.parse(payload.body);

    if (selectedUserId && selectedUserId === message.senderId) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }

    if (selectedUserId) {
        document.querySelector(`#${selectedUserId}`).classList.add('active');
    } else {
        messageForm.classList.add('hidden');
    }


    const notifiedUser = document.querySelector(`#${message.senderId}`);
    if (notifiedUser && !notifiedUser.classList.contains('active')) {
        const nbrMsg = notifiedUser.querySelector('.nbr-msg');
        nbrMsg.classList.remove('hidden');
        nbrMsg.textContent = '';
    }
}

function onLogout() {
    stompClient.send('/app/user.disconnectUser', {},
        JSON.stringify({nickName: nickname, fullName: fullname, status: 'OFFLINE'})
        );

    window.location.reload();
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
logout.addEventListener('click', onLogout, true);
window.onbeforeunload = () => onLogout();


//// the below block still needs setting uo the css class for the
//// notification. this method must be called when
//// the user send a message to another user
//function showNotification(message) {
//    const notification = document.createElement('div');
//    notification.classList.add('notification');
//    notification.textContent = `New message from ${message.senderId}: ${message.content}`;
//    document.body.appendChild(notification);
//
//    // Remove the notification after a few seconds
//    setTimeout(() => {
//        notification.remove();
//    }, 5000);
//}