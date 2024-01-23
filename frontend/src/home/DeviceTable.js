import React, { useState, useEffect } from 'react';
import UpdateDeviceForm from './UpdateDeviceForm';
import DeleteDeviceConfirmation from './DeleteDeviceConfirmation';
import AddDeviceForm from './AddDeviceForm';
import axios from 'axios';

const DeviceTable = () => {
  const [devices, setDevices] = useState([]);
  const [showUpdateForm, setShowUpdateForm] = useState(false);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [deletingDeviceId, setDeletingDeviceId] = useState(null);
  const [showAddDeviceForm, setShowAddDeviceForm] = useState(false);


  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const token = localStorage.getItem('token');
        const headers = {
          Authorization: `Bearer ${token}`,
        };
        const response = await axios.get('http://localhost:8082/api/v1/devices',{ headers });
        setDevices(response.data);
      } catch (error) {
        console.error('Error fetching devices:', error);
      }
    };

    fetchDevices();
  }, []);

  const handleUpdate = (deviceId) => {
    setShowUpdateForm(true);
    setSelectedDevice(deviceId);
  };

  const handleUpdateFormClose = () => {
    setShowUpdateForm(false);
    setSelectedDevice(null);
  };

  const handleDelete = (deviceId) => {
    setDeletingDeviceId(deviceId);
    setShowDeleteConfirmation(true);
  };

  const handleDeleteConfirmationClose = () => {
    setShowDeleteConfirmation(false);
    setDeletingDeviceId(null);
  };

  const handleDeleteConfirmation = (deviceId) => {
    setDevices(devices.filter((device) => device.id !== deviceId));
  };

  const handleAddNewDevice = () => {
    setShowAddDeviceForm(true);
  };

  const handleAddDeviceFormClose = () => {
    setShowAddDeviceForm(false);
  };

  const handleAddNewDeviceSuccess = async () => {
    try {
      const response = await fetch('http://localhost:8082/api/v1/devices');
      if (response.ok) {
        const data = await response.json();
        setDevices(data); 
      } else {
        console.error('Failed to retrieve devices after adding a new device');
      }
    } catch (error) {
      console.error('Error fetching devices after adding a new device:', error);
    }
  
    setShowAddDeviceForm(false); 
  };

  return (
    <div>
      <h2>Device Management</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Address</th>
            <th>Max Hourly Energy Consumption</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((device) => (
            <tr key={device.id}>
              <td>{device.id}</td>
              <td>{device.name}</td>
              <td>{device.address}</td>
              <td>{device.maximumHourlyEnergyConsumption}</td>
              <td>
                <button onClick={() => handleUpdate(device.id)}>Update</button>
                <button onClick={() => handleDelete(device.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button onClick={handleAddNewDevice}>Add New Device</button>

      {showUpdateForm && selectedDevice && (
        <UpdateDeviceForm deviceId={selectedDevice} onClose={handleUpdateFormClose} />
      )}

    {showDeleteConfirmation && deletingDeviceId && (
        <DeleteDeviceConfirmation
          deviceId={deletingDeviceId}
          onDelete={handleDeleteConfirmation}
          onClose={handleDeleteConfirmationClose}
        />
      )}

      {showAddDeviceForm && (
        <AddDeviceForm onAdd={handleAddNewDeviceSuccess} onClose={handleAddDeviceFormClose} />
      )}
    </div>
  );
};

export default DeviceTable;
