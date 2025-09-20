// src/components/SideNavBar.js
import React from 'react';
import { NavLink, Link, useNavigate } from 'react-router-dom'
import { Button } from 'react-bootstrap';
const SideNavBar = ({ title, navLinks, userName, token }) => {

  const navigate = useNavigate();
  let profileDashBoard = '';
  if (token === "Hospital") profileDashBoard = '/hospital/dashboard';
  else if (token === "Patient") profileDashBoard = '/patient/dashboard';
  else if (token === "InsuranceCompany") profileDashBoard = '/insurance/dashboard';

  const handleLogout = () => {
    alert("logout called")
    // console.log(localStorage.getItem(token));
    
    if(localStorage.getItem(token)) {
      localStorage.removeItem(token);
      navigate("/");
    }
    
  }
  return (
    <div >
      <div >
        <div className='d-flex flex-column align-items-start justify-content-between text-white bg-dark min-vh-100  px-3 pt-2'>
          <div className='d-flex flex-column align-items-start '>
            {/* Title/Brand */}
            <a
              className='text-decoration-none text-white d-none d-sm-inline d-flex align-items-center ms-3 mt-3'
            >
              <i className='fs-4 bi bi-speedometer'></i>
              <span className='ms-1 fs-5 d-none d-sm-inline'>{title}</span>
            </a>
            <hr className='text-secondary d-none d-sm-block' />

            {/* Navigation Links */}
            <ul className='nav nav-pills flex-column mt-3 mt-sm-0'>
              {navLinks.map((link, index) => (
                <li
                  className='nav-item text-white fs-5 my-1 py-2 py-sm-0'
                  key={index}
                >

                  <NavLink
                    to={link.href}
                    className='nav-link text-white fs-5'
                    aria-current='page'
                  >
                    <i className={`${link.icon} fs-6`}></i>
                    <span className='fs-6 ms-3 d-none d-sm-inline'>{link.label}</span>
                  </NavLink>
                </li>
              ))}
            </ul>
          </div>

          {/* User Profile Section */}
          <div className='dropdown open'>
            <a
              className='text-decoration-none text-white dropdown-toggle p-3'
              type='button'
              id='triggerId'
              data-bs-toggle='dropdown'
              aria-haspopup='true'
              aria-expanded='false'
            >
              <i className='fa-solid fa-user'></i>
              <span className='ms-2 d-none d-sm-inline'>{userName}</span>
            </a>
            <div className='dropdown-menu' aria-labelledby='triggerId'>
              <Link className='dropdown-item' to={profileDashBoard}>
                Profile
              </Link>
              {/* <a className='dropdown-item' href='#'>
                Settings
              </a>  */}
              <hr className='dropdown-divider' />
              <Button className='dropdown-item' onClick={handleLogout}>
                Logout
              </Button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SideNavBar;
