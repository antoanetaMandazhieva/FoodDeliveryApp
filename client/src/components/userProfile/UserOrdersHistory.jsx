import axios from 'axios';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import OrderHistoryItem from '../common/OrderHistoryItem';
import Navigation from '../common/Navigation';
import { getCookie } from '../../util/cookies';
import { ToastContainer } from 'react-toastify';

const UserOrdersHistory = () => {
    const { userId } = useParams();

    const [orders, setOrders] = useState([]);
    const [userRole, setUserRole] = useState();
    const [isImpersonatingClient, setImpersonating] = useState('CLIENT');

    useEffect(() => {
        setUserRole(getCookie('userRole'));
    }, []);

    console.log(userRole);

    useEffect(() => {
        const handleOrders = async () => {
            const { data } = await axios.get(`http://localhost:8080/api/orders/client/${userId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            setOrders(data);
        } 
        handleOrders();
    }, []);

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
                    userRole={isImpersonatingClient}
                    userId={userId}
                />)}
            </div>
        </div>
    );
}

export default UserOrdersHistory;