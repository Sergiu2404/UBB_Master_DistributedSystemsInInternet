import { useState } from 'react';
import './Auth.css';

export default function LoginPage({ setUser, setPage }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    if (!username || !password) return alert('Username and password cannot be empty');

    const stored = JSON.parse(localStorage.getItem('users') || '[]');
    const match = stored.find(u => u.username === username && u.password === password);

    if (match) setUser(match);
    else alert('Invalid username or password');
  };

  return (
    <div className="auth-container">
      <h2>Login</h2>
      <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} />
      <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
      <button onClick={handleLogin}>Login</button>
      <p>No account? <span onClick={() => setPage('register')}>Register</span></p>
    </div>
  );
}
