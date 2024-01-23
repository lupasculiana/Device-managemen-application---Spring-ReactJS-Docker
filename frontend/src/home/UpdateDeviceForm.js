import React, { useState } from 'react';

const UpdateDeviceForm = ({ deviceId, onClose }) => {
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');
  const [maxHourlyEnergy, setMaxHourlyEnergy] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();

    const deviceData = {
      id: deviceId,
      name: name,
      address: address,
      maximumHourlyEnergyConsumption: maxHourlyEnergy,
    };

    try {
      const response = await fetch(`http://localhost:8082/api/v1/devices/${deviceId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify(deviceData),
      });

      if (response.ok) {
        onClose(); 
      } else {
        console.error('Failed to update device');
      }
    } catch (error) {
      console.error('Error updating device:', error);
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
      <button type="submit">Submit</button>
    </form>
  );
};

export default UpdateDeviceForm;
