import axios from 'axios';
import { useEffect, useState } from 'react';
import { getCookie } from '../../util/cookies';
import { toast } from 'react-toastify';

// ADD TRY/CATCH BLOCKS FOR BUTTONS

const OrderHistoryItem = ({ orderId, clientAddress, clientPhone, 
    restaurantName, totalPrice, orderStatus, createdAt, userRole, userId, isImpersonatingClient }) => {
    
    const buttonsLoad = () => {
        switch (userRole) {
            case 'CLIENT':
                return (
                    <div className='grid-rows-subgrid row-span-2 grid-cols-subgrid col-span-6 flex justify-around items-center'>
                        <button 
                            className='order-btn bg-peach-400'
                            onClick={() => navigate(`/profile/${userId}/orders/track/${orderId}`)}
                        >
                            Track Order
                        </button>
                        <button 
                            className='order-btn bg-red-700'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/cancel/${userId}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Cancelled Order', { autoClose: 2000 });
                                    window.location.reload();
                                }
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 })
                                }
                            }}
                        >
                            Cancel Order
                        </button>
                    </div>
                ); 
            
            case 'EMPLOYEE':
                return (
                    <div className='grid-rows-subgrid row-span-2 grid-cols-subgrid col-span-6 flex justify-between items-center'>
                        <button
                            className='order-btn bg-emerald-600'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/accept?employeeId=${userId}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Accepted order', { autoClose: 2000 });
                                    window.location.reload();
                                }
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 })
                                }
                            }}
                        >
                            Accept Order
                        </button>
                        <button 
                            className='order-btn bg-amber-300'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/status?employeeId=${userId}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Changed to preparing', { autoClose: 2000 });
                                    window.location.reload();
                                }
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 });
                                }
                            }}
                        >
                            Change to preparing
                        </button>
                    </div>
                );
            
            case 'SUPPLIER':
                return (
                    // WHEN CREATING THE SUPPLIER INTERFACE, I WILL HAVE TO DELETE THE TAKEN ORDER FROM THE AVAILABLE ORDERS
                    <div className='grid-rows-subgrid row-span-2 grid-cols-subgrid col-span-6 flex justify-around items-center'>
                        <button 
                            className='order-btn bg-peach-400'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/assign/${2}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Asigned order', { autoClose: 2000 });
                                    window.location.reload();
                                }
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 })
                                }
                            }}
                        >
                            Asign Order
                        </button>
                        <button 
                            className='order-btn bg-amber-300'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/take?supplierId=${2}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Took order', { autoClose: 2000 });
                                    window.location.reload();
                                }
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 })
                                }
                            }}
                        >
                            Take Order
                        </button>
                        <button 
                            className='order-btn bg-emerald-600'
                            onClick={ async () => {
                                try {
                                    const { data } = await axios.put(`http://localhost:8080/api/orders/${orderId}/finish/${2}`, {
                                        headers: {
                                            'Content-Type': 'application/json'
                                        }
                                    });
                                    console.log(data);
                                    toast.success('Finished order', { autoClose: 2000 });
                                    window.location.reload();
                                } 
                                catch (e) {
                                    console.error(e);
                                    toast.error(e.response.data.message, { autoClose: 2000 });
                                }
                            }}
                        >
                            Finish Order
                        </button>
                    </div>
                );
        }
    }

    return (
        <div id={orderId} className='grid grid-rows-7 grid-cols-6 p-3 border-2 border-peach-400 bg-ivory rounded-2xl w-full md:w-[80%] lg:w-[60%] xl:w-[50%]'>
            <h1 className='grid-cols-subgrid col-span-2 text-sm sm:text-xl font-quicksand'>
                Order: {orderId}
            </h1>


            <div className='grid-cols-subgrid col-span-4 flex justify-around items-center'>
                <h1 className='text-sm sm:text-xl font-quicksand font-bold'>
                    Date:
                </h1>
                <h2 className='text-sm sm:text-xl text-zinc-800 font-quicksand'>
                    {createdAt}
                </h2>
            </div>

            <h2 className='grid-cols-subgrid col-span-2 text-sm sm:text-xl font-quicksand'>
                {orderStatus}
            </h2>

            <div className='grid-cols-subgrid col-span-4 flex justify-around items-center'>
                <h1 className='text-sm sm:text-xl font-quicksand font-bold'>
                    Contact:
                </h1>
                <h2 className='text-sm sm:text-xl text-zinc-800 font-quicksand'>
                    {clientPhone}
                </h2>
            </div>

            <div className='grid-cols-subgrid col-span-6 grid-rows-subgrid row-span-2 flex justify-around items-center'>
                <h1 className='max-sm:text-xs sm:text-md font-quicksand font-bold'>
                    To:
                </h1>
                <h2 className='max-sm:text-xs sm:text-md text-zinc-800 font-quicksand'>
                    {clientAddress}
                </h2>
            </div>

            <div className='grid-cols-subgrid col-span-6 grid-rows-subgrid row-span-2 flex justify-between items-center'>
                <div className='flex items-center'>
                    <h1 className='text-sm sm:text-xl font-quicksand font-bold mr-2'>
                        From:
                    </h1>
                    <h2 className='text-sm sm:text-xl text-zinc-800 font-quicksand'>
                        {restaurantName}
                    </h2>
                </div>
                <div className='flex items-center'>
                    <h1 className='text-sm sm:text-xl font-quicksand font-bold mr-2'>
                        Total:
                    </h1>
                    <h2 className='text-sm sm:text-xl text-zinc-800 font-quicksand'>
                        {parseFloat(totalPrice).toFixed(2) + 'lv.'}
                    </h2>
                </div>
            </div>
            
            {buttonsLoad()}

        </div>
    );
}

export default OrderHistoryItem;