import React, { useState, useEffect } from 'react';
import { Card, CardBody, CardTitle, CardText, Row, Col } from 'reactstrap';
import Sidebar from '../../component/Sidebar';
import Topbar from '../../component/Topbar';

export default function Dashboard() {
  const [collapsed, setCollapsed] = useState(false);
  const [isMobileOpen, setIsMobileOpen] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth < 992) {
        setCollapsed(true);
      } else {
        setCollapsed(false);
        setIsMobileOpen(false);
      }
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const toggleSidebar = () => {
    if (window.innerWidth < 992) {
      setIsMobileOpen(!isMobileOpen);
    } else {
      setCollapsed(!collapsed);
    }
  };

  return (
    <div className="dashboard-container d-flex">
      <Sidebar collapsed={collapsed} isMobileOpen={isMobileOpen} setIsMobileOpen={setIsMobileOpen} />
      <div className="dashboard-main flex-grow-1">
        <Topbar toggleSidebar={toggleSidebar} />
        {/* Backdrop for mobile sidebar */}
        {isMobileOpen && <div className="sidebar-backdrop" onClick={() => setIsMobileOpen(false)} />}
        <div className="dashboard-content p-4">
          <h3 className="mb-4">Welcome to Dashboard 👋</h3>

          <Row>
            <Col md="4">
              <Card className="dash-card shadow-sm">
                <CardBody>
                  <CardTitle tag="h5">Total Users</CardTitle>
                  <CardText className="display-6 fw-bold">1,245</CardText>
                </CardBody>
              </Card>
            </Col>
            <Col md="4">
              <Card className="dash-card shadow-sm">
                <CardBody>
                  <CardTitle tag="h5">Active Cases</CardTitle>
                  <CardText className="display-6 fw-bold">320</CardText>
                </CardBody>
              </Card>
            </Col>
            <Col md="4">
              <Card className="dash-card shadow-sm">
                <CardBody>
                  <CardTitle tag="h5">Pending Files</CardTitle>
                  <CardText className="display-6 fw-bold">58</CardText>
                </CardBody>
              </Card>
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
}
