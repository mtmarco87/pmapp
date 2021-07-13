import React from 'react';
import Dashboard from './app/components/layout/dashboard/Dashboard';
import Alerts from './app/components/layout/alerts/Alerts';

export default function App() {
  return (
    <>
      {/* Snackbars/Alerts */}
      <Alerts />
      {/* Main Dashboard rendering App and Routes */}
      <Dashboard />
    </>
  );
};
