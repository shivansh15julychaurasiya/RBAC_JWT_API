import React from 'react';
import { Button } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { Menu } from 'lucide-react'; // icon (lucide-react comes with Vite setup)

export default function Topbar({ toggleSidebar }) {
    
  const navigate = useNavigate();


  const handleLogout = () => {
    // clear auth or token here
    navigate('/');
  };

  return (
    <div className="topbar d-flex justify-content-between align-items-center px-4 py-2 shadow-sm">
      <div className="d-flex align-items-center">
        <Button color="light" className="me-3 toggle-btn" onClick={toggleSidebar}>
          <Menu size={20} />
        </Button>
        <h5 className="m-0">High Court DMS</h5>
      </div>

      <Button color="danger" size="sm" onClick={handleLogout}>
        Logout
      </Button>
    </div>
  );
}
