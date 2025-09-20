// // src/components/HomePage.js
// import React from 'react';
// import { Card } from 'react-bootstrap';

// const HomePage = ({ userData }) => {
//   const getUserInfo = (data) => {
//     if ('hospitalId' in data) {
//       return {
//         title: data.hospitalName,
//         subtitle: data.hospitalEmail,
//         idLabel: 'Hospital ID',
//         idValue: data.hospitalId,
//       };
//     } else if ('patientId' in data) {
//       return {
//         title: data.patientName,
//         subtitle: data.patientEmail,
//         idLabel: 'Patient ID',
//         idValue: data.patientId,
//         insuranceLabel: 'Insurance Company ID',
//         insuranceValue: data.insuranceCompId,
//       };
//     } else if ('insuranceCompId' in data) {
//       return {
//         title: data.insuranceCompName,
//         subtitle: data.insuranceCompEmail,
//         idLabel: 'Insurance Company ID',
//         idValue: data.insuranceCompId,
//       };
//     }
//     return {};
//   };

//   console.log("Inside home page: ", userData);
//   const userInfo = getUserInfo(userData);

//   return (
//     <Card className="m-4">
//       <Card.Body>
//         <Card.Title>{userInfo.title}</Card.Title>
//         <Card.Subtitle className="mb-2 text-muted">{userInfo.subtitle}</Card.Subtitle>
//         <Card.Text>
//           {userInfo.idLabel}: {userInfo.idValue}
//         </Card.Text>
//         {userInfo.insuranceLabel && (
//           <Card.Text>
//             {userInfo.insuranceLabel}: {userInfo.insuranceValue}
//           </Card.Text>
//         )}
//       </Card.Body>
//     </Card>
//   );
// };

// export default HomePage;





















