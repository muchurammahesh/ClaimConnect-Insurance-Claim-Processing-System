// src/components/Features.js
import React from 'react';
import { FaHospitalAlt, FaUserCheck, FaPaperPlane } from 'react-icons/fa';

const Features = () => {
  return (
    <section id="features" className="py-5">
      <div className="container">
        <h2 className="text-center mb-5">Our Features</h2>
        <div className="row">
          <div className="col-md-4 text-center">
            <FaHospitalAlt size={50} className="mb-4 text-primary" />
            <h4>Hospital Initiated Claims</h4>
            <p>Hospitals can generate claims on behalf of patients, reducing paperwork and administrative load.</p>
          </div>
          <div className="col-md-4 text-center">
            <FaUserCheck size={50} className="mb-4 text-primary" />
            <h4>Patient Approval</h4>
            <p>Patients approve claims digitally, ensuring transparency and security in the process.</p>
          </div>
          <div className="col-md-4 text-center">
            <FaPaperPlane size={50} className="mb-4 text-primary" />
            <h4>Direct Submission</h4>
            <p>Claims are sent directly to insurance companies, expediting the reimbursement process.</p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Features;
