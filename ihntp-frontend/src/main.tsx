import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import "./main.css";
import router from './routes/router';
import { AuthContextProvider } from './contexts/AuthContext';

createRoot(document.getElementById('root') as HTMLElement).render(
  <StrictMode>
    <AuthContextProvider>
      <RouterProvider router={router}/>
    </AuthContextProvider>
  </StrictMode>,
)
