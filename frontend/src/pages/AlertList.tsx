import { useEffect, useState } from 'react'
import { getAlerts, resolveAlert } from '../services/api'
import { MaintenanceAlert } from '../types'

function AlertList() {
  const [alerts, setAlerts] = useState<MaintenanceAlert[]>([])
  const [loading, setLoading] = useState(true)
  const [resolvingId, setResolvingId] = useState<number | null>(null)

  useEffect(() => {
    fetchAlerts()
  }, [])

  async function fetchAlerts() {
    try {
      const data = await getAlerts()
      setAlerts(data)
    } catch (err) {
      console.error('Failed to fetch alerts:', err)
    } finally {
      setLoading(false)
    }
  }

  async function handleResolve(alertId: number) {
    setResolvingId(alertId)
    try {
      await resolveAlert(alertId)
      fetchAlerts()
    } catch (err) {
      console.error('Failed to resolve alert:', err)
    } finally {
      setResolvingId(null)
    }
  }

  if (loading) return <div className="loading">Loading alerts...</div>

  const openAlerts = alerts.filter((a) => a.status === 'OPEN')
  const resolvedAlerts = alerts.filter((a) => a.status === 'RESOLVED')

  return (
    <div className="page">
      <h1 className="page-title">Maintenance Alerts</h1>

      {/* Summary */}
      <div className="card-grid" style={{ marginBottom: '32px' }}>
        <div className="card">
          <div className="card-label">Total Alerts</div>
          <div className="card-value">{alerts.length}</div>
        </div>
        <div className="card">
          <div className="card-label">Open</div>
          <div className="card-value danger">{openAlerts.length}</div>
        </div>
        <div className="card">
          <div className="card-label">Resolved</div>
          <div className="card-value healthy">{resolvedAlerts.length}</div>
        </div>
      </div>

      {/* Alerts Table */}
      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Vehicle</th>
              <th>Severity</th>
              <th>Reason</th>
              <th>Status</th>
              <th>Created At</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {alerts.length === 0 ? (
              <tr>
                <td colSpan={6} style={{ textAlign: 'center', color: '#6b7280' }}>
                  No alerts. All vehicles are healthy! 🎉
                </td>
              </tr>
            ) : (
              alerts.map((alert) => (
                <tr key={alert.id}>
                  <td><strong>{alert.vehicleIdentifier || `Vehicle #${alert.vehicleId}`}</strong></td>
                  <td>
                    <span className={`badge badge-${alert.severity.toLowerCase()}`}>
                      {alert.severity}
                    </span>
                  </td>
                  <td>{alert.reason}</td>
                  <td>
                    <span className={`badge badge-${alert.status.toLowerCase()}`}>
                      {alert.status}
                    </span>
                  </td>
                  <td>{new Date(alert.createdAt).toLocaleString()}</td>
                  <td>
                    {alert.status === 'OPEN' ? (
                      <button
                        className="btn btn-success"
                        onClick={() => handleResolve(alert.id)}
                        disabled={resolvingId === alert.id}
                      >
                        {resolvingId === alert.id ? 'Resolving...' : 'Resolve'}
                      </button>
                    ) : (
                      <span style={{ color: '#6b7280', fontSize: '0.85rem' }}>—</span>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default AlertList
