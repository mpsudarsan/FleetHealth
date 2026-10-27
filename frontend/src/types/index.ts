export interface Vehicle {
  id: number;
  vehicleId: string;
  vin: string;
  manufacturer: string;
  model: string;
  mileage: number;
}

export interface VehicleHealth {
  id: number;
  vehicleId: number;
  oilLife: number;
  tirePressure: number;
  fuelLevel: number;
  engineTemperature: number;
  batteryLevel: number;
  diagnosticTroubleCode: string;
  recordedAt: string;
}

export interface MaintenanceAlert {
  id: number;
  vehicleId: number;
  vehicleIdentifier: string;
  severity: string;
  reason: string;
  status: string;
  createdAt: string;
}
