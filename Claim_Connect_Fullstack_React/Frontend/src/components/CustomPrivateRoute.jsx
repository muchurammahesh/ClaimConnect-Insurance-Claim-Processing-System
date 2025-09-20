// src/components/PrivateRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';

const CustomPrivateRoute = ({ children, roleType }) => {

//   if (!roleType) {
//     console.error('PrivateRoute: roleType is required');
//     return <Navigate to="/signin" replace />;
//   }

//   let tokenKey = '';
//   switch (roleType) {
//     case 'Hospital':
//       tokenKey = 'hospitalToken';
//       break;
//     case 'Patient':
//       tokenKey = 'patientToken';
//       break;
//     case 'InsuranceCompany':
//       tokenKey = 'insuranceToken';
//       break;
//     default:
//       console.error(`PrivateRoute: Invalid roleType "${roleType}"`);
//       return <Navigate to="/signin" replace />;
//   }

  const token = localStorage.getItem(roleType);

  if (!token) {
    return <Navigate to={'/signin'} replace />;
    // return <Navigate to={`/signin?role=${roleType}`} replace />;
  }

  return children;
};

export default CustomPrivateRoute;
