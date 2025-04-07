import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import ErrorBoundary from './ErrorBoundary.jsx'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'


const router = createBrowserRouter([{
  path: '/',
  element: <App />
}])

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <ErrorBoundary>
        <RouterProvider router={router} />
    </ErrorBoundary>
  </StrictMode>,
)
