import { Vehicle, VehicleHealth, MaintenanceAlert } from '../types';

const API_BASE = '/api';

// ---------- Vehicle APIs ----------

export async function getVehicles(): Promise<Vehicle[]> {
  const response = await fetch(`${API_BASE}/vehicles`);
  if (!response.ok) throw new Error('Failed to fetch vehicles');
  return response.json();
}

export async function getVehicleById(id: number): Promise<Vehicle> {
  const response = await fetch(`${API_BASE}/vehicles/${id}`);
  if (!response.ok) throw new Error('Failed to fetch vehicle');
  return response.json();
}

export async function createVehicle(vehicle: Partial<Vehicle>): Promise<Vehicle> {
  const response = await fetch(`${API_BASE}/vehicles`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(vehicle),
  });
  if (!response.ok) throw new Error('Failed to create vehicle');
  return response.json();
}

// ---------- Vehicle Health APIs ----------

export async function getVehicleHealth(vehicleId: number): Promise<VehicleHealth[]> {
  const response = await fetch(`${API_BASE}/vehicles/${vehicleId}/health`);
  if (!response.ok) throw new Error('Failed to fetch vehicle health');
  return response.json();
}

export async function recordVehicleHealth(
  vehicleId: number,
  health: Partial<VehicleHealth>
): Promise<VehicleHealth> {
  const response = await fetch(`${API_BASE}/vehicles/${vehicleId}/health`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(health),
  });
  if (!response.ok) throw new Error('Failed to record vehicle health');
  return response.json();
}

// ---------- Maintenance Alert APIs ----------

export async function getAlerts(): Promise<MaintenanceAlert[]> {
  const response = await fetch(`${API_BASE}/maintenance/alerts`);
  if (!response.ok) throw new Error('Failed to fetch alerts');
  return response.json();
}

export async function resolveAlert(alertId: number): Promise<MaintenanceAlert> {
  const response = await fetch(`${API_BASE}/maintenance/alerts/${alertId}/resolve`, {
    method: 'PUT',
  });
  if (!response.ok) throw new Error('Failed to resolve alert');
  return response.json();
}
