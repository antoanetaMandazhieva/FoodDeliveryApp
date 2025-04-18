import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import ErrorBoundary from './ErrorBoundary.jsx'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import Restaurants from './components/restaurants/Restaurants.jsx'
import RestaurantMenu from './components/menu/RestaurantMenu.jsx'
import SignUp from './components/forms/SignUp.jsx'
import SignIn from './components/forms/SignIn.jsx'
import OrderPage from './components/userOrder/OrderPage.jsx'
import ProfilePage from './userProfile/ProfilePage.jsx'

const router = createBrowserRouter([{
    path: '/',
    element: <App />
}, {
    path: '/restaurants',
    element: <Restaurants />
}, {
    path: '/restaurants/:restaurantId',
    element: <RestaurantMenu />
}, {
    path: '/signup',
    element: <SignUp />
}, {
    path: '/signin',
    element: <SignIn />
}, {
    path: '/order/:userId',
    element: <OrderPage />
}, {
    path: '/profile/:userId',
    element: <ProfilePage />
}])

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ErrorBoundary>
            <RouterProvider router={router} />
        </ErrorBoundary>
    </StrictMode>,
)
