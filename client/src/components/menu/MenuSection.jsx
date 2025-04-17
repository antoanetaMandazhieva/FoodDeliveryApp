import sushi_plate from '../../assets/images/page_images/sushi_plate.jpg'
import { useState, useEffect } from 'react';
import { getCookie } from '../../util/cookies';
import OrderMini from '../userOrder/OrderMini';

// NB! EACH PRODUCT div MUST!!! HAVE AN ID, SAME AS THE PRODUCT ID

const MenuSection = ({ products, order, addToOrder }) => {
    const countSalads = 6;
    const countMains= 12;
    const countDesserts = 6;
    
    console.log(order);

    return (
        <section className='flex flex-col justify-between overflow-y-scroll'>
            <div id='salads-section' className='flex flex-col justify-around'>
                <h1 className='text-5xl font-bold font-quicksand'>
                    Salads
                </h1>
                <div className={`grid md:grid-rows-${Math.ceil(countSalads / 2)} 
                    lg:grid-rows-${Math.ceil(countSalads / 3)} md:grid-cols-2 lg:grid-cols-3
                    sm:gap-x-3 lg:gap-x-6 max-sm:gap-y-4 sm:gap-y-6 lg:gap-y-12 bg-ivory py-6
                    overflow-y-scroll`}
                >
                    {products.map((product, i) => <div 
                        id={product.id} key={i} 
                        className='h-56 grid grid-rows-3 grid-cols-4 grid-flow-row 
                            border-2 border-peach-100 rounded-4xl gap-2 p-1'>

                        <div className='grid-rows-subgrid grid-cols-subgrid col-span-2 row-span-2'>
                            <img 
                                className='rounded-4xl max-sm:size-full sm:h-full sm:w-auto md:size-full'
                                src={sushi_plate}
                            />
                        </div>

                        <div className='grid-cols-subgrid col-span-2'>
                            <h1 className='md:text-md lg:text-xl font-quicksand font-bold'>
                                {product.name}
                            </h1>
                        </div>

                        <div className='grid-cols-subgrid col-span-2'>
                            <p className='max-sm:text-xs sm:text-sm text-zinc-500 font-quicksand'>
                                {product.description}
                            </p>
                        </div>

                        <div className='w-full mt-3'>
                            <h2 className='md:text-md lg:text-xl font-quicksand font-bold'>
                                {product.price}
                            </h2>
                        </div>

                        <div className='w-full mt-3'>
                            {/* logic for old price perhaps */}
                        </div>

                        <div className='w-full mt-2 grid-cols-subgrid col-span-2 relative' onClick={() => addToOrder(product.id)}>
                            <svg xmlns='http://www.w3.org/2000/svg' fill='none' 
                                viewBox='0 0 24 24' stroke-width='1.5' 
                                stroke='currentColor' 
                                className='size-8 absolute right-2 hover:fill-teal'>
                                <path stroke-linecap='round' stroke-linejoin='round' d='M12 9v6m3-3H9m12 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z' />
                            </svg>
                        </div>
                    </div>
                    )}
                </div>
                
            </div>
        </section>
    );
}

export default MenuSection