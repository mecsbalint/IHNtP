import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import "./main.css";
import Layout from './pages/Layout/Layout';
import HomePage from './pages/HomePage';
import UserWishlist from './pages/UserWishlist';
import UserPlayedGames from './pages/UserPlayedGames';

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
    ],
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
