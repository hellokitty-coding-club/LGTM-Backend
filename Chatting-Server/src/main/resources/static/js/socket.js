(function (document) {
    'use strict';

    var chatApplication = {
        stompClient: null,
        username: null,
        roomId: new URL(location.href).searchParams.get('roomId'),
        colors: [
            '#2196F3', '#32c787', '#00BCD4', '#ff5652',
            '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
        ],
        init: function () {
            this.cacheDOM();
            this.bindEvents();
            this.addScript();
        },
        cacheDOM: function () {
            this.usernamePage = document.querySelector('#username-page');
            this.chatPage = document.querySelector('#chat-page');
            this.usernameForm = document.querySelector('#usernameForm');
            this.messageForm = document.querySelector('#messageForm');
            this.messageInput = document.querySelector('#message');
            this.messageArea = document.querySelector('#messageArea');
            this.connectingElement = document.querySelector('.connecting');
        },
        bindEvents: function () {
            this.usernameForm.addEventListener('submit', this.connect.bind(this), true);
            this.messageForm.addEventListener('submit', this.sendMessage.bind(this), true);
        },
        addScript: function () {
            document.write("<script\n" +
                "  src=\"https://code.jquery.com/jquery-3.6.1.min.js\"\n" +
                "  integrity=\"sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ=\"\n" +
                "  crossorigin=\"anonymous\"></script>");
        },
        connect: function (event) {
            this.username = document.querySelector('#name').value.trim();
            this.isDuplicateName();
            this.usernamePage.classList.add('hidden');
            this.chatPage.classList.remove('hidden');

            var socket = new SockJS('/ws-stomp');
            this.stompClient = Stomp.over(socket);

            this.stompClient.connect({}, this.onConnected.bind(this), this.onError.bind(this));

            event.preventDefault();
        },
        onConnected: function () {
            this.stompClient.subscribe('/sub/chatroom/detail/' + this.roomId, this.onMessageReceived.bind(this));

            this.stompClient.send("/pub/chat/enterUser", {},
                JSON.stringify({
                    "roomId": this.roomId,
                    sender: this.username,
                    type: 'ENTER'
                })
            );

            this.connectingElement.classList.add('hidden');
        },
        isDuplicateName: function () {
            $.ajax({
                type: "GET",
                url: "/chat/duplicateName",
                data: {
                    "username": this.username,
                    "roomId": this.roomId
                },
                success: function (data) {
                    console.log("함수 동작 확인 : " + data);
                    this.username = data;
                }.bind(this)
            });
        },
        getUserList: function () {
            const $list = $("#list");

            $.ajax({
                type: "GET",
                url: "/chat/userlist",
                data: {
                    "roomId": this.roomId
                },
                success: function (data) {
                    var users = "";
                    for (let i = 0; i < data.length; i++) {
                        users += "<li class='dropdown-item'>" + data[i] + "</li>"
                    }
                    $list.html(users);
                }
            })
        },
        onError: function (error) {
            this.connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
            this.connectingElement.style.color = 'red';
        },
        sendMessage: function (event) {
            var messageContent = this.messageInput.value.trim();

            if (messageContent && this.stompClient) {
                var chatMessage = {
                    "roomId": this.roomId,
                    sender: this.username,
                    message: this.messageInput.value,
                    type: 'TALK'
                };

                this.stompClient.send("/pub/chat/sendMessage", {}, JSON.stringify(chatMessage));
                this.messageInput.value = '';
            }
            event.preventDefault();
        },
        onMessageReceived: function (payload) {
            var chat = JSON.parse(payload.body);

            var messageElement = document.createElement('li');

            if (chat.type === 'ENTER') {
                messageElement.classList.add('event-message');
                chat.content = chat.sender + chat.message;
                this.getUserList();

            } else if (chat.type === 'LEAVE') {
                messageElement.classList.add('event-message');
                chat.content = chat.sender + chat.message;
                this.getUserList();

            } else {
                messageElement.classList.add('chat-message');

                var avatarElement = document.createElement('i');
                var avatarText = document.createTextNode(chat.sender[0]);
                avatarElement.appendChild(avatarText);
                avatarElement.style['background-color'] = this.getAvatarColor(chat.sender);

                messageElement.appendChild(avatarElement);

                var usernameElement = document.createElement('span');
                var usernameText = document.createTextNode(chat.sender);
                usernameElement.appendChild(usernameText);
                messageElement.appendChild(usernameElement);
            }

            var textElement = document.createElement('p');
            var messageText = document.createTextNode(chat.message);
            textElement.appendChild(messageText);

            messageElement.appendChild(textElement);

            this.messageArea.appendChild(messageElement);
            this.messageArea.scrollTop = this.messageArea.scrollHeight;
        },
        getAvatarColor: function (messageSender) {
            var hash = 0;
            for (var i = 0; i < messageSender.length; i++) {
                hash = 31 * hash + messageSender.charCodeAt(i);
            }

            var index = Math.abs(hash % this.colors.length);
            return this.colors[index];
        },
    };

    chatApplication.init();

})(document);
