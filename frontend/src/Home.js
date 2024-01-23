import React from "react";
import UserDashboard from "./home/UserDashboard";
import AdminDashboard from "./home/AdminDashboard";

const Home = ({ userRole, customerId }) => {
    
    console.log(userRole,customerId);
    return (
        <div>
            <h1>Welcome to the Dashboard</h1>
            {userRole === 'ADMIN' ? (
                <AdminDashboard customerId={customerId} />
            ) : (
                <UserDashboard customerId={customerId} />
            )}
        </div>
    );
};


export default Home;