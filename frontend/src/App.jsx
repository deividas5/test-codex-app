import React, { useEffect, useState } from 'react';
import {
  Alert,
  AppBar,
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Chip,
  Container,
  CssBaseline,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Divider,
  Grid,
  Snackbar,
  Stack,
  TextField,
  Toolbar,
  Typography
} from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import Inventory2RoundedIcon from '@mui/icons-material/Inventory2Rounded';
import LoginRoundedIcon from '@mui/icons-material/LoginRounded';
import LogoutRoundedIcon from '@mui/icons-material/LogoutRounded';
import AddShoppingCartRoundedIcon from '@mui/icons-material/AddShoppingCartRounded';

const API_URL = '/api';
const initialItem = { id: null, name: '', description: '', quantity: 0 };

const theme = createTheme({
  palette: {
    primary: {
      main: '#1565c0'
    },
    background: {
      default: '#f1f5f9'
    }
  },
  shape: { borderRadius: 14 }
});

function App() {
  const [token, setToken] = useState('');
  const [username, setUsername] = useState('');
  const [loginForm, setLoginForm] = useState({ username: '', password: '' });
  const [registerForm, setRegisterForm] = useState({ username: '', password: '' });
  const [items, setItems] = useState([]);
  const [itemForm, setItemForm] = useState(initialItem);
  const [status, setStatus] = useState({ message: '', severity: 'info' });
  const [showLogin, setShowLogin] = useState(false);

  useEffect(() => {
    fetchItems();
  }, []);

  async function fetchItems() {
    try {
      const response = await fetch(`${API_URL}/items/public`);
      const data = await response.json();
      setItems(data);
    } catch (error) {
      setStatus({ message: 'Unable to load items.', severity: 'error' });
    }
  }

  async function handleAuth(path, form, onSuccess) {
    try {
      const response = await fetch(`${API_URL}/auth/${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form)
      });
      if (!response.ok) {
        throw new Error('Authentication failed');
      }
      const data = await response.json();
      setToken(data.token);
      setUsername(form.username);
      onSuccess();
      setStatus({ message: `${path === 'login' ? 'Logged in' : 'Registered'} successfully`, severity: 'success' });
    } catch (error) {
      setStatus({ message: error.message, severity: 'error' });
    }
  }

  async function saveItem(event) {
    event.preventDefault();
    if (!token) {
      setStatus({ message: 'Please log in to manage items', severity: 'warning' });
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
      setStatus({ message: 'Item saved', severity: 'success' });
    } catch (error) {
      setStatus({ message: error.message, severity: 'error' });
    }
  }

  async function deleteItem(id) {
    if (!token) {
      setStatus({ message: 'Please log in to manage items', severity: 'warning' });
      return;
    }
    try {
      const response = await fetch(`${API_URL}/items/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });
      if (!response.ok) throw new Error('Unable to delete item');
      fetchItems();
      setStatus({ message: 'Item deleted', severity: 'info' });
    } catch (error) {
      setStatus({ message: error.message, severity: 'error' });
    }
  }

  function handleLogout() {
    setToken('');
    setUsername('');
    setItemForm(initialItem);
    setStatus({ message: 'Logged out', severity: 'info' });
  }

  const openLoginDialog = () => setShowLogin(true);
  const closeLoginDialog = () => setShowLogin(false);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AppBar position="sticky" elevation={0} color="transparent" sx={{ backdropFilter: 'blur(8px)' }}>
        <Toolbar>
          <Inventory2RoundedIcon color="primary" sx={{ mr: 1 }} />
          <Typography variant="h6" sx={{ flexGrow: 1 }} color="text.primary">
            Warehouse Dashboard
          </Typography>
          {username && (
            <Chip label={`Hello, ${username}`} color="primary" variant="outlined" sx={{ mr: 2 }} />
          )}
          {token ? (
            <Button startIcon={<LogoutRoundedIcon />} color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          ) : (
            <Button startIcon={<LoginRoundedIcon />} color="primary" variant="contained" onClick={openLoginDialog}>
              Login
            </Button>
          )}
        </Toolbar>
      </AppBar>

      <Box sx={{ py: 4 }}>
        <Container maxWidth="lg">
          <Grid container spacing={3} alignItems="stretch">
            <Grid item xs={12} md={7}>
              <Card elevation={0} sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h4" gutterBottom>
                    Manage your warehouse with confidence
                  </Typography>
                  <Typography color="text.secondary" paragraph>
                    Track public inventory, add new items, and keep quantities up to date. Use the login window to
                    access secure actions or register a new account to start managing the stock.
                  </Typography>
                  <Stack direction="row" spacing={2} flexWrap="wrap">
                    <Button variant="contained" startIcon={<AddShoppingCartRoundedIcon />} onClick={openLoginDialog}>
                      Open login window
                    </Button>
                    <Button variant="outlined" onClick={fetchItems}>Refresh items</Button>
                  </Stack>
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12} md={5}>
              <Card elevation={1} sx={{ height: '100%' }}>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Quick register
                  </Typography>
                  <Stack spacing={2} component="form" onSubmit={(e) => e.preventDefault()}>
                    <TextField
                      label="Username"
                      fullWidth
                      value={registerForm.username}
                      onChange={(e) => setRegisterForm({ ...registerForm, username: e.target.value })}
                    />
                    <TextField
                      label="Password"
                      type="password"
                      fullWidth
                      value={registerForm.password}
                      onChange={(e) => setRegisterForm({ ...registerForm, password: e.target.value })}
                    />
                  </Stack>
                </CardContent>
                <CardActions sx={{ px: 3, pb: 3 }}>
                  <Button
                    fullWidth
                    variant="contained"
                    onClick={() => handleAuth('register', registerForm, () => setRegisterForm({ username: '', password: '' }))}
                  >
                    Register and get token
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          </Grid>

          <Box mt={4}>
            <Grid container spacing={3}>
              <Grid item xs={12} md={7}>
                <Card elevation={1}>
                  <CardContent>
                    <Stack direction="row" justifyContent="space-between" alignItems="center" mb={2}>
                      <Typography variant="h6">Inventory</Typography>
                      <Chip label={`${items.length} items`} color="primary" variant="outlined" />
                    </Stack>
                    <Grid container spacing={2}>
                      {items.map((item) => (
                        <Grid item xs={12} sm={6} key={item.id}>
                          <Card variant="outlined">
                            <CardContent>
                              <Stack direction="row" justifyContent="space-between" alignItems="center" mb={1}>
                                <Typography variant="subtitle1" fontWeight={600}>
                                  {item.name}
                                </Typography>
                                <Chip label={`Qty: ${item.quantity}`} color="primary" size="small" />
                              </Stack>
                              <Typography color="text.secondary" variant="body2" mb={2}>
                                {item.description || 'No description'}
                              </Typography>
                              {token && (
                                <Stack direction="row" spacing={1}>
                                  <Button size="small" variant="text" onClick={() => setItemForm(item)}>
                                    Edit
                                  </Button>
                                  <Button size="small" color="error" onClick={() => deleteItem(item.id)}>
                                    Delete
                                  </Button>
                                </Stack>
                              )}
                            </CardContent>
                          </Card>
                        </Grid>
                      ))}
                      {items.length === 0 && (
                        <Grid item xs={12}>
                          <Typography color="text.secondary">No items available.</Typography>
                        </Grid>
                      )}
                    </Grid>
                  </CardContent>
                </Card>
              </Grid>

              <Grid item xs={12} md={5}>
                <Card elevation={1}>
                  <CardContent>
                    <Typography variant="h6" gutterBottom>
                      {itemForm.id ? 'Edit Item' : 'Add Item'}
                    </Typography>
                    <Box component="form" onSubmit={saveItem} noValidate>
                      <Stack spacing={2}>
                        <TextField
                          label="Name"
                          value={itemForm.name}
                          onChange={(e) => setItemForm({ ...itemForm, name: e.target.value })}
                          required
                        />
                        <TextField
                          label="Description"
                          value={itemForm.description}
                          onChange={(e) => setItemForm({ ...itemForm, description: e.target.value })}
                        />
                        <TextField
                          label="Quantity"
                          type="number"
                          inputProps={{ min: 0 }}
                          value={itemForm.quantity}
                          onChange={(e) => setItemForm({ ...itemForm, quantity: e.target.value })}
                          required
                        />
                        <Stack direction="row" spacing={1}>
                          <Button type="submit" variant="contained" fullWidth>
                            Save
                          </Button>
                          {itemForm.id && (
                            <Button
                              variant="outlined"
                              fullWidth
                              onClick={() => setItemForm(initialItem)}
                            >
                              Cancel
                            </Button>
                          )}
                        </Stack>
                      </Stack>
                    </Box>
                  </CardContent>
                </Card>
              </Grid>
            </Grid>
          </Box>
        </Container>
      </Box>

      <Dialog open={showLogin} onClose={closeLoginDialog} fullWidth maxWidth="sm">
        <DialogTitle>Login</DialogTitle>
        <DialogContent>
          <Stack spacing={2} mt={1}>
            <TextField
              label="Username"
              fullWidth
              value={loginForm.username}
              onChange={(e) => setLoginForm({ ...loginForm, username: e.target.value })}
            />
            <TextField
              label="Password"
              type="password"
              fullWidth
              value={loginForm.password}
              onChange={(e) => setLoginForm({ ...loginForm, password: e.target.value })}
            />
            <Divider />
            <Typography variant="body2" color="text.secondary">
              Use the login window to access protected item actions. Default admin: admin / admin123.
            </Typography>
          </Stack>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={closeLoginDialog}>Close</Button>
          <Button
            variant="contained"
            onClick={() =>
              handleAuth('login', loginForm, () => {
                setLoginForm({ username: '', password: '' });
                closeLoginDialog();
              })
            }
          >
            Login
          </Button>
        </DialogActions>
      </Dialog>

      <Snackbar
        open={!!status.message}
        autoHideDuration={3500}
        onClose={() => setStatus({ message: '', severity: 'info' })}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
      >
        {status.message && (
          <Alert severity={status.severity} variant="filled" onClose={() => setStatus({ message: '', severity: 'info' })}>
            {status.message}
          </Alert>
        )}
      </Snackbar>
    </ThemeProvider>
  );
}

export default App;
