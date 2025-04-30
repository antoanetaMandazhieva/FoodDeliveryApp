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
import ProfilePage from './components/userProfile/ProfilePage.jsx'
import RestaurantReviewForm from './components/forms/RestaurantReviewForm.jsx'
import SupplierReviewForm from './components/forms/SupplierReviewForm.jsx'
import UserOrdersHistory from './components/userProfile/UserOrdersHistory.jsx'
import EmployeePage from './components/employee/EmployeePage.jsx'
import CreateRestaurant from './components/forms/CreateRestaurant.jsx'
import AddProduct from './components/forms/AddProduct.jsx'
import RemoveProduct from './components/forms/RemoveProduct.jsx'
import EmployeeOrdersDashboard from './components/employee/EmployeeOrdersDashboard.jsx'
import SupplierPage from './components/supplier/SupplierPage.jsx'
import AdminPanel from './components/admin/AdminPanel.jsx'
import ChangeUserRole from './components/forms/ChangeUserRole.jsx'
import SupplierOrdersDashboard from './components/supplier/SupplierOrdersDashboard.jsx'
import SupplierAssignedOrders from './components/supplier/SupplierAssignedOrders.jsx'
import UserTrackOrder from './components/userProfile/UserTrackOrder.jsx'


const router = createBrowserRouter([{
    path: '/',
    element: <App />
}, {
    path: '/restaurants',
    element: <Restaurants />
}, {
    path: '/restaurants/:restaurantName',
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
}, {
    path: '/profile/:userId/restaurant-review',
    element: <RestaurantReviewForm />
}, {
    path: '/profile/:userId/supplier-review',
    element: <SupplierReviewForm />
}, {
    path: '/profile/:userId/orders',
    element: <UserOrdersHistory />
}, {
    path: '/profile/:userId/orders/track/:orderId',
    element: <UserTrackOrder />
}, {
    path: '/profile/employee/:employeeId',
    element: <EmployeePage />
}, {
    path: '/profile/employee/:employeeId/add-product',
    element: <AddProduct />
}, {
    path: '/profile/employee/:employeeId/remove-product',
    element: <RemoveProduct />
}, {
    path: '/profile/employee/:employeeId/create-restaurant',
    element: <CreateRestaurant />
}, {
    path: '/profile/employee/:employeeId/orders-dashboard',
    element: <EmployeeOrdersDashboard />
}, {
    path: '/profile/supplier/:supplierId/orders-dashboard',
    element: <SupplierOrdersDashboard />
}, {
    path: '/profile/supplier/:supplierId',
    element: <SupplierPage />
}, {
    path: '/profile/supplier/:supplierId/orders-assigned',
    element: <SupplierAssignedOrders />
}, {
    path: '/profile/admin/:adminId', 
    element: <AdminPanel />
}, {
    path: '/profile/admin/:adminId/change-role',
    element: <ChangeUserRole />
}])

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <ErrorBoundary>
            <RouterProvider router={router} />
        </ErrorBoundary>
    </StrictMode>,
)
