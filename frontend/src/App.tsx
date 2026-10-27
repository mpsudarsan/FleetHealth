import { Routes, Route, Link, useLocation } from 'react-router-dom'
import Dashboard from './pages/Dashboard'
import VehicleList from './pages/VehicleList'
import VehicleDetails from './pages/VehicleDetails'
import AlertList from './pages/AlertList'
import './App.css'

function App() {
  const location = useLocation()

  return (
    <div>
      {/* Navigation Bar */}
      <nav className="navbar">
        <span className="navbar-brand">🚛 FleetHealth</span>
        <div className="navbar-links">
          <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
            Dashboard
          </Link>
          <Link to="/vehicles" className={location.pathname.startsWith('/vehicles') ? 'active' : ''}>
            Vehicles
          </Link>
          <Link to="/alerts" className={location.pathname === '/alerts' ? 'active' : ''}>
            Alerts
          </Link>
        </div>
      </nav>

      {/* Page Routes */}
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/vehicles" element={<VehicleList />} />
        <Route path="/vehicles/:id" element={<VehicleDetails />} />
        <Route path="/alerts" element={<AlertList />} />
      </Routes>
    </div>
  )
}

export default App
