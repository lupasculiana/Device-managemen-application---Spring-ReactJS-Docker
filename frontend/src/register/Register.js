import React, { useState } from "react";
import { Navigate } from "react-router-dom";
import axios from 'axios';

export const Register = ({ onFormSwitch, setCurrentUserRole, setCurrentCustomerId }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [username, setUsername] = useState('');
    const[isLoggedIn, setIsLoggedIn] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            console.log(email,username,password);
            const response = await axios.post('http://localhost:8081/api/v1/auth/register', {
                email: email,
                username: username,
                password: password
            });

            console.log("User registered:", response.data);
            const userRole = 'USER'; 
            const customerId = response.data.customerId;
            setCurrentCustomerId(customerId);
            setCurrentUserRole(userRole);
            setIsLoggedIn(true); 
        } catch (error) {
            console.error("Registration failed:", error);
        }
    }

    if(isLoggedIn){
        return <Navigate to="/home" />;
      }else{
    return (
        <div className="auth-form-container">
            <h2>Register</h2>
        <form className="register-form" onSubmit={handleSubmit}>
            <label htmlFor="username">Choose an username</label>
            <input value={username} name="username" onChange={(e) => setUsername(e.target.value)} id="username" placeholder="..."/>
            <label htmlFor="email">email</label>
            <input value={email} onChange={(e) => setEmail(e.target.value)}type="email" placeholder="youremail@gmail.com" id="email" name="email" />
            <label htmlFor="password">password</label>
            <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" placeholder="********" id="password" name="password" />
            <button type="submit">Log In</button>
        </form>
        <button className="link-btn" onClick={() => onFormSwitch('login')}>Already have an account? Login here.</button>
    </div>
    )
      }
}