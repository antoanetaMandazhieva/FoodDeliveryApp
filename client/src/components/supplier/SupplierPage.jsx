import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Navigation from '../common/Navigation';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';
import OrderHistoryItem from '../common/OrderHistoryItem';
import { getCookie } from '../../util/cookies';

const SupplierPage = () => {
    const { supplierId } = useParams();

    const [availableOrders, setAvailableOrders] = useState([]);
    const [supplierOrders, setSupplierOrders] = useState([]);
    const [orders, setOrders] = useState([]);
    const [role, setRole] = useState();
    
    const getAvailableOrders = async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/orders/available/${supplierId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setAvailableOrders(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    const getCurrentSupplierOrders = async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/orders/client/${supplierId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setSupplierOrders(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    useEffect(() => {
        setRole(getCookie('userRole')); 
        getAvailableOrders();
        getCurrentSupplierOrders();
    }, []);

    useEffect(() => {
        const combinedOrders = availableOrders.filter(order =>
            !supplierOrders.some(supOrder => supOrder.id === order.id)
        );
        setOrders(combinedOrders);
    }, [availableOrders, supplierOrders]);

    return (
        <div className='flex flex-col h-screen justify-center items-center bg-ivory'>
            <Navigation />
            <ToastContainer />
            <div className='user-order overflow-y-scroll px-2'>
                {orders.map((order, i) => <OrderHistoryItem 
                    key={i}
                    orderId={order.id}
                    clientAddress={order.clientAddress}
                    clientPhone={order.clientPhone}
                    restaurantName={order.restaurantName}
                    totalPrice={order.totalPrice}
                    orderStatus={order.orderStatus}
                    createdAt={order.createdAt}
                    userRole={role}
                    userId={supplierId}
                />)}
            </div>
        </div>
    );
}

export default SupplierPage;