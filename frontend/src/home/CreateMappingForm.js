import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CreateMappingForm = () => {
  const [users, setUsers] = useState([]);
  const [devices, setDevices] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [selectedDevice, setSelectedDevice] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const token = localStorage.getItem('token');
        const headers = {
          Authorization: `Bearer ${token}`,
        };
        const response = await axios.get('http://localhost:8081/api/v1/customers', { headers });
        setUsers(response.data);
      } catch (error) {
        console.error('Error fetching users:', error);
      }
    };

    const fetchDevices = async () => {
      try {
        const token = localStorage.getItem('token');
        const headers = {
          Authorization: `Bearer ${token}`,
        };
        const response = await axios.get('http://localhost:8082/api/v1/devices', { headers });
        setDevices(response.data);
      } catch (error) {
        console.error('Error fetching devices:', error);
      }
    };

    fetchUsers();
    fetchDevices();
  }, []);

  const handleCreateMapping = async () => {
    try {
      const token = localStorage.getItem('token');
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const mappingDTO = {
        customerId: selectedUser,
        deviceId: selectedDevice,
      };
      const response = await axios.put('http://localhost:8082/api/v1/devices/mapping', mappingDTO, { headers });
      console.log('Mapping created:', response.data);
    } catch (error) {
      console.error('Error creating mapping:', error);
    }
  };

  return (
    <div>
      <h2>Create Mapping</h2>
      <label htmlFor="users">Select a User:</label>
      <select id="users" onChange={(e) => setSelectedUser(e.target.value)}>
        <option value="">Select a user</option>
        {users.map((user) => (
          <option key={user.id} value={user.id}>{user.username}</option>
        ))}
      </select>
      
      <label htmlFor="devices">Select a Device:</label>
      <select id="devices" onChange={(e) => setSelectedDevice(e.target.value)}>
        <option value="">Select a device</option>
        {devices.map((device) => (
          <option key={device.id} value={device.id}>{device.name}</option>
        ))}
      </select>
      
      <button onClick={handleCreateMapping}>Create new mapping</button>
    </div>
  );
};

export default CreateMappingForm;
