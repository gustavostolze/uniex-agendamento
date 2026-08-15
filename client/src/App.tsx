import { BrowserRouter, Routes, Route } from 'react-router-dom';
import ClientBooking from './pages/ClientBooking';
import Dashboard from './pages/Dashboard';
import './App.css';
import Login from './pages/Login';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ClientBooking />} />

        <Route path="/login" element={<Login />} />
        
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;