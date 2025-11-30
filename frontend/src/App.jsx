import React, { useEffect, useState } from 'react';

const API_URL = '/api';

const initialItem = { name: '', description: '', quantity: 0 };

function App() {
  const [token, setToken] = useState('');
  const [username, setUsername] = useState('');
  const [authForm, setAuthForm] = useState({ username: '', password: '' });
  const [items, setItems] = useState([]);
  const [itemForm, setItemForm] = useState(initialItem);
  const [status, setStatus] = useState('');

  useEffect(() => {
    fetchItems();
  }, []);

  async function fetchItems() {
    try {
      const response = await fetch(`${API_URL}/items/public`);
      const data = await response.json();
      setItems(data);
    } catch (error) {
      setStatus('Unable to load items.');
    }
  }

  async function authenticate(path) {
    setStatus('');
    try {
      const response = await fetch(`${API_URL}/auth/${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(authForm)
      });
      if (!response.ok) {
        throw new Error('Authentication failed');
      }
      const data = await response.json();
      setToken(data.token);
      setUsername(authForm.username);
      setAuthForm({ username: '', password: '' });
    } catch (error) {
      setStatus(error.message);
    }
  }

  async function saveItem(event) {
    event.preventDefault();
    if (!token) {
      setStatus('Please log in to manage items');
      return;
    }
    const method = itemForm.id ? 'PUT' : 'POST';
    const url = itemForm.id ? `${API_URL}/items/${itemForm.id}` : `${API_URL}/items`;
    try {
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          name: itemForm.name,
          description: itemForm.description,
          quantity: Number(itemForm.quantity)
        })
      });
      if (!response.ok) throw new Error('Unable to save item');
      await fetchItems();
      setItemForm(initialItem);
      setStatus('Item saved');
    } catch (error) {
      setStatus(error.message);
    }
  }

  async function deleteItem(id) {
    if (!token) {
      setStatus('Please log in to manage items');
      return;
    }
    try {
      const response = await fetch(`${API_URL}/items/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (!response.ok) throw new Error('Unable to delete item');
      fetchItems();
    } catch (error) {
      setStatus(error.message);
    }
  }

  return (
    <div className="container">
      <header>
        <h1>Warehouse Dashboard</h1>
        {username && <p>Welcome, {username}</p>}
      </header>

      <section className="auth">
        <h2>Login or Register</h2>
        <div className="auth-forms">
          <input
            type="text"
            placeholder="Username"
            value={authForm.username}
            onChange={(e) => setAuthForm({ ...authForm, username: e.target.value })}
          />
          <input
            type="password"
            placeholder="Password"
            value={authForm.password}
            onChange={(e) => setAuthForm({ ...authForm, password: e.target.value })}
          />
          <div className="auth-actions">
            <button onClick={() => authenticate('login')}>Login</button>
            <button onClick={() => authenticate('register')}>Register</button>
          </div>
        </div>
      </section>

      <section className="items">
        <h2>Items</h2>
        <div className="item-list">
          {items.map((item) => (
            <div key={item.id} className="item-card">
              <div className="item-header">
                <h3>{item.name}</h3>
                <span className="badge">Qty: {item.quantity}</span>
              </div>
              <p>{item.description}</p>
              {token && (
                <div className="item-actions">
                  <button onClick={() => setItemForm(item)}>Edit</button>
                  <button className="danger" onClick={() => deleteItem(item.id)}>
                    Delete
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      </section>

      <section className="item-form">
        <h2>{itemForm.id ? 'Edit Item' : 'Add Item'}</h2>
        <form onSubmit={saveItem}>
          <input
            type="text"
            placeholder="Name"
            value={itemForm.name}
            onChange={(e) => setItemForm({ ...itemForm, name: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Description"
            value={itemForm.description}
            onChange={(e) => setItemForm({ ...itemForm, description: e.target.value })}
          />
          <input
            type="number"
            min="0"
            placeholder="Quantity"
            value={itemForm.quantity}
            onChange={(e) => setItemForm({ ...itemForm, quantity: e.target.value })}
            required
          />
          <button type="submit">Save</button>
          {itemForm.id && (
            <button type="button" onClick={() => setItemForm(initialItem)}>
              Cancel
            </button>
          )}
        </form>
      </section>

      {status && <div className="status">{status}</div>}
    </div>
  );
}

export default App;
