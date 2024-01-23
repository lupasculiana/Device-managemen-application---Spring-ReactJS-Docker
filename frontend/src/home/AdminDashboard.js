import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './AdminDashboard.css';
import DeviceTable from './DeviceTable'; 
import UserTable from "./UserTable";
import CreateMappingForm from './CreateMappingForm';

const AdminDashboard = () => {
  const [showDeviceTable, setShowDeviceTable] = useState(false);
  const [showUserTable, setShowUserTable] = useState(false);
  const [showCreateMappingForm, setShowCreateMappingForm] = useState(false);

  const handleManageDevicesClick = () => {
    setShowUserTable(false);
    setShowDeviceTable(true);
  };

  const handleManageUsersClick = () => {
    setShowDeviceTable(false);
    setShowUserTable(true);
  };

  const handleCreateMappingClick = () => {
    setShowDeviceTable(false);
    setShowUserTable(false);
    setShowCreateMappingForm(true); 
  };

  return (
    <div className="admin-dashboard">
      <h2>Admin Dashboard</h2>
      <div className="button-container">
        <button className="admin-button" onClick={handleManageDevicesClick}>Manage Devices</button>
        <button className="admin-button" onClick={handleCreateMappingClick}>Create Mapping</button>
        <button className="admin-button" onClick={handleManageUsersClick}>Manage Users</button>
        <Link to="/chat"> 
          <button className="admin-button">Chat</button>
        </Link>
        {showDeviceTable && <DeviceTable />}
        {showUserTable && <UserTable />}
        {showCreateMappingForm && <CreateMappingForm />}
      </div>
    </div>
  );
};

export default AdminDashboard;
