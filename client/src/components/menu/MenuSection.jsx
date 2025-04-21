import ProductsSection from './ProductsSection';

// NB! EACH PRODUCT div MUST!!! HAVE AN ID, SAME AS THE PRODUCT ID

const MenuSection = ({ products, order, addToOrder }) => {

    const salads = products.filter(product => product.category === 'SALADS');
    const mains = products.filter(product => product.category === 'MAIN');
    const desserts = products.filter(product => product.category === 'DESSERTS');
    
    console.log(order);

    return (
        <section className='flex flex-col justify-between overflow-y-scroll'>

            {/* Salads */}

            <div id='salads-section' className='flex flex-col justify-around'>
                <h1 className='text-5xl font-bold font-quicksand'>
                    Salads
                </h1>
                <ProductsSection 
                    products={salads}
                    addToOrder={addToOrder}
                />
            </div>

            {/* Mains */}

            <div id='mains-section' className='flex flex-col justify-around'>
                <h1 className='text-5xl font-bold font-quicksand'>
                    Mains
                </h1>
                <ProductsSection 
                    products={mains}
                    addToOrder={addToOrder}
                />
            </div>

            {/* Desserts */}

            <div id='desserts-section' className='flex flex-col justify-around'>
                <h1 className='text-5xl font-bold font-quicksand'>
                    Desserts
                </h1>
                <ProductsSection 
                    products={desserts}
                    addToOrder={addToOrder}
                />
            </div>
        </section>
    );
}

export default MenuSection