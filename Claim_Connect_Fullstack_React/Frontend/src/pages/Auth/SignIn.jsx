// src/components/SignIn.js
import React from 'react';
import { useForm } from 'react-hook-form';
import { Container, Form, Button } from 'react-bootstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

const SignIn = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();
  const navigate = useNavigate();

  // onSubmit handler using react-hook-form
  const onSubmit = async (data) => {
    let apiUrl = '';
    let payload = {};
    const role = data.role;

    switch (role) {
      case 'Hospital':
        apiUrl = 'http://localhost:3434/api/hospital/authenticate';
        payload = {
          hospitalEmail: data.hospitalEmail,
          hospitalPwd: data.hospitalPwd,
        };
        break;
      case 'Patient':
        apiUrl = 'http://localhost:3434/api/patient/authenticate';
        payload = {
          patientEmail: data.patientEmail,
          patientPassword: data.patientPassword,
        };
        break;
      case 'InsuranceCompany':
        apiUrl = 'http://localhost:3434/api/insuranceComp/authenticate';
        payload = {
          insuranceCompEmail: data.insuranceCompEmail,
          insuranceCompPwd: data.insuranceCompPwd,
        };
        break;
      default:
        toast.error('Please select a role.');
        return;
    }

    try {
      const response = await axios.post(apiUrl, payload);
      if (response.data.data != null) {
        toast.success("JWT Token Received");
        setTimeout(() => {
          const token = response.data.data;
          localStorage.setItem(role, token);
          if (role === 'Hospital') {
            navigate('/hospital/dashboard');
          } else if (role === 'Patient') {
            navigate('/patient/dashboard');
          } else if (role === 'InsuranceCompany') {
            navigate('/insurance/dashboard');
          }
        }, 2000);
      } else {
        toast.error(response.data.msg || 'Sign-in failed.');
      }
    } catch (error) {
      if (error.response && error.response.data && error.response.data.msg) {
        toast.error(error.response.data.msg);
      } else {
        toast.error('An error occurred during sign-in.');
      }
      console.error('Sign-in error:', error);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <ToastContainer position="top-center" />
      <div className="w-50 p-4 border rounded shadow-sm">
        <h2 className="text-center mb-4">Sign In</h2>
        <Form onSubmit={handleSubmit(onSubmit)}>
          {/* Role Selection */}
          <Form.Group controlId="formRole" className="mb-3">
            <Form.Label>Select Role</Form.Label>
            <Form.Select
              {...register("role", { required: "Role is required" })}
            >
              <option value="">-- Select Role --</option>
              <option value="Hospital">Hospital</option>
              <option value="Patient">Patient</option>
              <option value="InsuranceCompany">Insurance Company</option>
            </Form.Select>
            {errors.role && (
              <p className="text-danger">{errors.role.message}</p>
            )}
          </Form.Group>

          {/* Conditional Fields for Hospital */}
          {watch("role") === "Hospital" && (
            <>
              <Form.Group controlId="hospitalEmail" className="mb-3">
                <Form.Label>Hospital Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter hospital email"
                  {...register("hospitalEmail", {
                    required: "Hospital email is required",
                    pattern: {
                      value: /^\S+@\S+$/i,
                      message: "Invalid email address",
                    },
                  })}
                />
                {errors.hospitalEmail && (
                  <p className="text-danger">{errors.hospitalEmail.message}</p>
                )}
              </Form.Group>
              <Form.Group controlId="hospitalPwd" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("hospitalPwd", {
                    required: "Password is required",
                    minLength: {
                      value: 6,
                      message: "Password should be at least 6 characters long",
                    },
                  })}
                />
                {errors.hospitalPwd && (
                  <p className="text-danger">{errors.hospitalPwd.message}</p>
                )}
              </Form.Group>
            </>
          )}

          {/* Conditional Fields for Patient */}
          {watch("role") === "Patient" && (
            <>
              <Form.Group controlId="patientEmail" className="mb-3">
                <Form.Label>Patient Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter patient email"
                  {...register("patientEmail", {
                    required: "Patient email is required",
                    pattern: {
                      value: /^\S+@\S+$/i,
                      message: "Invalid email address",
                    },
                  })}
                />
                {errors.patientEmail && (
                  <p className="text-danger">{errors.patientEmail.message}</p>
                )}
              </Form.Group>
              <Form.Group controlId="patientPassword" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("patientPassword", {
                    required: "Password is required",
                    minLength: {
                      value: 6,
                      message: "Password should be at least 6 characters long",
                    },
                  })}
                />
                {errors.patientPassword && (
                  <p className="text-danger">{errors.patientPassword.message}</p>
                )}
              </Form.Group>
            </>
          )}

          {/* Conditional Fields for Insurance Company */}
          {watch("role") === "InsuranceCompany" && (
            <>
              <Form.Group controlId="insuranceCompEmail" className="mb-3">
                <Form.Label>Insurance Company Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter insurance company email"
                  {...register("insuranceCompEmail", {
                    required: "Email is required",
                    pattern: {
                      value: /^\S+@\S+$/i,
                      message: "Invalid email address",
                    },
                  })}
                />
                {errors.insuranceCompEmail && (
                  <p className="text-danger">{errors.insuranceCompEmail.message}</p>
                )}
              </Form.Group>
              <Form.Group controlId="insuranceCompPwd" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("insuranceCompPwd", {
                    required: "Password is required",
                    minLength: {
                      value: 6,
                      message: "Password should be at least 6 characters long",
                    },
                  })}
                />
                {errors.insuranceCompPwd && (
                  <p className="text-danger">{errors.insuranceCompPwd.message}</p>
                )}
              </Form.Group>
            </>
          )}

          <Button variant="primary" type="submit" className="w-100 mb-3">
            Sign In
          </Button>

          <div className="text-center">
            Don't have an account? <Link to="/signup">Sign up</Link>
          </div>
        </Form>
      </div>
    </Container>
  );
};

export default SignIn;
