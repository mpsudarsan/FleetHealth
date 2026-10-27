import { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import { getVehicleById, getVehicleHealth, recordVehicleHealth } from '../services/api'
import { Vehicle, VehicleHealth } from '../types'

function VehicleDetails() {
  const { id } = useParams<{ id: string }>()
  const vehicleId = parseInt(id || '0')

  const [vehicle, setVehicle] = useState<Vehicle | null>(null)
  const [healthRecords, setHealthRecords] = useState<VehicleHealth[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    oilLife: '',
    tirePressure: '',
    fuelLevel: '',
    engineTemperature: '',
    batteryLevel: '',
    diagnosticTroubleCode: '',
  })

  useEffect(() => {
    fetchData()
  }, [vehicleId])

  async function fetchData() {
    try {
      const [vehicleData, healthData] = await Promise.all([
        getVehicleById(vehicleId),
        getVehicleHealth(vehicleId),
      ])
      setVehicle(vehicleData)
      setHealthRecords(healthData)
    } catch (err) {
      console.error('Failed to fetch vehicle details:', err)
    } finally {
      setLoading(false)
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    try {
      await recordVehicleHealth(vehicleId, {
        oilLife: formData.oilLife ? parseInt(formData.oilLife) : undefined,
        tirePressure: formData.tirePressure ? parseInt(formData.tirePressure) : undefined,
        fuelLevel: formData.fuelLevel ? parseInt(formData.fuelLevel) : undefined,
        engineTemperature: formData.engineTemperature ? parseInt(formData.engineTemperature) : undefined,
        batteryLevel: formData.batteryLevel ? parseInt(formData.batteryLevel) : undefined,
        diagnosticTroubleCode: formData.diagnosticTroubleCode || undefined,
      })

      setFormData({ oilLife: '', tirePressure: '', fuelLevel: '', engineTemperature: '', batteryLevel: '', diagnosticTroubleCode: '' })
      setShowForm(false)
      fetchData()
    } catch (err) {
      console.error('Failed to record health data:', err)
    }
  }

  // Helper to color-code health values
  function getHealthColor(label: string, value: number | undefined): string {
    if (value === undefined || value === null) return ''
    switch (label) {
      case 'oilLife':
        return value < 10 ? 'critical' : value < 30 ? 'warn' : 'good'
      case 'tirePressure':
        return value < 30 ? 'critical' : value < 35 ? 'warn' : 'good'
      case 'fuelLevel':
        return value < 15 ? 'critical' : value < 30 ? 'warn' : 'good'
      case 'engineTemperature':
        return value > 100 ? 'critical' : value > 90 ? 'warn' : 'good'
      case 'batteryLevel':
        return value < 20 ? 'critical' : value < 40 ? 'warn' : 'good'
      default:
        return ''
    }
  }

  if (loading) return <div className="loading">Loading vehicle details...</div>
  if (!vehicle) return <div className="error">Vehicle not found</div>

  const latestHealth = healthRecords.length > 0 ? healthRecords[0] : null

  return (
    <div className="page">
      <Link to="/vehicles" style={{ fontSize: '0.9rem', marginBottom: '16px', display: 'inline-block' }}>
        ← Back to Vehicles
      </Link>

      <h1 className="page-title">
        {vehicle.vehicleId} — {vehicle.manufacturer} {vehicle.model}
      </h1>

      {/* Vehicle Info */}
      <div className="card-grid">
        <div className="card">
          <div className="card-label">Vehicle ID</div>
          <div className="card-value" style={{ fontSize: '1.2rem' }}>{vehicle.vehicleId}</div>
        </div>
        <div className="card">
          <div className="card-label">VIN</div>
          <div className="card-value" style={{ fontSize: '1.2rem' }}>{vehicle.vin}</div>
        </div>
        <div className="card">
          <div className="card-label">Mileage</div>
          <div className="card-value" style={{ fontSize: '1.2rem' }}>{vehicle.mileage?.toLocaleString()} km</div>
        </div>
      </div>

      {/* Latest Health Readings */}
      <div className="section-header">
        <h2 className="section-title">Latest Health Readings</h2>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Record Health'}
        </button>
      </div>

      {latestHealth ? (
        <div className="detail-grid">
          <div className="detail-card">
            <div className="detail-label">Oil Life</div>
            <div className={`detail-value ${getHealthColor('oilLife', latestHealth.oilLife)}`}>
              {latestHealth.oilLife ?? '—'}%
            </div>
          </div>
          <div className="detail-card">
            <div className="detail-label">Tire Pressure</div>
            <div className={`detail-value ${getHealthColor('tirePressure', latestHealth.tirePressure)}`}>
              {latestHealth.tirePressure ?? '—'} PSI
            </div>
          </div>
          <div className="detail-card">
            <div className="detail-label">Fuel Level</div>
            <div className={`detail-value ${getHealthColor('fuelLevel', latestHealth.fuelLevel)}`}>
              {latestHealth.fuelLevel ?? '—'}%
            </div>
          </div>
          <div className="detail-card">
            <div className="detail-label">Engine Temp</div>
            <div className={`detail-value ${getHealthColor('engineTemperature', latestHealth.engineTemperature)}`}>
              {latestHealth.engineTemperature ?? '—'}°C
            </div>
          </div>
          <div className="detail-card">
            <div className="detail-label">Battery Level</div>
            <div className={`detail-value ${getHealthColor('batteryLevel', latestHealth.batteryLevel)}`}>
              {latestHealth.batteryLevel ?? '—'}%
            </div>
          </div>
          <div className="detail-card">
            <div className="detail-label">DTC</div>
            <div className={`detail-value ${latestHealth.diagnosticTroubleCode ? 'critical' : 'good'}`}
                 style={{ fontSize: '1rem' }}>
              {latestHealth.diagnosticTroubleCode || 'None'}
            </div>
          </div>
        </div>
      ) : (
        <p style={{ color: '#6b7280', marginBottom: '24px' }}>No health data recorded yet.</p>
      )}

      {/* Record Health Form */}
      {showForm && (
        <div className="form-container">
          <h3 className="form-title">Record Health Data</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label>Oil Life (%)</label>
                <input type="number" min="0" max="100" placeholder="e.g., 8"
                  value={formData.oilLife}
                  onChange={(e) => setFormData({ ...formData, oilLife: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Tire Pressure (PSI)</label>
                <input type="number" min="0" placeholder="e.g., 27"
                  value={formData.tirePressure}
                  onChange={(e) => setFormData({ ...formData, tirePressure: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Fuel Level (%)</label>
                <input type="number" min="0" max="100" placeholder="e.g., 40"
                  value={formData.fuelLevel}
                  onChange={(e) => setFormData({ ...formData, fuelLevel: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Engine Temperature (°C)</label>
                <input type="number" placeholder="e.g., 105"
                  value={formData.engineTemperature}
                  onChange={(e) => setFormData({ ...formData, engineTemperature: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Battery Level (%)</label>
                <input type="number" min="0" max="100" placeholder="e.g., 75"
                  value={formData.batteryLevel}
                  onChange={(e) => setFormData({ ...formData, batteryLevel: e.target.value })} />
              </div>
              <div className="form-group">
                <label>Diagnostic Trouble Code</label>
                <input type="text" placeholder="e.g., ENGINE_OVERHEAT"
                  value={formData.diagnosticTroubleCode}
                  onChange={(e) => setFormData({ ...formData, diagnosticTroubleCode: e.target.value })} />
              </div>
            </div>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">Record Health</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Health History */}
      {healthRecords.length > 0 && (
        <>
          <h2 className="section-title" style={{ marginBottom: '16px' }}>Health History</h2>
          <div className="table-container">
            <table>
              <thead>
                <tr>
                  <th>Recorded At</th>
                  <th>Oil Life</th>
                  <th>Tire PSI</th>
                  <th>Fuel</th>
                  <th>Engine °C</th>
                  <th>Battery</th>
                  <th>DTC</th>
                </tr>
              </thead>
              <tbody>
                {healthRecords.map((record) => (
                  <tr key={record.id}>
                    <td>{new Date(record.recordedAt).toLocaleString()}</td>
                    <td>{record.oilLife ?? '—'}%</td>
                    <td>{record.tirePressure ?? '—'}</td>
                    <td>{record.fuelLevel ?? '—'}%</td>
                    <td>{record.engineTemperature ?? '—'}°C</td>
                    <td>{record.batteryLevel ?? '—'}%</td>
                    <td>{record.diagnosticTroubleCode || '—'}</td>
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

export default VehicleDetails
