import React, { useState, useEffect } from 'react';
import SideNavBar from '../../components/SideNav/SideNavBar';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import HomePage from '../../components/HomePage';
import MyClaims from '../../components/MyClaims';
import { patientNavLinks } from './patientNavLinks';
import { patientInstance } from '../../utils/axiosInstances';
const PatientHome = () => {
  const navigate = useNavigate();
  const [patientData, setPatientData] = useState(null);
  useEffect(() => {
    const fetchPatientInfo = async () => {
      try {
        const token = localStorage.getItem('Patient');

        // const response = await axios.get('http://localhost:3434/api/patient/getPatient', {
        //   headers: {
        //     'Authorization': `Bearer ${token}`,
        //   },
        // });
        const response = await patientInstance.get('getPatient');
        console.log(response.data);
        setPatientData(response.data.data);
      } catch (error) {
        console.error('Failed to fetch patient information:', error);
        if (error.response.data.error === "Invalid token" || error.response.data.error === "Missing authorization header") {
          localStorage.removeItem("Patient")
          //location.href = "/signin";
          toast.error('Session expired. Please log in again.');
          setTimeout(() => {
            navigate("/signin");
          }, 3000);
        }
        else {
          toast.error('An error occurred while fetching hospital data.');
        }
      }
    };

    // if (!user) {
    fetchPatientInfo();
    // }
  }, [navigate]);

  if (!patientData) {
    return <div>Loading...</div>;
  }

  return (
    <div className='d-flex'>
      <SideNavBar
        title='Patient Portal'
        userName={patientData.patientName}
        navLinks={patientNavLinks}
        token="Patient"
      />
      {/* <div className='flex-grow-1'> */}
      <div className='col'>
        <Routes>
          <Route path='dashboard' element={<HomePage userData={patientData} />} />
          <Route
            path='my-claims'
            element={
              <MyClaims
                getClaimsApi='getClaims'
                acceptClaimApi='acceptClaim'
                revertClaimApi='revertClaim'
                rejectClaimApi='rejectClaim'
                tokenName='Patient'
                editableStatus='pending'
              />
            } />
          {/* Add more routes as needed */}
        </Routes>
      </div>
      {/* </div> */}
    </div>
  );
}

export default PatientHome;
