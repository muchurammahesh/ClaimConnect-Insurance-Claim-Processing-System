import { useState } from 'react'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import HospitalHome from './pages/Hospital/HospitalHome';
import PatientHome from './pages/Patient/PatientHome';
import InsuranceCompHome from './pages/InsuranceComp/InsuranceCompHome'
import './App.css'
import LandingPage from './pages/LandingPage';
import SignUp from './pages/Auth/SignUp';
import SignIn from './pages/Auth/SignIn';
import CustomPrivateRoute from './components/CustomPrivateRoute';

function App() {

  return (
    <Router>

      <Routes>
        <Route path='/' Component={LandingPage} />

        {/* Authentication Routes */}
        <Route path='/signup' Component={SignUp} />
        <Route path='/signin' Component={SignIn} />


        {/* Protected Routes */}
        <Route
          path='/hospital/*'
          element={
            <CustomPrivateRoute roleType="Hospital">
              <HospitalHome />
            </CustomPrivateRoute>
          }
        />
        <Route
          path='/patient/*'
          element={
            <CustomPrivateRoute roleType="Patient">
              <PatientHome />
            </CustomPrivateRoute>
          }
        />
        <Route
          path='/insurance/*'
          element={
            <CustomPrivateRoute roleType="InsuranceCompany">
              <InsuranceCompHome />
            </CustomPrivateRoute>
          }
        />
      </Routes>
    </Router>

  )
}

export default App
// <Router>
//   <Routes>
//     <Route path="/" element={<Login />} />
//     <Route path="/hospital-home" element={<HospitalHome />} />
//     <Route path="/patient-home" element={<PatientHome />} />
//     <Route path="/insurance-home" element={<InsuranceCompHome />} />
//   </Routes>
// </Router>