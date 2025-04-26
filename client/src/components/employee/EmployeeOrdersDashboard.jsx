import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Navigation from '../common/Navigation';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';
import OrderHistoryItem from '../common/OrderHistoryItem';
import { getCookie } from '../../util/cookies';

const EmployeeOrdersDashboard = () => {
    const { employeeId } = useParams();

    const [pendingOrders, setPendingOrders] = useState([]);
    const [acceptedOrders, setAcceptedOrders] = useState([]);
    const [employeeOrders, setEmployeeOrders] = useState([]);
    const [orders, setOrders] = useState([]);
    const [role, setRole] = useState();

    console.log(role);

    const getPending = async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/orders/status/PENDING/${employeeId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setPendingOrders(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    const getAccepted = async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/orders/status/ACCEPTED/${employeeId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setAcceptedOrders(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    const getCurrentEmployeeOrders = async () => {
        try {
            const { data } = await axios.get(`http://localhost:8080/api/orders/client/${employeeId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setEmployeeOrders(data);
        }
        catch (e) {
            console.error(e);
        }
    }

    useEffect(() => {
        setRole(getCookie('userRole')); 
        getPending();
        getAccepted();
        getCurrentEmployeeOrders();
    }, []);

    useEffect(() => {
        const combinedOrders = [...pendingOrders, ...acceptedOrders].filter(order =>
            !employeeOrders.some(empOrder => empOrder.id === order.id)
        );
        setOrders(combinedOrders);
    }, [pendingOrders, acceptedOrders, employeeOrders])

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
                    userId={employeeId}
                />)}
            </div>
        </div>
    );
}

export default EmployeeOrdersDashboard;