import React from 'react';

const DeleteDeviceConfirmation = ({ deviceId, onDelete, onClose }) => {
  const handleDelete = () => {
    fetch(`http://localhost:8082/api/v1/devices/${deviceId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`,
      },
    })
      .then((response) => {
        if (response.ok) {
          onDelete(deviceId); 
          onClose(); 
        } else {
          console.error('Failed to delete device');
        }
      })
      .catch((error) => {
        console.error('Error deleting device:', error);
      });
  };

  return (
    <div>
      <p>Are you sure you want to delete this device?</p>
      <button onClick={handleDelete}>Yes</button>
      <button onClick={onClose}>No</button>
    </div>
  );
};

export default DeleteDeviceConfirmation;