// src/components/HomePage.js
import React, { useState } from 'react';
import { Card, Button, Modal, Form } from 'react-bootstrap';
import { FaEdit } from 'react-icons/fa';
import { ToastContainer, toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';
import 'react-toastify/dist/ReactToastify.css';
import { useForm } from 'react-hook-form';
import {
  hospitalInstance,
  patientInstance,
  insuranceInstance
} from '../utils/axiosInstances';

const HomePage = ({ userData }) => {
  const navigate = useNavigate();

  // Determine the role-specific properties from userData
  const getUserInfo = (data) => {
    if (!data) return {};
    if ('hospitalId' in data) {
      return {
        role: 'Hospital',
        title: data.hospitalName,
        subtitle: data.hospitalEmail,
        idLabel: 'Hospital ID',
        idValue: data.hospitalId,
      };
    } else if ('patientId' in data) {
      return {
        role: 'Patient',
        title: data.patientName,
        subtitle: data.patientEmail,
        idLabel: 'Patient ID',
        idValue: data.patientId,
        insuranceLabel: 'Insurance Company ID',
        insuranceValue: data.insuranceCompId,
      };
    } else if ('insuranceCompId' in data) {
      return {
        role: 'InsuranceCompany',
        title: data.insuranceCompName,
        subtitle: data.insuranceCompEmail,
        idLabel: 'Insurance Company ID',
        idValue: data.insuranceCompId,
      };
    }
    return {};
  };

  const userInfo = getUserInfo(userData);

  // Prepare the correct axios instance based on role
  const getInstanceByRole = (role) => {
    if (role === 'Hospital') return hospitalInstance;
    else if (role === 'Patient') return patientInstance;
    else if (role === 'InsuranceCompany') return insuranceInstance;
    return null;
  };

  // Using separate react-hook-form hooks for each modal:
  const {
    register: registerProfile,
    handleSubmit: handleProfileSubmit,
    formState: { errors: profileErrors },
    reset: resetProfile,
  } = useForm({
    defaultValues: {
      name: userInfo.title || '',
      // For patient, the insurance company ID is editable
      insuranceCompId: userInfo.insuranceValue || '',
      currentPwd: '',
    },
  });

  const {
    register: registerPassword,
    handleSubmit: handlePasswordSubmit,
    formState: { errors: passwordErrors },
    reset: resetPassword,
  } = useForm({
    defaultValues: {
      oldPassword: '',
      newPassword: '',
    },
  });

  // Modal state
  const [showProfileModal, setShowProfileModal] = useState(false);
  const [showPasswordModal, setShowPasswordModal] = useState(false);

  // Handle Profile Update Submission
  const onProfileSubmit = async (data) => {
    const token = localStorage.getItem(userInfo.role);
    let instance = getInstanceByRole(userInfo.role);
    try {
      let response;
      if (userInfo.role === 'Hospital') {
        response = await instance.put('updateHospital', {
          hospitalName: data.name,
          hospitalPwd: data.currentPwd,
        });
      } else if (userInfo.role === 'Patient') {
        response = await instance.put('updatePatient', {
          patientName: data.name,
          patientPassword: data.currentPwd,
          insuranceCompId: data.insuranceCompId,
        });
      } else if (userInfo.role === 'InsuranceCompany') {
        response = await instance.put('update', {
          insuranceCompName: data.name,
          insuranceCompPwd: data.currentPwd,
        });
      }

      if (response.data.status === 'OK') {
        toast.success(response.data.msg || 'Profile updated successfully.');
        setShowProfileModal(false);
      } else {
        toast.error(response.data.msg || 'Failed to update profile.');
      }
    } catch (error) {
      if (
        error.response?.data?.error === "Invalid token" ||
        error.response?.data?.error === "Missing authorization header"
      ) {
        localStorage.removeItem(userInfo.role);
        toast.error('Session expired. Please log in again.');
        setTimeout(() => {
          navigate("/signin");
        }, 3000);
      } else {
        console.error('Error updating profile:', error);
        toast.error('Error updating profile.');
      }
    }
  };

  // Handle Password Update Submission
  const onPasswordSubmit = async (data) => {
    const token = localStorage.getItem(userInfo.role);
    let instance = getInstanceByRole(userInfo.role);
    try {
      let response;
      if (userInfo.role === 'Hospital') {
        response = await instance.put('updatePassword', {
          oldHospitalPwd: data.oldPassword,
          newHospitalPwd: data.newPassword,
        });
      } else if (userInfo.role === 'Patient') {
        response = await instance.put('updatePassword', {
          oldPatientPassword: data.oldPassword,
          newPatientPassword: data.newPassword,
        });
      } else if (userInfo.role === 'InsuranceCompany') {
        response = await instance.put('updatePassword', {
          oldInsuranceCompPwd: data.oldPassword,
          newInsuranceCompPwd: data.newPassword,
        });
      }

      if (response.data.status === 'OK') {
        toast.success(response.data.msg || 'Password updated successfully.');
        localStorage.removeItem(userInfo.role);
        setShowPasswordModal(false);
        setTimeout(() => {
          toast.info('Please log in again with your new password.');
          window.location.href = '/signin';
        }, 2000);
      } else {
        toast.error(response.data.msg || 'Failed to update password.');
      }
    } catch (error) {
      if (
        error.response?.data?.error === "Invalid token" ||
        error.response?.data?.error === "Missing authorization header"
      ) {
        localStorage.removeItem(userInfo.role);
        toast.error('Session expired. Please log in again.');
        setTimeout(() => {
          navigate("/signin");
        }, 3000);
      } else {
        console.error('Error updating password:', error);
        toast.error('Error updating password.');
      }
    }
  };

  return (
    <>
      <ToastContainer position="top-center" />
      <Card className="m-4">
        <Card.Body>
          <div className="d-flex justify-content-between align-items-center">
            <Card.Title>{userInfo.title}</Card.Title>
            <Button variant="link" onClick={() => setShowProfileModal(true)}>
              <FaEdit />
            </Button>
          </div>
          <Card.Subtitle className="mb-2 text-muted">{userInfo.subtitle}</Card.Subtitle>
          <Card.Text>
            {userInfo.idLabel}: {userInfo.idValue}
          </Card.Text>
          {userInfo.insuranceLabel && (
            <Card.Text>
              {userInfo.insuranceLabel}: {userInfo.insuranceValue}
            </Card.Text>
          )}
          <Button variant="outline-primary" onClick={() => setShowPasswordModal(true)}>
            Update Password
          </Button>
        </Card.Body>
      </Card>

      {/* Profile Update Modal */}
      <Modal show={showProfileModal} onHide={() => setShowProfileModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Profile</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleProfileSubmit(onProfileSubmit)}>
            <Form.Group controlId="updateName">
              <Form.Label>
                {userInfo.role === 'Hospital'
                  ? 'Update Hospital Name'
                  : userInfo.role === 'Patient'
                  ? 'Update Patient Name'
                  : 'Update Insurance Company Name'}
              </Form.Label>
              <Form.Control
                type="text"
                placeholder="Enter name"
                {...registerProfile("name", { required: "Name is required" })}
              />
              {profileErrors.name && <p className="text-danger">{profileErrors.name.message}</p>}
            </Form.Group>
            {userInfo.role === 'Patient' && (
              <Form.Group controlId="updateInsuranceCompId" className="mt-3">
                <Form.Label>Update Insurance Company ID</Form.Label>
                <Form.Control
                  type="number"
                  placeholder="Enter Insurance Company ID"
                  {...registerProfile("insuranceCompId", { required: "Insurance Company ID is required" })}
                />
                {profileErrors.insuranceCompId && <p className="text-danger">{profileErrors.insuranceCompId.message}</p>}
              </Form.Group>
            )}
            <Form.Group controlId="currentPwd" className="mt-3">
              <Form.Label>
                {userInfo.role === 'Hospital'
                  ? 'Current Hospital Password'
                  : userInfo.role === 'Patient'
                  ? 'Current Patient Password'
                  : 'Current Insurance Company Password'}
              </Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter current password"
                {...registerProfile("currentPwd", { required: "Current password is required" })}
              />
              {profileErrors.currentPwd && <p className="text-danger">{profileErrors.currentPwd.message}</p>}
            </Form.Group>
            <Button variant="primary" type="submit" className="mt-3">
              Save Changes
            </Button>
          </Form>
        </Modal.Body>
      </Modal>

      {/* Update Password Modal */}
      <Modal show={showPasswordModal} onHide={() => setShowPasswordModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Password</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handlePasswordSubmit(onPasswordSubmit)}>
            <Form.Group controlId="oldPassword">
              <Form.Label>Old Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter old password"
                {...registerPassword("oldPassword", { required: "Old password is required" })}
              />
              {passwordErrors.oldPassword && <p className="text-danger">{passwordErrors.oldPassword.message}</p>}
            </Form.Group>
            <Form.Group controlId="newPassword" className="mt-3">
              <Form.Label>New Password</Form.Label>
              <Form.Control
                type="password"
                placeholder="Enter new password"
                {...registerPassword("newPassword", { 
                  required: "New password is required", 
                  minLength: { value: 6, message: "New password must be at least 6 characters" } 
                })}
              />
              {passwordErrors.newPassword && <p className="text-danger">{passwordErrors.newPassword.message}</p>}
            </Form.Group>
            <Button variant="primary" type="submit" className="mt-3">
              Save Changes
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </>
  );
};

export default HomePage;


