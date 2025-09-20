// src/components/DetailsPopup.js
import React from 'react';
import { Modal, Button } from 'react-bootstrap';

const DetailsPopup = ({ show, onHide, hospitalData, patientData, insuranceCompData }) => {
  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>Details</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {hospitalData && (
          <div>
            <h5>Hospital Details</h5>
            <p><strong>Name:</strong> {hospitalData.hospitalName}</p>
            <p><strong>Email:</strong> {hospitalData.hospitalEmail}</p>
          </div>
        )}
        {patientData && (
          <div>
            <h5>Patient Details</h5>
            <p><strong>Name:</strong> {patientData.patientName}</p>
            <p><strong>Email:</strong> {patientData.patientEmail}</p>
          </div>
        )}
        {insuranceCompData && (
          <div>
            <h5>Insurance Company Details</h5>
            <p><strong>Name:</strong> {insuranceCompData.insuranceCompName}</p>
            <p><strong>Email:</strong> {insuranceCompData.insuranceCompEmail}</p>
          </div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>Close</Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DetailsPopup;
