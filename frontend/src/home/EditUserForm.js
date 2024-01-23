import React, { useState } from 'react';
import axios from 'axios';

const EditUserForm = ({ customerId, closeEditForm }) => {
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleEditFormSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem('token');
      const headers = {
        Authorization: `Bearer ${token}`,
      };

      const response = await axios.put(
        `http://localhost:8081/api/v1/customers/${customerId}`,
        { email, username, password },
        { headers }
      );

      if (response.status === 200) {
        closeEditForm();
      } else {
        console.error('Failed to update user');
      }
    } catch (error) {
      console.error('Error updating user:', error);
    }
  };

  return (
    <form onSubmit={handleEditFormSubmit}>
      <label htmlFor="email">Email</label>
      <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

      <label htmlFor="username">Username</label>
      <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required />

      <label htmlFor="password">Password</label>
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />

      <button type="submit">Submit</button>
    </form>
  );
};

export default EditUserForm;
