// src/components/Footer.js
import React from 'react';

const Footer = () => {
  return (
    <footer className="bg-light py-4">
      <div className="container text-center">
        <p className="mb-0">&copy; {new Date().getFullYear()} HealthClaimPro. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
