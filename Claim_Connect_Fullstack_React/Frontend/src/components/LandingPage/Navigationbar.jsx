import React from 'react';
import { Navbar, Nav, NavDropdown, Container } from 'react-bootstrap';
import { Link } from 'react-router-dom';
const Navigationbar = () => {
  return (
    <Container>
      <Navbar expand="lg" bg="light" className='rounded-pill px-4' style={{ backgroundColor: "#fff" }}>
        <Container>
          <Navbar.Brand href="#hero">Claim Connect</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" /> {/* React-Bootstrap handles toggling */}
          <Navbar.Collapse id="basic-navbar-nav">
            {/* <Nav className="ms-auto"> */}
            <Nav className="align-items-center ms-auto">
              <Nav.Link href="#hero">Home</Nav.Link>
              <Nav.Link href="#features" >Features</Nav.Link>
              <Nav.Link as={Link} to="/signup" >
                <button className="btn btn-primary">Sign In / Sign Up</button>
              </Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </Container>
  );
};

export default Navigationbar;
