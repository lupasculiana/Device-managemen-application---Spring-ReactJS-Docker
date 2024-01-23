import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import axios from 'axios';

export const Login = ({ onFormSwitch, setCurrentUserRole, setCurrentCustomerId }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const[isLoggedIn, setIsLoggedIn] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/api/v1/auth/authenticate', {
                username: username,
                password: password
            });

            console.log("User logged in:", response.data);
            const userRole = response.data.role; 
            const customerId = response.data.customerId;
            console.log(userRole);
            setCurrentUserRole(userRole);
            setCurrentCustomerId(customerId);
            localStorage.setItem('token', response.data.token);
            setIsLoggedIn(true);
        } catch (error) {
            console.error("Login failed:", error);
        }
    }

    if(isLoggedIn){
      return <Navigate to="/home" />;
    }else{
    return (
        <div className="auth-form-container">
            <h2>Login</h2>
            <form className="login-form" onSubmit={handleSubmit}>
                <label htmlFor="username">username</label>
                <input value={username} onChange={(e) => setUsername(e.target.value)}type="username" placeholder="username" id="username" name="username" />
                <label htmlFor="password">password</label>
                <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="********" id="password" name="password" />
                <button type="submit">Log In</button>
            </form>
            <button className="link-btn" onClick={() => onFormSwitch('register')}>Don't have an account? Register here.</button>
        </div>
    )
    }
}