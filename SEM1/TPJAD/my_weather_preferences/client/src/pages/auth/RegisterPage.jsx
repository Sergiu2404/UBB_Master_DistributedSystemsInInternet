import { useState } from 'react';
import "./Auth.css";

export default function Register({ setUser, setPage }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const isValid = (username, password) => username.length > 3 && password.length > 3;

  const handleRegister = () => {
    if (!username || !password) return alert('Username and password cannot be empty');
    if (!isValid(username, password)) return alert('Username and password must be at least 4 characters');

    const stored = JSON.parse(localStorage.getItem('users') || '[]');
    if (stored.find(u => u.username === username)) return alert('Username exists');

    const newUser = { username, password };
    stored.push(newUser);
    localStorage.setItem('users', JSON.stringify(stored));
    setUser(newUser);
  };

  return (
    <div className="auth-container">
      <h2>Register</h2>
      <input placeholder="Username" value={username} onChange={e => setUsername(e.target.value)} />
      <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
      <button onClick={handleRegister}>Register</button>
      <p>Already have an account? <span onClick={() => setPage('login')}>Login</span></p>
    </div>
  );
}
