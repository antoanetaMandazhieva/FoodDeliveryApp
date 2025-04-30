import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Navigation from '../common/Navigation';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';
import OrderHistoryItem from '../common/OrderHistoryItem';
import { getCookie } from '../../util/cookies';

const SupplierAssignedOrders = () => {
    const { supplierId } = useParams();

    const [assignedOrders, setAssignedOrders] = useState([]);
    const [role, setRole] = useState();

    useEffect(() => {
        const handleOrders = async () => {
            const { data } = await axios.get(`http://localhost:8080/api/orders/supplier/${supplierId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            const ordersToDisplay = data.filter(order => order.orderStatus !== 'DELIVERED');
            setAssignedOrders(ordersToDisplay);
        }
        handleOrders();

        setRole(getCookie('userRole'));
    }, []);

    return (
        <div className='flex flex-col h-screen justify-center items-center bg-ivory'>
            <Navigation />
            <ToastContainer />
            <div className='user-order overflow-y-scroll px-2'>
                {assignedOrders.map((order, i) => <OrderHistoryItem 
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

export default SupplierAssignedOrders;