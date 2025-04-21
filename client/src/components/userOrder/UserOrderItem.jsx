import { getRestaurantProductUrl } from "../../util/getImageURL";


const UserOrderItem = ({ productId, productName, productDescription, productPrice, deleteFromOrder }) => {
    return (
        <div id={productId} className='max-sm:w-full sm:w-[90%] md:w-[70%] grid grid-rows-2 grid-cols-5 grid-flow-col gap-x-6 gap-y-3 border-2 border-peach-100 rounded-xl mb-4'>
            <div className='grid-rows-subgrid row-span-2 grid-cols-subgrid col-span-2'>
                <img className='h-full w-auto rounded-xl' 
                    src={getRestaurantProductUrl('restaurant_images', `product_${productId}.jpg`)}
                />
            </div>
            <div className='grid-cols-subgrid col-span-2'>
                <h1 className='md:text-lg lg:text-2xl text-ivory font-quicksand font-bold'>
                    {productName}
                </h1>
            </div>
            <div className='grid-cols-subgrid col-span-2'>
                <p className='max-sm:text-xs sm:text-sm text-zinc-500 font-quicksand'>
                    {productDescription}
                </p>
            </div>
            <div className='relative'>
                <svg xmlns='http://www.w3.org/2000/svg' 
                    fill='none' viewBox='0 0 24 24' stroke-width='1.5' 
                    stroke='currentColor' 
                    className='size-9 stroke-zinc-500 absolute right-2'
                    onClick={deleteFromOrder}
                >
                    <path stroke-linecap='round' stroke-linejoin='round' d='M6 18 18 6M6 6l12 12' />
                </svg> 
            </div>
            <div className='relative'>
                <h2 className='text-sm md:text-md lg:text-xl text-ivory 
                    font-quicksand font-bold absolute right-2 bottom-2'>
                    {parseFloat(productPrice).toFixed(2)}
                </h2>
            </div>
        </div>
    );
}

export default UserOrderItem;