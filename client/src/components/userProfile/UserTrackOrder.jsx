import axios from 'axios';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Navigation from '../common/Navigation';
import OrderTrackerVisual from './OrderTrackerVisual';
import { ToastContainer } from 'react-toastify';
import { toast } from 'react-toastify';

const UserTrackOrder = () => {
    const { userId, orderId } = useParams();

    const [order, setOrder] = useState();
    console.log(order);

    useEffect(() => {
        const handleOrder = async () => {
            const { data } = await axios.get(`http://localhost:8080/api/orders/get-order-info/${orderId}?clientId=${userId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            setOrder(data);
        }
        handleOrder();
    }, []);

    const cancelOrder = async () => {
        try {
            const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/cancel/${userId}`, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            console.log(data);
            toast.success('Cancelled Order', { autoClose: 2000 });
            setTimeout(() => window.location.reload(), 2500);
        }
        catch (e) {
            console.error(e);
            toast.error(e.response.data.message, { autoClose: 2000 })
        }
    }

    return (
        <div className='flex flex-col justify-between items-center h-screen overflow-y-scroll bg-peach-400'>
            <Navigation />
            <ToastContainer />
            <div className='flex flex-col justify-between items-center m-10 py-5 bg-ivory/5 rounded-4xl 
                bg-clip-padding backdrop-filter backdrop-blur-2xl border border-ivory shadow-zinc-800 shadow-xl
                w-full sm:w-[90%] md:w-[80%] h-screen bg'>
                
                <h1 className='text-3xl text-ivory font-quicksand font-bold'>
                    Order: {orderId}
                </h1>

                <OrderTrackerVisual
                    status={order && order.orderStatus}
                />
                
                {(order && order.orderStatus === 'PENDING') && <button
                        className='bg-ivory rounded-4xl p-3 text-2xl text-red-600 font-quicksand font-bold hover:scale-95'
                        onClick={cancelOrder}        
                    >
                        Cancel Order
                    </button>}
                
                <div className='flex justify-around items-center w-full'>
                    <h2 className='text-2xl text-ivory font-quicksand font-bold'>
                        Total Price: {order && parseFloat(order.totalPrice).toFixed(2)} lv.
                    </h2>
                    <h3 className='text-2xl text-ivory font-quicksand'>
                        Discount: {order && order.discount ? order.discount : '0%'}
                    </h3>
                </div>

                <h2 className='text-xl text-ivory font-quicksand'>
                    Address: {order && order.clientAddress}
                </h2>
            </div>
        </div>
    );
}

export default UserTrackOrder;