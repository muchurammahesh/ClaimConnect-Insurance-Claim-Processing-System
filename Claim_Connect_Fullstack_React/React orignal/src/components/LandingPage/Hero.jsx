// src/components/Hero.js
import React from 'react';
import Navigationbar from './Navigationbar';

const Hero = () => {
  return (
    <div className='pt-5' style={{backgroundColor: "#edf3f9"}}>
    <Navigationbar/>
    <section id="hero" className="d-flex align-items-center" style={{ minHeight: '80vh'}}>
      <div className="container text-center text-white" >
        <h1  style={{color: "#2d465e"}}>Streamline and Process</h1>
        <h2  style={{color: "#0d83fd"}}>Health Insurance Claims</h2>
        <p style={{color: "#111", letterSpacing: '0px'}} className="lead fw-normal mt-4" >Empowering hospitals to manage patient claims efficiently with patient-approved submissions.</p>
        <a href="#features" className="btn btn-outline-light btn-lg mt-4">Learn More</a>
      </div>
    </section>
    </div>
  );
};

export default Hero;

