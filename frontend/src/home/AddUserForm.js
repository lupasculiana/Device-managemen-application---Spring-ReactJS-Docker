import React, { useState } from 'react';
import axios from 'axios';

const AddUserForm = ({ updateCustomerList, closeAddUserForm }) => {
  const [newUser, setNewUser] = useState({ username: '', email: '', password: '' });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewUser({ ...newUser, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token'); 
      const headers = {
        Authorization: `Bearer ${token}`,
      };

      await axios.post('http://localhost:8081/api/v1/customers', newUser, { headers });
      await updateCustomerList();
      setNewUser({ username: '', email: '', password: '' });
      closeAddUserForm();
    } catch (error) {
      console.error('Error creating user:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="username">Username:</label>
      <input type="text" name="username" value={newUser.username} onChange={handleChange} />

      <label htmlFor="email">Email:</label>
      <input type="email" name="email" value={newUser.email} onChange={handleChange} />

      <label htmlFor="password">Password:</label>
      <input type="password" name="password" value={newUser.password} onChange={handleChange} />

      <button type="submit">Create new user</button>
    </form>
  );
};

export default AddUserForm;
