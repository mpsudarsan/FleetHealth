import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getVehicles, createVehicle } from '../services/api'
import { Vehicle } from '../types'

function VehicleList() {
  const [vehicles, setVehicles] = useState<Vehicle[]>([])
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [formData, setFormData] = useState({
    vehicleId: '',
    vin: '',
    manufacturer: '',
    model: '',
    mileage: '',
  })
  const [error, setError] = useState('')

  useEffect(() => {
    fetchVehicles()
  }, [])

  async function fetchVehicles() {
    try {
      const data = await getVehicles()
      setVehicles(data)
    } catch (err) {
      console.error('Failed to fetch vehicles:', err)
    } finally {
      setLoading(false)
    }
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')

    if (!formData.vehicleId || !formData.vin) {
      setError('Vehicle ID and VIN are required')
      return
    }

    try {
      await createVehicle({
        vehicleId: formData.vehicleId,
        vin: formData.vin,
        manufacturer: formData.manufacturer,
        model: formData.model,
        mileage: formData.mileage ? parseInt(formData.mileage) : 0,
      })

      // Reset form and refresh list
      setFormData({ vehicleId: '', vin: '', manufacturer: '', model: '', mileage: '' })
      setShowForm(false)
      fetchVehicles()
    } catch (err) {
      setError('Failed to create vehicle. Please check your input.')
      console.error(err)
    }
  }

  if (loading) return <div className="loading">Loading vehicles...</div>

  return (
    <div className="page">
      <div className="section-header">
        <h1 className="page-title">Vehicles</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Vehicle'}
        </button>
      </div>

      {/* Add Vehicle Form */}
      {showForm && (
        <div className="form-container">
          <h3 className="form-title">Add New Vehicle</h3>
          {error && <p className="error" style={{ padding: '8px', textAlign: 'left' }}>{error}</p>}
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label>Vehicle ID *</label>
                <input
                  type="text"
                  placeholder="e.g., VH-102"
                  value={formData.vehicleId}
                  onChange={(e) => setFormData({ ...formData, vehicleId: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>VIN *</label>
                <input
                  type="text"
                  placeholder="e.g., ABC123456789"
                  value={formData.vin}
                  onChange={(e) => setFormData({ ...formData, vin: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Manufacturer</label>
                <input
                  type="text"
                  placeholder="e.g., Toyota"
                  value={formData.manufacturer}
                  onChange={(e) => setFormData({ ...formData, manufacturer: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Model</label>
                <input
                  type="text"
                  placeholder="e.g., Corolla"
                  value={formData.model}
                  onChange={(e) => setFormData({ ...formData, model: e.target.value })}
                />
              </div>
              <div className="form-group">
                <label>Mileage (km)</label>
                <input
                  type="number"
                  placeholder="e.g., 98500"
                  value={formData.mileage}
                  onChange={(e) => setFormData({ ...formData, mileage: e.target.value })}
                />
              </div>
            </div>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">Add Vehicle</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </form>
        </div>
      )}

      {/* Vehicle Table */}
      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Vehicle ID</th>
              <th>VIN</th>
              <th>Manufacturer</th>
              <th>Model</th>
              <th>Mileage</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {vehicles.length === 0 ? (
              <tr>
                <td colSpan={6} style={{ textAlign: 'center', color: '#6b7280' }}>
                  No vehicles yet. Add your first vehicle above.
                </td>
              </tr>
            ) : (
              vehicles.map((vehicle) => (
                <tr key={vehicle.id}>
                  <td><strong>{vehicle.vehicleId}</strong></td>
                  <td>{vehicle.vin}</td>
                  <td>{vehicle.manufacturer}</td>
                  <td>{vehicle.model}</td>
                  <td>{vehicle.mileage?.toLocaleString()} km</td>
                  <td>
                    <Link to={`/vehicles/${vehicle.id}`} className="btn btn-secondary">
                      Details
                    </Link>
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

export default VehicleList
