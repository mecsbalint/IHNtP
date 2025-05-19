import { createBrowserRouter } from "react-router-dom";
import Layout from '../pages/Layout/Layout';
import HomePage from '../pages/HomePage';
import GamePage from '../pages/GamePage';
import RegistrationPage from '../pages/RegistrationPage';
import LoginPage from '../pages/LoginPage';
import WishlistPage from '../pages/WishlistPage';
import BacklogPage from '../pages/BacklogPage';
import AddGamePage from "../pages/AddGamePage";

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
        element: <GamePage />,
      },
      {
        path: "/registration",
        element: <RegistrationPage />,
      },
      {
        path: "/login",
        element: <LoginPage />,
      },
      {
        path: "/wishlist",
        element: <WishlistPage />,
      },
      {
        path: "/backlog",
        element: <BacklogPage />,
      },
      {
        path: "/games/add",
        element: <AddGamePage />
      }
    ],
  },
]);

export default router;
