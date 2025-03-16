import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
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
      {
        path: "/user/:user_name/wishlist",
        element: <UserWishlist />
      },
      {
        path: "/user/:user_name/played_games",
        element: <UserPlayedGames />
      },
    ],
  },
]);

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router}/>
  </StrictMode>,
)
