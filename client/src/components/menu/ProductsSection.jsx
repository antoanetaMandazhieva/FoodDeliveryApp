import { useEffect, useState } from 'react';
import { getRestaurantProductUrl } from '../../util/getImageURL';
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ProductsSection = ({ products, addToOrder }) => {
    const handleAddToOrder = (productId) => {
        addToOrder(productId);
        toast.success('Product added to cart!', { autoClose: 2000 });
    };

    return (
        <div className={`grid md:grid-rows-${Math.ceil(products.length / 2)} 
            lg:grid-rows-${Math.ceil(products.length / 3)} md:grid-cols-2 lg:grid-cols-3
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
                            src={getRestaurantProductUrl('restaurant_images', `product_${product.id}.jpg`)}
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
                            {(parseFloat(product.price).toFixed(2))}
                        </h2>
                    </div>

                    <div className='w-full mt-3'>
                        {/* {} */}
                    </div>

                    <div className='w-full mt-2 grid-cols-subgrid col-span-2 relative hover:cursor-pointer' onClick={() => handleAddToOrder(product.id)}>
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
    );
}

export default ProductsSection;