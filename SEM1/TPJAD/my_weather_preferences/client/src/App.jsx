import { useState } from 'react';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import './App.css';

export default function App() {
  // const [user, setUser] = useState(null);
  // const [page, setPage] = useState('login');

  return (
    <div className="app">
      {/* {user ? (
        <div className="welcome">
          <h1>Welcome, {user.username}!</h1>
          <button onClick={() => setUser(null)}>Logout</button>
        </div>
      ) : page === 'login' ? (
        <LoginPage setUser={setUser} setPage={setPage} />
      ) : (
        <RegisterPage setUser={setUser} setPage={setPage} />
      )} */}
    </div>
  );
}
