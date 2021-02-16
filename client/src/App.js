import 'bootswatch/dist/slate/bootstrap.min.css';
import logo from './logo.svg';
import './App.css';
import React, {useState} from "react";
import {BrowserRouter as Router, Route, Link} from "react-router-dom";
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem,
    NavbarText
} from 'reactstrap';
import {ListFilesPage} from './filemanagement/list-files';
import {UploadFilePage} from './filemanagement/upload-file';

function App() {
    const [isOpen, setIsOpen] = useState(false);
    const toggle = () => setIsOpen(!isOpen);
  return (
    <div className="App">
      <Router>
      <Navbar expand="md">
        <NavbarBrand href="/">File manager</NavbarBrand>
          <NavbarToggler onClick={toggle}/>
          <Collapse isOpen={isOpen} navbar>
              <Nav>
                  <NavItem>
                      <NavLink tag={Link} to="/list-files">See files</NavLink>
                  </NavItem>
                  <NavItem>
                      <NavLink tag={Link} to="/upload-file">Upload a file</NavLink>
                  </NavItem>
              </Nav>
          </Collapse>
      </Navbar>
          <Route path="/list-files">
              <ListFilesPage />
          </Route>
          <Route path="/upload-file">
              <UploadFilePage />
          </Route>
      </Router>
    </div>
  );
}

export default App;
