import React from 'react';
import { Button, Card, CardBody } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

export default function NotFound() {
  const navigate = useNavigate();

  return (
    <div className="notfound-bg d-flex justify-content-center align-items-center">
      <Card className="notfound-card text-center p-4 shadow-lg">
        <CardBody>
          <h1 className="notfound-title">404</h1>
          <h4 className="notfound-subtitle">Page Not Found</h4>
          <p className="notfound-text">
            The page you’re looking for doesn’t exist or may have been moved.
          </p>
          <Button color="primary" onClick={() => navigate('/')}>
            Go Back Home
          </Button>
        </CardBody>
      </Card>
      <div className="animated-bg"></div>
    </div>
  );
}
