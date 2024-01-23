import React, { useEffect, useState } from 'react';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

const ChatPage = () => {
  const [privateChats, setPrivateChats] = useState(new Map());
  const [publicChats, setPublicChats] = useState([]);
  const [isTyping, setIsTyping] = useState(false);
  const [tab, setTab] = useState("CHATROOM");
  const [userData, setUserData] = useState({
    username: '',
    receivername: '',
    connected: false,
    message: ''
  });
  const [typingUsers, setTypingUsers] = useState([]);

  useEffect(() => {
    console.log(userData);
  }, [userData]);

  const connect = () => {
    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    setUserData({ ...userData, "connected": true });
    stompClient.subscribe('/chatroom/public', onMessageReceived);
    stompClient.subscribe('/user/' + userData.username + '/private', onPrivateMessage);
    stompClient.subscribe('/chatroom/public/typing', onMessageReceived); // Subscribe to public typing topic
    stompClient.subscribe('/user/' + userData.username + '/private/typing', onMessageReceived); // Subscribe to private typing topic
    userJoin();
  };

  const userJoin = () => {
    var chatMessage = {
      senderName: userData.username,
      status: "JOIN"
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  };

  const handleTyping = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, "message": value });

    const typingMessage = {
      senderName: userData.username,
      status: value ? "TYPING" : "STOP_TYPING"
    };

    stompClient.send("/app/typing", {}, JSON.stringify(typingMessage));
  };

  const onMessageReceived = (payload) => {
    var payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
      case "JOIN":
        if (!privateChats.get(payloadData.senderName)) {
          privateChats.set(payloadData.senderName, []);
          setPrivateChats(new Map(privateChats));
        }
        break;
      case "MESSAGE":
        publicChats.push(payloadData);
        setPublicChats([...publicChats]);
        break;
      case "TYPING":
        if (payloadData.senderName !== userData.username) {
          setTypingUsers((prevTypingUsers) => {
            if (!prevTypingUsers.includes(payloadData.senderName)) {
              return [...prevTypingUsers, payloadData.senderName];
            }
            return prevTypingUsers;
          });
          setTimeout(() => {
            const idx = typingUsers.indexOf(userData.username);
            setTypingUsers(typingUsers.splice(idx, 1));
          }, 3000);
        }
        break;
      case "SEEN":
        const chat = privateChats.get(payloadData.receiverName);
        
        console.log('chat:',chat);
        if(!chat){
          return;
        }
        console.log('chat:',chat);
        for(let idx = 0; idx < chat.length; idx++){
          console.log(chat[idx]);
          chat[idx].seen = true;
        }
      case "STOP_TYPING":
        setTypingUsers((prevTypingUsers) =>
          prevTypingUsers.filter((user) => user !== payloadData.senderName)
        );
        break;
    }
  };

  const onPrivateMessage = (payload) => {
    console.log(payload);
    var payloadData = JSON.parse(payload.body);
    if (privateChats.get(payloadData.senderName)) {
      privateChats.get(payloadData.senderName).push(payloadData);
      setPrivateChats(new Map(privateChats));

    } else {
      let list = [];
      list.push(payloadData);
      privateChats.set(payloadData.senderName, list);
      setPrivateChats(new Map(privateChats));
    }
  };

  const onError = (err) => {
    console.log(err);
  };

  const handleMessage = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, "message": value });
  };

  const sendValue = () => {
    setIsTyping(false);
    if (stompClient) {
      var chatMessage = {
        senderName: userData.username,
        message: userData.message,
        status: "MESSAGE",
        seen: false
      };
      console.log(chatMessage);
      stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, "message": "" });
    }
  };

  const sendPrivateValue = () => {
    setIsTyping(false);
    if (stompClient) {
      var chatMessage = {
        senderName: userData.username,
        receiverName: tab,
        message: userData.message,
        status: "MESSAGE",
        seen: false 
      };

      if (userData.username !== tab) {
        privateChats.get(tab).push(chatMessage);
        setPrivateChats(new Map(privateChats));
      }
      stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
      setUserData({ ...userData, "message": "" });
    }
  };

  const handleUsername = (event) => {
    const { value } = event.target;
    setUserData({ ...userData, "username": value });
  };

  const registerUser = () => {
    connect();
  };

  const sendSeenStatus = (message) => {
    if (stompClient) {
        stompClient.send("/app/seen", {}, JSON.stringify(message));
    }
};

  return (
    <div className="container">
      {userData.connected ? (
        <div className="chat-box">
          <div className="member-list">
            <ul>
              <li
                onClick={() => {
                  setTab('CHATROOM');
                }}
                className={`member ${tab === 'CHATROOM' && 'active'}`}
              >
                Chatroom
              </li>
              {[...privateChats.keys()].map((name, index) => (
                <li
                  onClick={() => {
                    setTab(name);
                    const message = { 
                      senderName: userData.username,
                      receiverName: name,
                      message: "",
                      status: "SEEN",
                      seen: true }
                    sendSeenStatus(message);
                  }}
                  className={`member ${tab === name && 'active'}`}
                  key={index}
                >
                  {name}
                </li>
              ))}
            </ul>
          </div>
          {tab === 'CHATROOM' && (
            <div className="chat-content">
              <ul className="chat-messages">
                {publicChats.map((chat, index) => (
                  <li
                    className={`message ${chat.senderName === userData.username && 'self'}`}
                    key={index}
                  >
                    {chat.senderName !== userData.username && (
                      <div className="avatar">{chat.senderName}</div>
                    )}
                    <div className="message-data">{chat.message}</div>
                    {chat.senderName === userData.username && chat.seen && (
                      <div className="seen-text">Seen</div>
                    )}
                    {chat.senderName === userData.username && (
                      <div className="avatar self">{chat.senderName}</div>
                    )}
                  </li>
                ))}
              </ul>

              <div className="send-message">
                <input
                  type="text"
                  className="input-message"
                  placeholder="enter the message"
                  value={userData.message}
                  onChange={(event) => {
                    handleMessage(event);
                    setIsTyping(true);
                    handleTyping(event); // Notify typing status
                  }}
                />
                <button type="button" className="send-button" onClick={sendValue}>
                  send
                </button>
              </div>
            </div>
          )}
          {tab !== 'CHATROOM' && (
            <div className="chat-content">
              <ul className="chat-messages">
                {[...privateChats.get(tab)].map((chat, index) => (
                  <li
                    className={`message ${chat.senderName === userData.username && 'self'}`}
                    key={index}
                  >
                    {chat.senderName !== userData.username && (
                      <div className="avatar">{chat.senderName}</div>
                    )}
                    <div className="message-data">{chat.message}</div>
                    {chat.senderName === userData.username && chat.seen && (
                      <div className="seen-text">Seen</div>
                    )}
                    {chat.senderName === userData.username && (
                      <div className="avatar self">{chat.senderName}</div>
                    )}
                  </li>
                ))}
              </ul>

              <div className="send-message">
                <input
                  type="text"
                  className="input-message"
                  placeholder="enter the message"
                  value={userData.message}
                  onChange={(event) => {
                    handleMessage(event);
                    setIsTyping(true);
                    handleTyping(event); // Notify typing status
                  }}
                />
                <button type="button" className="send-button" onClick={sendPrivateValue}>
                  send
                </button>
              </div>
            </div>
          )}
          {typingUsers.length > 0 && (
            <div style={{ color: 'gray', fontSize: '0.8em' }}>
              {typingUsers.length === 1
                ? `${typingUsers[0]} is typing...`
                : 'Several people are typing...'}
            </div>
          )}
        </div>
      ) : (
        <div className="register">
          <input
            id="user-name"
            placeholder="Enter your name"
            name="userName"
            value={userData.username}
            onChange={handleUsername}
            margin="normal"
          />
          <button type="button" onClick={registerUser}>
            connect
          </button>
        </div>
      )}
    </div>
  );
};

export default ChatPage;
