import React, { useState } from "react";
import './App.css';
import { Login } from "./login/Login";
import { Register } from "./register/Register";
import Home from "./Home"
import ChatPage from "./chat/ChatPage"; 
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

function App() {
  const [currentForm, setCurrentForm] = useState('login');
  const [currentUserRole, setCurrentUserRole] = useState('user'); 
  const [currentCustomerId, setCurrentCustomerId] = useState(); 

  const toggleForm = (formName) => {
    setCurrentForm(formName);
  }

 const setUserRole = (role) => {
    setCurrentUserRole(role);
  }

  const setCustomerId = (customerId) => {
    setCurrentCustomerId(customerId);
  }

  return (
   
    <Router>
      <Routes>
        <Route path="/" element={ <div className="App">
      {
        currentForm === "login" ? <Login onFormSwitch={toggleForm} setCurrentUserRole={setUserRole} setCurrentCustomerId={setCustomerId} /> : <Register onFormSwitch={toggleForm}
        setCurrentUserRole = {setCurrentUserRole} setCurrentCustomerId={setCustomerId}/>
      }
    </div>} />
    <Route path="/home" element={<Home userRole={currentUserRole} customerId={currentCustomerId} />} />
    <Route path="/chat" element={<ChatPage />} /> 
      </Routes>
    </Router>
  );
}

export default App;