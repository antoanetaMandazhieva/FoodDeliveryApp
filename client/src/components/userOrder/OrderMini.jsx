import { Link } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { getCookie } from '../../util/cookies';
import UserOrderItem from './UserOrderItem';

const OrderMini = ({ order, incrementCount, decrementCount, handleCartClick, deleteFromOrder, userId }) => {
    console.log(userId)
    const [state, setState] = useState();

    useEffect(() => {
        setState({order: order, incrementCount: incrementCount, 
            decrementCount: decrementCount, deleteFromOrder: deleteFromOrder})
    }, []);

    return (
        <section className='h-screen w-full bg-zinc-900 overflow-y-scroll'>
            <svg onClick={handleCartClick} xmlns='http://www.w3.org/2000/svg' fill='none' 
                viewBox='0 0 24 24' stroke-width='1.5' 
                stroke='currentColor' 
                className='size-12 stroke-ivory'>
                <path stroke-linecap='round' stroke-linejoin='round' d='M6 18 18 6M6 6l12 12' />
            </svg>
            <div className='h-screen w-full overflow-y-scroll bg-zinc-900 flex flex-col justify-around items-center px-3'>
                {order.length <= 0 
                    ? <h1 className='text-7xl text-ivory font-playfair font-bold place-self-center'>
                        No products in order yet
                    </h1> 
                    : order.map((product, i) => {
                        return (
                            <div key={i} className='flex max-md:flex-col justify-between items-center'>
                                <UserOrderItem 
                                    productId={product.id}
                                    productDescription={product.description}
                                    productName={product.name}
                                    productPrice={product.price}
                                    deleteFromOrder={deleteFromOrder}
                                />
                                <div className='h-full w-[40%] md:w-[20%] lg:w-[15%] flex justify-around items-center max-md:mb-3'>
                                    <svg onClick={() => decrementCount(product.id)} xmlns='http://www.w3.org/2000/svg' 
                                        viewBox='0 0 24 24' stroke-width='1.5'
                                        stroke='currentColor' 
                                        className='size-10 fill-teal-400 hover:fill-teal stroke-ivory'>
                                        <path stroke-linecap='round' stroke-linejoin='round' d='M15 12H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z' />
                                    </svg>
                                    <h1 className='text-3xl text-ivory font-playfair rounded-full'>
                                        {product.count}
                                    </h1>
                                    <svg onClick={() => incrementCount(product.id)} xmlns='http://www.w3.org/2000/svg' 
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
            {order.length > 0 && 
                <Link to={`/order/${userId}`} state={ order }>
                    <div className='w-full flex justify-center items-center'>
                        <div className='w-[40%] h-14 bg-peach-400 hover:bg-peach-100
                            rounded-4xl mb-5 flex justify-center items-center'>
                            <h1 className='text-3xl text-ivory font-quicksand font-bold'> 
                                Checkout
                            </h1>
                        </div>
                    </div>
                </Link>
            }
        </section>
    );
}

export default OrderMini;