import Navigation from '../common/Navigation';
import MenuSection from './MenuSection';
import SearchBarMenu from './SearchBarMenu';
import OrderMini from '../userOrder/OrderMini';
import { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { useGSAP } from '@gsap/react';
import { getCookie } from '../../util/cookies';
import gsap from 'gsap';
import axios from 'axios';
import { ToastContainer } from 'react-toastify';

const RestaurantMenu = () => {
    const { restaurantName } = useParams();

    const [products, setProducts] = useState([]);
    const [restaurantDetails, setRestaurantDetails] = useState([]);
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

    console.log(restaurantDetails)
    // console.log(products)
    useEffect(() => {
        const handleProducts = async () => {
            try {
                const { data } = await axios.get(`http://localhost:8080/api/restaurants/${restaurantName}/products`, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                setProducts(data);
            } catch (e) {
                console.error(e);
                alert('Could not fetch products');
            }
        }

        const handleRestaurantDetails = async () => {
            try {
                const { data } = await axios.get(`http://localhost:8080/api/restaurants/name/${restaurantName}`, {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });
                setRestaurantDetails(data);
            } catch (e) {
                console.error(e);
                alert('Could not fetch restaurant details');
            }
        }

        handleProducts();
        handleRestaurantDetails();

        setUserId(getCookie('userId'));
    }, []);

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
            <ToastContainer />
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
                        restaurantDetails={restaurantDetails}
                        orderCount={order.length}
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