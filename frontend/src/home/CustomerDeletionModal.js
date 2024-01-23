import React from 'react';
import axios from 'axios';

const CustomerDeletionModal = ({ customerId, show, close }) => {
  const handleDelete = async () => {
    try {
      const token = localStorage.getItem('token');
      const headers = {
        Authorization: `Bearer ${token}`,
      };

      await axios.delete(`http://localhost:8081/api/v1/customers/${customerId}`, { headers });
      close(); 
    } catch (error) {
      console.error('Failed to delete customer:', error);
    }
  };

  return (
    show && (
      <div className="modal">
        <div className="modal-content">
          <h3>Are you sure you want to delete this customer?</h3>
          <button onClick={handleDelete}>Yes</button>
          <button onClick={close}>No</button>
        </div>
      </div>
    )
  );
};

export default CustomerDeletionModal;
