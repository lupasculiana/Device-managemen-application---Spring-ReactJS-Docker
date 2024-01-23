import React, { useState, useEffect } from 'react';
import axios from 'axios';
import EditUserForm from './EditUserForm';
import CustomerDeletionModal from './CustomerDeletionModal';
import AddUserForm from './AddUserForm';

const UserTable = () => {
  const [customers, setCustomers] = useState([]);
  const [showEditForm, setShowEditForm] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddUserForm, setShowAddUserForm] = useState(false);
  const [selectedUserId, setSelectedUserId] = useState(null);

  useEffect(() => {
    const fetchCustomers = async () => {
      try {
        const token = localStorage.getItem('token'); 
        const headers = {
        Authorization: `Bearer ${token}`,
        };
        const response = await axios.get('http://localhost:8081/api/v1/customers', { headers });
        if (response.status >= 200 && response.status < 300) {
            const data = response.data;
            setCustomers(data);
          } else {
            console.error('Failed to fetch customers');
          }
      } catch (error) {
        console.error('Error fetching customers:', error);
      }
    };

    fetchCustomers();
  }, []);

  const handleEdit = (userId) => {
    setSelectedUserId(userId);
    setShowEditForm(true);
  };

  const closeEditForm = async () => {
    setShowEditForm(false);
    try {
      const token = localStorage.getItem('token'); 
      const headers = {
      Authorization: `Bearer ${token}`,
      };
      const response = await axios.get('http://localhost:8081/api/v1/customers', { headers });
      if (response.status >= 200 && response.status < 300) {
          const data = response.data;
          setCustomers(data);
        } else {
          console.error('Failed to fetch customers');
        }
    } catch (error) {
      console.error('Error fetching customers:', error);
    }
    setSelectedUserId(null);
  };

  const handleDelete = (userId) => {
    setSelectedUserId(userId);
    setShowDeleteModal(true);
  };

  const closeDeleteModal = () => {
    setShowDeleteModal(false);
    setSelectedUserId(null);
  };

  const removeCustomerFromTable = (id) => {
    setCustomers(customers.filter((customer) => customer.id !== id));
  };

  const handleAddNewUser = () => {
    setShowAddUserForm(true);
  };

  const updateCustomerList = async () => {
    try {
      const token = localStorage.getItem('token');
      const headers = {
        Authorization: `Bearer ${token}`,
      };
      const response = await axios.get('http://localhost:8081/api/v1/customers', { headers });
      if (response.status >= 200 && response.status < 300) {
        const data = response.data;
        setCustomers(data);
      } else {
        console.error('Failed to fetch customers');
      }
    } catch (error) {
      console.error('Error fetching customers:', error);
    }
  };

  const closeAddUserForm = () => {
    setShowAddUserForm(false);
  };
  

  return (
    <div>
      <h2>User Management</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {customers.map((customer) => (
            <tr key={customer.id}>
              <td>{customer.id}</td>
              <td>{customer.username}</td>
              <td>{customer.email}</td>
              <td>
                <button onClick={() => handleEdit(customer.id)}>Edit</button>
                <button onClick={() => handleDelete(customer.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {showAddUserForm ? (
      <AddUserForm
        updateCustomerList={updateCustomerList}
        closeAddUserForm={closeAddUserForm} 
      />
    ) : (
      <button onClick={handleAddNewUser}>Add New User</button>
    )}

      {showEditForm && (
        <EditUserForm customerId={selectedUserId} closeEditForm={closeEditForm} />
      )}

      <CustomerDeletionModal
        customerId={selectedUserId}
        show={showDeleteModal}
        close={() => {
          closeDeleteModal();
          removeCustomerFromTable(selectedUserId); 
        }}
      />

    </div>
  );
};

export default UserTable;
