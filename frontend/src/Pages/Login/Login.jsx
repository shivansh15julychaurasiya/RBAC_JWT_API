import React, { useState } from 'react';
import { Button, Form, FormGroup, Input, Label, Card, CardBody, Alert } from 'reactstrap';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const navigate = useNavigate();

  // form state
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });

  // error message state
  const [error, setError] = useState('');

  // handle input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  // validation function
  const validate = () => {
    const { username, password } = formData;

    if (!username || !password) {
      setError('Please fill in all fields.');
      return false;
    }

   

    // password length check
    if (password.length < 6) {
      setError('Password must be at least 6 characters long.');
      return false;
    }

    setError('');
    return true;
  };

  // handle form submit
  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      console.log('Form Data:', formData);
      // later you can replace this with API call
      navigate('/dashboard');
    }
  };

  return (
    <div className="login-bg d-flex justify-content-center align-items-center">
      <Card className="card-custom p-4" style={{ width: '400px' }}>
        <CardBody>
          <h3 className="text-center mb-4">Login</h3>

          {error && <Alert color="danger">{error}</Alert>}

          <Form onSubmit={handleSubmit}>
            <FormGroup>
              <Label>Username:</Label>
              <Input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Enter username"
              />
            </FormGroup>
            <FormGroup>
              <Label>Password:</Label>
              <Input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Enter password"
              />
            </FormGroup>
            <Button color="primary" block className="mt-3">
              Login
            </Button>
          </Form>
        </CardBody>
      </Card>
    </div>
  );
}
