// src/components/SignUp.js
import React, { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { Container, Form, Button } from 'react-bootstrap';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useNavigate, Link } from 'react-router-dom';
import { hospitalInstance, patientInstance, insuranceInstance } from '../../utils/axiosInstances';

const SignUp = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: {
      role: '',
      hospitalEmail: '',
      hospitalName: '',
      hospitalPwd: '',
      patientEmail: '',
      patientName: '',
      patientPassword: '',
      insuranceCompId: '',
      insuranceCompEmail: '',
      insuranceCompName: '',
      insuranceCompPwd: '',
    },
  });

  const navigate = useNavigate();
  const role = watch("role");
  const [insuranceCompanies, setInsuranceCompanies] = React.useState([]);

  // If role is Patient, fetch insurance companies using insuranceInstance
  useEffect(() => {
    if (role === 'Patient') {
      const fetchInsuranceCompanies = async () => {
        try {
          const response = await axios.get('http://localhost:3434/api/insuranceComp/getAllICs');
          if (response.data.status === 'OK') {
            setInsuranceCompanies(response.data.data);
          } else {
            console.error('Failed to fetch insurance companies:', response.data.msg);
          }
        } catch (error) {
          console.error('Error fetching insurance companies:', error);
        }
      };
      fetchInsuranceCompanies();
    }
  }, [role]);

  // onSubmit handles registration using the appropriate Axios instance
  const onSubmit = async (data) => {
    let url = 'http://localhost:3434/api/';
    let endpoint = 'signup';
    let payload = {};

    if (data.role === 'Hospital') {
      localStorage.removeItem("Hospital");
      url += 'hospital';
      payload = {
        hospitalEmail: data.hospitalEmail,
        hospitalName: data.hospitalName,
        hospitalPwd: data.hospitalPwd,
      };
    } else if (data.role === 'Patient') {
      localStorage.removeItem("Patient");
      url += 'patient';
      payload = {
        patientEmail: data.patientEmail,
        patientName: data.patientName,
        patientPassword: data.patientPassword,
        insuranceCompId: data.insuranceCompId,
      };
    } else if (data.role === 'InsuranceCompany') {
      localStorage.removeItem("InsuranceCompany");
      url += 'insuranceComp';
      payload = {
        insuranceCompEmail: data.insuranceCompEmail,
        insuranceCompName: data.insuranceCompName,
        insuranceCompPwd: data.insuranceCompPwd,
      };
    } else {
      toast.error("Please select a role.");
      return;
    }

    try {
      const response = await axios.post(url+'/'+endpoint, payload);
      if (response.data.status === "CREATED") {
        toast.success(response.data.msg);
        // Redirect to the sign-in page after a delay
        setTimeout(() => {
          navigate("/signin");
        }, 5000);
      } else {
        toast.error(response.data.msg || "Registration Failed");
      }
    } catch (error) {
      if (error.response && error.response.data && error.response.data.msg) {
        toast.error(error.response.data.msg);
      } else {
        toast.error("An error occurred during registration.");
      }
      console.error("Registration error:", error);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <ToastContainer position="top-center" />
      <div className="w-50 p-4 border rounded shadow-sm">
        <h2 className="text-center mb-4">Sign Up</h2>
        <Form onSubmit={handleSubmit(onSubmit)}>
          {/* Role Selection */}
          <Form.Group controlId="formRole" className="mb-3">
            <Form.Label>Select Role</Form.Label>
            <Form.Select {...register("role", { required: "Role is required" })}>
              <option value="">-- Select Role --</option>
              <option value="Hospital">Hospital</option>
              <option value="Patient">Patient</option>
              <option value="InsuranceCompany">Insurance Company</option>
            </Form.Select>
            {errors.role && <p className="text-danger">{errors.role.message}</p>}
          </Form.Group>

          {/* Conditional Fields for Hospital */}
          {role === 'Hospital' && (
            <>
              <Form.Group controlId="hospitalEmail" className="mb-3">
                <Form.Label>Hospital Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter hospital email"
                  {...register("hospitalEmail", {
                    required: "Hospital email is required",
                    pattern: { value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/i, message: "Invalid email address" },
                  })}
                />
                {errors.hospitalEmail && <p className="text-danger">{errors.hospitalEmail.message}</p>}
              </Form.Group>
              <Form.Group controlId="hospitalName" className="mb-3">
                <Form.Label>Hospital Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter hospital name"
                  {...register("hospitalName", { required: "Hospital name is required" })}
                />
                {errors.hospitalName && <p className="text-danger">{errors.hospitalName.message}</p>}
              </Form.Group>
              <Form.Group controlId="hospitalPwd" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("hospitalPwd", {
                    required: "Password is required",
                    minLength: { value: 8, message: "Password must be at least 8 characters" },
                  })}
                />
                {errors.hospitalPwd && <p className="text-danger">{errors.hospitalPwd.message}</p>}
              </Form.Group>
            </>
          )}

          {/* Conditional Fields for Patient */}
          {role === 'Patient' && (
            <>
              {/* Insurance Company selection (fetched via axios) */}
              <Form.Group controlId="insuranceCompId" className="mb-3">
                <Form.Label>Insurance Company</Form.Label>
                <Form.Select
                  {...register("insuranceCompId", { required: "Insurance Company is required" })}
                >
                  <option value="">-- Select Insurance Company --</option>
                  {insuranceCompanies.map((company) => (
                    <option key={company.insuranceCompId} value={company.insuranceCompId}>
                      {company.insuranceCompName}
                    </option>
                  ))}
                </Form.Select>
                {errors.insuranceCompId && <p className="text-danger">{errors.insuranceCompId.message}</p>}
              </Form.Group>
              <Form.Group controlId="patientEmail" className="mb-3">
                <Form.Label>Patient Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter patient email"
                  {...register("patientEmail", {
                    required: "Patient email is required",
                    pattern: { value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/i, message: "Invalid email address" },
                  })}
                />
                {errors.patientEmail && <p className="text-danger">{errors.patientEmail.message}</p>}
              </Form.Group>
              <Form.Group controlId="patientName" className="mb-3">
                <Form.Label>Patient Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter patient name"
                  {...register("patientName", { required: "Patient name is required" })}
                />
                {errors.patientName && <p className="text-danger">{errors.patientName.message}</p>}
              </Form.Group>
              <Form.Group controlId="patientPassword" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("patientPassword", {
                    required: "Password is required",
                    minLength: { value: 8, message: "Password must be at least 8 characters" },
                  })}
                />
                {errors.patientPassword && <p className="text-danger">{errors.patientPassword.message}</p>}
              </Form.Group>
            </>
          )}

          {/* Conditional Fields for InsuranceCompany */}
          {role === 'InsuranceCompany' && (
            <>
              <Form.Group controlId="insuranceCompEmail" className="mb-3">
                <Form.Label>Insurance Company Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter insurance company email"
                  {...register("insuranceCompEmail", {
                    required: "Email is required",
                    pattern: { value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/i, message: "Invalid email address" }
                  })}
                />
                {errors.insuranceCompEmail && <p className="text-danger">{errors.insuranceCompEmail.message}</p>}
              </Form.Group>
              <Form.Group controlId="insuranceCompName" className="mb-3">
                <Form.Label>Insurance Company Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter insurance company name"
                  {...register("insuranceCompName", { required: "Insurance company name is required" })}
                />
                {errors.insuranceCompName && <p className="text-danger">{errors.insuranceCompName.message}</p>}
              </Form.Group>
              <Form.Group controlId="insuranceCompPwd" className="mb-3">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register("insuranceCompPwd", {
                    required: "Password is required",
                    minLength: { value: 8, message: "Password must be at least 8 characters" }
                  })}
                />
                {errors.insuranceCompPwd && <p className="text-danger">{errors.insuranceCompPwd.message}</p>}
              </Form.Group>
            </>
          )}

          {/* Submit Button */}
          <Button variant="primary" type="submit" className="w-100 mb-3">
            Sign Up
          </Button>

          {/* Link to Sign In */}
          <div className="text-center">
            Already have an account? <Link to="/signin">Sign In</Link>
          </div>
        </Form>
      </div>
    </Container>
  );
};

export default SignUp;
