import { useEffect, useState } from 'react'
import { getVehicles, getAlerts } from '../services/api'
import { Vehicle, MaintenanceAlert } from '../types'
import { Link } from 'react-router-dom'

function Dashboard() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([])
  const [alerts, setAlerts] = useState<MaintenanceAlert[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function fetchData() {
      try {
        const [vehicleData, alertData] = await Promise.all([
          getVehicles(),
          getAlerts(),
        ])
        setVehicles(vehicleData)
        setAlerts(alertData)
      } catch (err) {
        console.error('Failed to fetch dashboard data:', err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) return <div className="loading">Loading dashboard...</div>

  const openAlerts = alerts.filter((a) => a.status === 'OPEN')
  const vehicleIdsWithAlerts = new Set(openAlerts.map((a) => a.vehicleId))
  const healthyCount = vehicles.filter((v) => !vehicleIdsWithAlerts.has(v.id)).length
  const needsMaintenanceCount = vehicles.filter((v) => vehicleIdsWithAlerts.has(v.id)).length

  return (
    <div className="page">
      <h1 className="page-title">Dashboard</h1>

      {/* Summary Cards */}
      <div className="card-grid">
        <div className="card">
          <div className="card-label">Total Vehicles</div>
          <div className="card-value">{vehicles.length}</div>
        </div>
        <div className="card">
          <div className="card-label">Healthy Vehicles</div>
          <div className="card-value healthy">{healthyCount}</div>
        </div>
        <div className="card">
          <div className="card-label">Needs Maintenance</div>
          <div className="card-value warning">{needsMaintenanceCount}</div>
        </div>
        <div className="card">
          <div className="card-label">Open Alerts</div>
          <div className="card-value danger">{openAlerts.length}</div>
        </div>
      </div>

      {/* Recent Alerts */}
      {openAlerts.length > 0 && (
        <>
          <div className="section-header">
            <h2 className="section-title">Recent Open Alerts</h2>
            <Link to="/alerts" className="btn btn-secondary">View All</Link>
          </div>
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Vehicle</th>
                  <th>Severity</th>
                  <th>Reason</th>
                  <th>Created</th>
                </tr>
              </thead>
              <tbody>
                {openAlerts.slice(0, 5).map((alert) => (
                  <tr key={alert.id}>
                    <td>{alert.vehicleIdentifier || `Vehicle #${alert.vehicleId}`}</td>
                    <td>
                      <span className={`badge badge-${alert.severity.toLowerCase()}`}>
                        {alert.severity}
                      </span>
                    </td>
                    <td>{alert.reason}</td>
                    <td>{new Date(alert.createdAt).toLocaleString()}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </div>
  )
}

export default Dashboard
