import Navigation from '../common/Navigation';
import MenuSection from './MenuSection';
import SearchBarMenu from './SearchBarMenu';
import OrderMini from '../userOrder/OrderMini';
import { useState, useEffect, useRef } from 'react';
import { useGSAP } from '@gsap/react';
import { getCookie } from '../../util/cookies';
import gsap from 'gsap';

const RestaurantMenu = () => {
    const [products, setProducts] = useState([]);
    const [order, setOrder] = useState([]);
    const [cartIsClicked, setCartIsClicked] = useState(() => false);
    const [userId, setUserId] = useState();

    const menuSectionRef = useRef(null);
    const searchBarMenuRef = useRef(null);
    const orderMiniRef = useRef(null);

    useGSAP(() => {
        gsap.set(orderMiniRef.current, {
            x: '100%',
            opacity: 0
        })
    }, []);

    useGSAP(() => {
        if (cartIsClicked) {
            gsap.set(searchBarMenuRef.current, {
                opacity: 0.2,
                zIndex: 10
            });
            gsap.set(menuSectionRef.current, {
                opacity: 0.2,
                zIndex: 10
            })

            gsap.to(orderMiniRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                delay: 0.1,
                duration: 1
            });
        }
        else {
            gsap.to(orderMiniRef.current, {
                opacity: 0,
                zIndex: 10,
                x: '100%',
                duration: 0.7
            });

            gsap.to(searchBarMenuRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                duration: 1,
                delay: 0.3
            });
            gsap.to(menuSectionRef.current, {
                opacity: 1,
                zIndex: 20,
                x: 0,
                ease: 'power3.inOut',
                duration: 1,
                delay: 0.3
            })
        }
    }, [cartIsClicked])

    useEffect(() => {
        // products: [{id: , category: , description: , ...}]
        // DEMO:

        setProducts([
            {id: '1', category: 'MAIN', description: 'Classic pasta served in a rich meat sauce with tomatoes, garlic, and basil.', is_available: '1', name: 'Spaghetti Bolognese', price: '14.50', cuisine_id: '1', restaurant_id: '1'},
            {id: '2', category: 'SALADS', description: 'Thin-crust pizza topped with fresh tomato sauce, basil, and mozzarella.', is_available: '1', name: 'Margherita Pizza', price: '15.00', cuisine_id: '1', restaurant_id: '1'}
        ]);

        setUserId(getCookie('userId'));
    }, []);
    
    console.log(userId);

    const addToOrder = (id) => {
        const product = products.find(pr => pr.id === id);

        setOrder(prev => {
            return [
                ...prev,
                { ...product, count: 1 }
            ];
        });
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

    return (
        <div className='h-screen overflow-y-scroll bg-ivory'>
            <Navigation />
            <main className='w-full flex-grow bg-ivory'>
                <div className='w-full inset-0 z-10 absolute' ref={orderMiniRef}>
                    <OrderMini 
                        order={order}
                        incrementCount={incrementProductCount}
                        decrementCount={decrementProductCount}
                        handleCartClick={() => setCartIsClicked(prev => !prev)}
                        deleteFromOrder={deleteFromOrder}
                        userId={userId}
                    />
                </div>
                <div className='w-full inset-0 z-20 p-6' ref={searchBarMenuRef}>
                    <SearchBarMenu 
                        handleCartClick={() => setCartIsClicked(prev => !prev)}
                    />
                </div>
                <div className='w-full inset-0 z-20 p-6' ref={menuSectionRef}>
                    <MenuSection 
                        products={products}
                        order={order}
                        addToOrder={addToOrder}
                    />
                </div>
            </main>
        </div>
    );
}

export default RestaurantMenu;