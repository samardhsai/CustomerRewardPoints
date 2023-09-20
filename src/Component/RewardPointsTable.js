import React, { useEffect, useState } from "react";
import axios from "axios";
import "bootstrap/dist/css/bootstrap.min.css";

const RewardPointsTable = () => {
  const [rewardPointsData, setRewardPointsData] = useState({});
  const [totalPoints, setTotalPoints] = useState({});
  const [selectedDuration, setSelectedDuration] = useState(3); 

  useEffect(() => {
    // Fetch the data from your backend API based on selectedDuration
    axios
      .get(`http://localhost:8080/api/transactions/rewardPointsByDuration/${selectedDuration}`)
      .then((response) => {
        setRewardPointsData(response.data);

        // Calculate total points for each customer
        const totalPointsData = {};
        for (const customerId in response.data) {
          const customerData = response.data[customerId];
          const total = Object.values(customerData).reduce(
            (acc, val) => acc + (val || 0),
            0
          );
          totalPointsData[customerId] = total;
        }
        setTotalPoints(totalPointsData);
      })
      .catch((error) => {
        console.error("Error fetching data:", error);
      });
  }, [selectedDuration]); 

  // Extract unique months from the data
  const months = Array.from(
    new Set(
      Object.values(rewardPointsData).flatMap((customerData) =>
        Object.keys(customerData)
      )
    )
  );

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Customer Reward Points</h2>
      <div className="mb-3">
        <label htmlFor="durationSelect" className="form-label">
          Select Duration:
        </label>
        <select
          id="durationSelect"
          className="form-select"
          value={selectedDuration}
          onChange={(e) => setSelectedDuration(parseInt(e.target.value))}
        >
          <option value={1}>1 Month</option>
          <option value={3}>3 Months</option>
          <option value={6}>6 Months</option>
        </select>
      </div>
      <div className="table-responsive">
        <table className="table table-bordered">
          <thead>
            <tr>
              <th>Customer ID</th>
              {months.map((month) => (
                <th key={month}>{month}</th>
              ))}
              <th>Total</th> {/* Add Total column */}
            </tr>
          </thead>
          <tbody>
            {Object.keys(rewardPointsData).map((customerId) => (
              <tr key={customerId}>
                <td>{customerId}</td>
                {months.map((month) => (
                  <td key={`${customerId}-${month}`}>
                    {rewardPointsData[customerId][month] || 0}
                  </td>
                ))}
                <td>{totalPoints[customerId] || 0}</td> {/* Display Total */}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default RewardPointsTable;
