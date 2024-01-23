import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import { Link } from 'react-router-dom';
import { Chart as ChartJS } from 'chart.js/auto'
import { Chart }            from 'react-chartjs-2'
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';


const UserDashboard = ({ customerId }) => {
  const [devices, setDevices] = useState([]); 
  const [messages, setMessages] = useState([]);
  const [chartData, setChartData] = useState(null);
  const [selectedDate, setSelectedDate] = useState(null);

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const response = await fetch(`http://localhost:8082/api/v1/devices/${customerId}`,{
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          },
        });
        if (response.ok) { 
          const data = await response.json();
          console.log(data);
          setDevices(data.device || []); 
         
        } else {
          console.error('Failed to fetch devices');
        }
      } catch (error) {
        console.error('Error fetching devices:', error);
      }
    };

    fetchDevices();
  }, [customerId]);

  const handleGenerateChart = async () => {
    try {
      const response = await fetch('http://localhost:8085/api/v1/messages',{
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      if (response.ok) {
        const data = await response.json();
        console.log('Messages data:', data);
        setMessages(data || []);

        const selectedDateMessages = data.filter(
          (message) =>
            new Date(message.timestamp).toDateString() === selectedDate.toDateString()
        );

        const hourlyEnergyConsumption = selectedDateMessages.reduce((acc, message) => {
          const hour = new Date(message.timestamp).getHours();
          acc[hour] = (acc[hour] || 0) + message.energyConsumption;
          return acc;
        }, {});

        // Extract relevant data for the chart (example: using energy consumption as the data point)
        const chartData = {
          labels: Object.keys(hourlyEnergyConsumption).map((hour) => `${hour}:00`),
          datasets: [
            {
              label: 'Energy Consumption',
              data: Object.values(hourlyEnergyConsumption),
              borderColor: 'rgba(75,192,192,1)',
              backgroundColor: 'rgba(75,192,192,0.4)',
            },
          ],
        };

        setChartData(chartData);
      } else {
        console.error('Failed to fetch messages');
      }
    } catch (error) {
      console.error('Error fetching messages:', error);
    }
  };


  return (
    <div>
      <h2>User Dashboard</h2>
      <Link to="/chat"> 
          <button className="admin-button">Chat</button>
        </Link>
      <h3>Devices:</h3>
      {devices && devices.length > 0 ? (
        <ul>
          {devices.map(device => (
            <li key={device.id}>
              <strong>Name:</strong> {device.name}<br />
              <strong>Address:</strong> {device.address}<br />
              <strong>Max Hourly Energy Consumption:</strong> {device.maximumHourlyEnergyConsumption}
            </li>
          ))}
        </ul>
      ) : (
        <p>No devices found...</p>
      )}

    <label>Select a day:</label>
    <DatePicker selected={selectedDate} onChange={(date) => setSelectedDate(date)} />
    <button onClick={handleGenerateChart}>Generate Chart</button>

      {chartData && (
        <div>
          <h3>Energy Consumption Chart:</h3>
          <Line data={chartData} />
        </div>
      )}
    </div>
  );
};

export default UserDashboard;
