import React, { useState } from 'react';

const AddDeviceForm = ({ onAdd, onClose }) => {
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [maxHourlyEnergy, setMaxHourlyEnergy] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const deviceData = {
      name: name,
      address: address,
      maximumHourlyEnergyConsumption: maxHourlyEnergy,
    };

    try {
      const response = await fetch('http://localhost:8082/api/v1/devices', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(deviceData),
      });

      if (response.ok) {
        onAdd(); 
        onClose(); 
      } else {
        console.error('Failed to create new device');
      }
    } catch (error) {
      console.error('Error creating new device:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Name:
        <input type="text" value={name} onChange={(e) => setName(e.target.value)} />
      </label>
      <label>
        Address:
        <input type="text" value={address} onChange={(e) => setAddress(e.target.value)} />
      </label>
      <label>
        Max Hourly Energy Consumption:
        <input type="number" value={maxHourlyEnergy} onChange={(e) => setMaxHourlyEnergy(e.target.value)} />
      </label>
      <button type="submit">Create New Device</button>
    </form>
  );
};

export default AddDeviceForm;
