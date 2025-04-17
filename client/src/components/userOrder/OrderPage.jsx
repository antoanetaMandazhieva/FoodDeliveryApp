import { useParams, useLocation } from 'react-router-dom';
import { useState, useEffect } from 'react';
import Navigation from '../common/Navigation';
import CreateBubbles from '../common/CreateBubbles';
import UserOrderItem from './UserOrderItem';

const OrderPage = () => {
    const { state } = useLocation();
    const userId = useParams();

    const [order, setOrder] = useState(state);
    const [addresses, setAddresses] = useState([]);
    const [selectedAddress, setSelectedAddress] = useState();
    const [requestData, setRequestData] = useState({
        restaurantId: order.length > 0 && order[0].restaurant_id,
        productIds: order.length > 0 && order.map(product => product.id) 
    });

    console.log(requestData)

    useEffect(() => {
        // GET USER DETAILS -> GET ADDRESSES
        setAddresses([
            {street: 'Dondukov 91 Blvd.', city: 'Sofia Center', postalCode: '1000', country: 'Bulgaria'},
            {street: 'Pirin 12A street', city: 'Sofia Borovo', postalCode: '1000', country: 'Bulgaria'}
        ])
    }, []);

    const handleRequest = () => {
        if (selectedAddress === 'none') {
            console.warn('Choose an address');
        }
        else {
            const address = addresses.find(addr => addr.street === selectedAddress);
            setRequestData(prev => {
                return {
                    ...prev,
                    address: address
                };
            });
        }
    }

    const deleteFromOrder = (id) => {
        setOrder(prevOrder => prevOrder.filter(prod => prod.id !== id));
    }

    const incrementProductCount = (productId) => {
        setOrder(prevOrder => 
            prevOrder.map(product => 
                product.id === productId 
                ? {...product, count: product.count + 1 >= 10 ? 10 : product.count + 1} 
                : product))
    }

    const decrementProductCount = (productId) => {
        setOrder(prevOrder => 
            prevOrder.map(product => 
                product.id === productId
                ? {...product, count: product.count - 1 <= 1 ? 1 : product.count - 1 }
                : product
            )
        )
    }

    const calculateOrderTotal = () => {
        let sum = 0;
        order.forEach(product => sum += (product.price * product.count));

        return sum.toFixed(2);
    }

    return (
        <div className='h-screen w-full bg-peach-400 overflow-y-scroll'>
            <Navigation />
            <main className='flex flex-col justify-around items-center relative'>
                {order.length <= 0
                    ? <h1 className='text-7xl text-ivory font-playfair font-bold place-self-center'>
                    No products in order yet
                    </h1>
                    : <section className='user-order overflow-y-scroll z-10'>
                    <div className='h-screen w-full overflow-y-scroll bg-none flex flex-col justify-around items-center px-3'>
                        {order.map((product, i) => {
                            return (
                                <div key={i} className='flex max-md:flex-col justify-between items-center'>
                                    <UserOrderItem 
                                        productId={product.id}
                                        productDescription={product.description}
                                        productName={product.name}
                                        productPrice={product.price}
                                        deleteFromOrder={() => deleteFromOrder(product.id)}
                                    />
                                    <div className='h-full w-[40%] md:w-[20%] lg:w-[15%] flex justify-around items-center max-md:mb-3'>
                                        <svg onClick={() => decrementProductCount(product.id)} xmlns='http://www.w3.org/2000/svg' 
                                            viewBox='0 0 24 24' stroke-width='1.5'
                                            stroke='currentColor' 
                                            className='size-10 fill-teal-400 hover:fill-teal stroke-ivory'>
                                            <path stroke-linecap='round' stroke-linejoin='round' d='M15 12H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z' />
                                        </svg>
                                        <h1 className='text-3xl text-ivory font-playfair rounded-full'>
                                            {product.count}
                                        </h1>
                                        <svg onClick={() => incrementProductCount(product.id)} xmlns='http://www.w3.org/2000/svg' 
                                            viewBox='0 0 24 24' stroke-width='1.5' 
                                            stroke='currentColor' 
                                            className='size-10 fill-teal-400 hover:fill-teal stroke-ivory'>
                                            <path stroke-linecap='round' stroke-linejoin='round' d='M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z' />
                                        </svg>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                    <div className='w-full flex justify-around items-center'>
                        <h1 className='text-3xl text-ivory font-quicksand font-bold'>
                            Address:
                        </h1>
                        <select 
                            className='bg-ivory text-black text-xl font-quicksand'
                            value={selectedAddress}
                            onChange={e => setSelectedAddress(e.target.value)}
                        >
                            <option className='text-peach-400' value='none'>Select Address</option>  
                                {addresses.map((addr, i) => <option key={i} value={addr.street} className='text-peach-400'>
                                    {`${addr.street}, ${addr.city}, ${addr.postalCode}, ${addr.country}`}
                            </option>)}
                        </select>
                    </div>
                    <div className='w-full min-h-14 flex justify-around items-center'>
                        <h1 className='text-3xl text-ivory font-quicksand font-bold'>
                            Total Price: {calculateOrderTotal()} 
                        </h1>
                        <button className='w-[40%] h-full rounded-2xl bg-ivory
                            text-xl text-peach-400 font-quicksand font-bold
                            hover:scale-90'
                            onClick={handleRequest}
                        >
                            Pay (Cash)
                        </button>
                    </div>
                </section>
                }
                {order.length > 0 && <div className='absolute inset-0 z-0 max-md:hidden'>
                    <CreateBubbles />
                </div>}
            </main>
        </div>
    );
}

export default OrderPage;