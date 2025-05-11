import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import "./main.css";
import Layout from './pages/Layout/Layout';
import HomePage from './pages/HomePage';
import GamePage from './pages/GamePage';
import RegistrationPage from './pages/RegistrationPage';
import LoginPage from './pages/LoginPage';

const router = createBrowserRouter([
  {
    path: "/",
    element: <Layout />,
    // errorElement: < />,
    children: [
      {
        path: "/",
        element: <HomePage />,
      },
      {
        path: "/game/:id",
        element: <GamePage />
      },
      {
        path: "/registration",
        element: <RegistrationPage />
      },
      {
        path: "/login",
        element: <LoginPage />
      }
    ],
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
